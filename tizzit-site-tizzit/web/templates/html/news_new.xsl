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

	<xsl:template match="news" mode="newsList">
		<xsl:choose>
			<xsl:when test="//news/newslist//item/newsname!=''">
				<table cellspacing="0" cellpadding="6" border="0" style="border-collapse:collapse;">
					<tr>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href"
										>page.html?sortBy=title&amp;order=<xsl:value-of
										select="$oppOrder"/></xsl:attribute>
								<span>Titel</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href"
										>page.html?sortBy=date&amp;order=<xsl:value-of
										select="$oppOrder"/></xsl:attribute>
								<span>Datum</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href"
										>page.html?sortBy=location&amp;order=<xsl:value-of
										select="$oppOrder"/></xsl:attribute>
								<span>Ort</span>
							</a>
						</th>
					</tr>
					<xsl:apply-templates select="." mode="sort"/>
				</table>
			</xsl:when>
			<xsl:otherwise>Zurzeit liegen leider keine aktuellen Nachrichten und Termine vor.
				<br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt
				wieder.</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="item" mode="date" priority="1.1">
		<tr>
			<td valign="top" class="list-detail">
				<a>
					<xsl:attribute name="href">page.html?newsNr=<xsl:value-of select="@timestamp"
						/></xsl:attribute>
					<xsl:apply-templates select="newsname" mode="format"/>
				</a>
			</td>
			<td valign="top" class="list-detail">
				<xsl:if test="newsdate!=''">
					<xsl:if test="newsdate-info=''">
						<xsl:apply-templates select="newsdate" mode="format"/>
						<xsl:apply-templates select="newsEndDate" mode="format"/>
					</xsl:if>
				</xsl:if>
				<xsl:if test="newsdate-info!=''">
					<xsl:apply-templates select="newsdate-info"/>
				</xsl:if>
			</td>
			<td valign="top" class="list-detail">
				<xsl:apply-templates select="newslocality"/>
			</td>
		</tr>
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
	<xsl:template match="stickyPad" mode="newsDetail" priority="1.1"/>

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
		<xsl:apply-templates select="newslocality" mode="format"/>
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

	<xsl:template match="item" mode="format2" priority="1.1">
		<tr>
			<td style="border: 1px solid #888888" valign="top">
				<a>
					<xsl:attribute name="href">page.html?newsNr=<xsl:value-of select="@timestamp"
						/></xsl:attribute>
					<xsl:apply-templates select="newsname" mode="format"/>
				</a>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:if test="newsdate!=''">
					<xsl:apply-templates select="newsdate" mode="format"/>
					<xsl:apply-templates select="newsEndDate" mode="format"/>
				</xsl:if>
				<xsl:if test="newsdate-info!=''">
					<xsl:if
						test="normalize-space(newsdate/year)!='' or normalize-space(newsEndDate/year)!=''">
						<br/>
					</xsl:if>
					<xsl:apply-templates select="newsdate-info"/>
				</xsl:if>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:apply-templates select="newslocality"/>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="newsdate" mode="format">
		<xsl:param name="day" select="day"/>
		<xsl:param name="month" select="month"/>
		<xsl:param name="year" select="year"/>

		<xsl:text>Veröffentlicht am </xsl:text>
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
		<xsl:value-of select="format-date($date, '[D]. [MNn] [Y]')"/>
		<br/>
	</xsl:template>

	<xsl:template match="newslocality" mode="format" priority="1.1">
		<span id="news_location">
			<xsl:apply-templates mode="format"/>
		</span>
		<br/>
		<br/>
		<a href="javascript:history.back()">
			<img src="/httpd/img/but_zurueck_lo.gif" alt="back"
				onmouseover="this.src = '/httpd/img/but_zurueck_hi.gif'"
				onmouseout="this.src = '/httpd/img/but_zurueck_lo.gif'"/>
		</a>
	</xsl:template>

</xsl:stylesheet>
