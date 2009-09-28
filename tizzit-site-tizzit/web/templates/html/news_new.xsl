<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ctmpl="http://www.conquest-cms.net/template" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xsl:include href="common.xsl"/>

	<xsl:param name="sort" select="0"/>
	<xsl:param name="newsNr" select="'0'"/>
	<xsl:param name="sortBy" select="'date'"/>
	<xsl:param name="currentDate" select="''"/>

	<!--day, month and year of the currentDate-->
	<xsl:variable name="day">
		<xsl:value-of select="number(substring($currentDate,1,2))"/>
	</xsl:variable>
	<xsl:variable name="month">
		<xsl:value-of select="number(substring($currentDate,4,2))"/>
	</xsl:variable>
	<xsl:variable name="year">
		<xsl:value-of select="number(substring($currentDate,7,4))"/>
	</xsl:variable>

	<xsl:param name="oppOrder">
		<xsl:choose>
			<xsl:when test="$order='ascending'">descending</xsl:when>
			<xsl:otherwise>ascending</xsl:otherwise>
		</xsl:choose>
	</xsl:param>

	<xsl:param name="order">
		<xsl:choose>
			<xsl:when test="//source/all/content/news/sortOrder/value!=''">
				<xsl:value-of select="//source/all/content/news/sortOrder/value"/>
			</xsl:when>
			<xsl:otherwise>descending</xsl:otherwise>
		</xsl:choose>
	</xsl:param>

	<xsl:template match="content" mode="format" priority="1.1">
		<xsl:choose>
			<xsl:when test="$newsNr != '0'">
				<xsl:apply-templates select="news" mode="newsDetail"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="news" mode="newsList"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="news" mode="sort" priority="1.1">
		<xsl:choose>
			<xsl:when test="$sortBy='date'">
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="ascending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:when test="$order='descending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="descending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$sortBy='title'">
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newslocality" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="ascending"/>
							<xsl:sort select="newslocality" data-type="text" order="descending"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="item" mode="format">
		<xsl:if test="newsname!=''">
			<xsl:choose>
				<!--if there's no start date-->
				<xsl:when test="deploy-start-stop/start/year='' or not(deploy-start-stop)">
					<!--<xsl:when test="not(deploy-start-stop)">-->
					<xsl:apply-templates select="." mode="checkStopTime">
						<!-- Durchschleusen der Position -->
						<xsl:with-param name="itemPosition">
							<xsl:value-of select="position()"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:when>
				<xsl:otherwise>
					<!--	Test the Year -->
					<xsl:choose>
						<xsl:when test="$year = deploy-start-stop/start/year">
							<!--Test the month-->
							<xsl:choose>
								<xsl:when test="$month = deploy-start-stop/start/month">
									<!--Test the day-->
									<xsl:choose>
										<xsl:when test="$day &gt;= deploy-start-stop/start/day">
											<xsl:apply-templates select="." mode="checkStopTime">
												<!-- Durchschleusen der Position -->
												<xsl:with-param name="itemPosition">
												<xsl:value-of select="position()"/>
												</xsl:with-param>
											</xsl:apply-templates>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$month &gt; deploy-start-stop/start/month">
									<xsl:apply-templates select="." mode="checkStopTime">
										<!-- Durchschleusen der Position -->
										<xsl:with-param name="itemPosition">
											<xsl:value-of select="position()"/>
										</xsl:with-param>
									</xsl:apply-templates>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$year &gt; deploy-start-stop/start/year">
							<xsl:apply-templates select="." mode="checkStopTime">
								<!-- Durchschleusen der Position -->
								<xsl:with-param name="itemPosition">
									<xsl:value-of select="position()"/>
								</xsl:with-param>
							</xsl:apply-templates>
						</xsl:when>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template match="item" mode="checkStopTime">
		<xsl:param name="itemPosition"/>
		<xsl:choose>
			<xsl:when test="deploy-start-stop/stop/year='' or not(deploy-start-stop)">
				<xsl:apply-templates select="." mode="format2">
					<xsl:with-param name="itemPosition">
						<xsl:value-of select="$itemPosition"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<!--	Test the Year -->
				<xsl:choose>
					<xsl:when test="$year = deploy-start-stop/stop/year">
						<xsl:choose>
							<xsl:when test="$month = deploy-start-stop/stop/month">
								<!--Test the day-->
								<xsl:choose>
									<xsl:when test="$day &lt;= deploy-start-stop/stop/day">
										<xsl:apply-templates select="." mode="format2">
											<!-- Durchschleusen der Position -->
											<xsl:with-param name="itemPosition">
												<xsl:value-of select="$itemPosition"/>
											</xsl:with-param>
										</xsl:apply-templates>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="$month &lt; deploy-start-stop/stop/month">
								<xsl:apply-templates select="." mode="format2">
									<xsl:with-param name="itemPosition">
										<xsl:value-of select="$itemPosition"/>
									</xsl:with-param>
								</xsl:apply-templates>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$year &lt; deploy-start-stop/stop/year">
						<xsl:apply-templates select="." mode="format2">
							<xsl:with-param name="itemPosition">
								<xsl:value-of select="$itemPosition"/>
							</xsl:with-param>
						</xsl:apply-templates>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="news" mode="newsDetail">
		<xsl:apply-templates select="newslist" mode="newsDetail"/>
	</xsl:template>
	
	<xsl:template match="newslist" mode="newsDetail">
		<xsl:apply-templates select="item[@timestamp = $newsNr]" mode="newsDetail"/>
	</xsl:template>

	<xsl:template match="item" mode="newsDetail">
		<div class="metadata">
			<xsl:apply-templates select="newsdate" mode="format"/>
		</div>
		<div class="announcement_text">
			<xsl:apply-templates select="text" mode="format"/>
		</div>
	</xsl:template>

	<xsl:template match="newsname" mode="format" priority="1.1">
		<xsl:apply-templates mode="format"/>
	</xsl:template>

	<xsl:template match="newsEndDate" mode="format" priority="1.1">
		<xsl:if test="normalize-space(year) != ''">
			<xsl:text>-&#160;</xsl:text>
			<xsl:apply-templates mode="format"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="newsdate" mode="format">
		<xsl:param name="day" select="day"/>
		<xsl:param name="month" select="month"/>
		<xsl:param name="year" select="year"/>

		<xsl:text>Published on </xsl:text>
		<xsl:variable name="date-string">
			<xsl:value-of select="$year"/>
			<xsl:text>-</xsl:text>
			<xsl:if test="string-length($month) &lt;  2">
				<xsl:text>0</xsl:text>
			</xsl:if>
			<xsl:value-of select="$month"/>
			<xsl:text>-</xsl:text>
			<xsl:if test="string-length($day) &lt;  2">
				<xsl:text>0</xsl:text>
			</xsl:if>
			<xsl:value-of select="$day"/>
		</xsl:variable>
		
		<xsl:variable name="date">
			<xsl:value-of select="xs:date($date-string)"/>
		</xsl:variable>
		
		<xsl:value-of select="format-date($date, '[MNn] [D], [Y]')"/>
	</xsl:template>
	
	<xsl:template match="head" priority="1.1">
		<ctmpl:module name="headline" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<h1 class="headline">
				<xsl:choose>
					<xsl:when test="title = ''">
						&#160;
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="//newsname">
								<xsl:value-of select="//newsname"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="title"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>				
			</h1>
		</ctmpl:module>
		<ctmpl:module name="meta" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<xsl:apply-templates select="title" mode="format"/>
			<xsl:apply-templates select="meta" mode="format"/>
		</ctmpl:module>
		<ctmpl:module name="searchresults" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<xsl:apply-templates select="searchresults" mode="start"/>
		</ctmpl:module>
		<xsl:apply-templates select="." mode="include"/>
	</xsl:template>

</xsl:stylesheet>