<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="news_new.xsl"/>

	<!-- Aktuelles Datum -->
	<xsl:variable name="currentTime">
		<xsl:value-of select="format-date(current-date(), '[Y][M01][D01]')"/>
	</xsl:variable>

	<!-- Ausgewählte Daten aus dem CMS -->
	<xsl:variable name="viewType">
		<xsl:choose>
			<xsl:when test="//source/all/content/newsArchive/value = '1'">live</xsl:when>
			<xsl:when test="//source/all/content/newsArchive/value = '2'">archiv</xsl:when>
			<xsl:otherwise>all</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="viewIdInternalLink">
		<xsl:value-of select="//source/all/content/internalLink/internalLink/@viewid"/>
	</xsl:variable>

	<xsl:variable name="urlInternalLink">
		<xsl:value-of select="//source/all/content/internalLink/internalLink/@url"/>
	</xsl:variable>

	<xsl:variable name="doc"
		select="document(concat('cocoon:/content-',$viewIdInternalLink,'.xml'))"/>

	<xsl:template match="content" mode="format" priority="2">
		<xsl:choose>
			<xsl:when test="$newsNr != '0'">
				<xsl:apply-templates select="$doc//news" mode="newsDetail"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="preTxt!=''">
					<div class="preText">
						<xsl:apply-templates select="preTxt" mode="format"/>
					</div>
				</xsl:if>
				<div class="newsContainer">
					<xsl:apply-templates select="$doc//news" mode="newsList"/>
				</div>
				<xsl:if test="afterTxt">
					<div class="afterText">
						<xsl:apply-templates select="afterTxt" mode="format"/>
					</div>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- muss hier überschrieben werden, wegen der Variable doc -->
	<xsl:template match="news" mode="newsList" priority="1.2">
		<xsl:choose>
			<xsl:when test="$doc//news/newslist//item/newsname!=''">
				<xsl:apply-templates select="$doc//news" mode="sort"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Zurzeit liegen leider keine aktuellen Nachrichten vor.</xsl:text>
				<br/><br/>
				<xsl:text>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- muss hier überschrieben werden, wegen dem Parameter orderSecond -->
	<xsl:template match="news" mode="sort" priority="1">
		<xsl:choose>
			<xsl:when test="$sortBy='date'">
				<xsl:choose>
					<xsl:when test="$orderSecond='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/month" data-type="number"
								order="ascending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="ascending"
							/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:when test="$orderSecond='descending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/year" data-type="number"
								order="descending"/>
							<xsl:sort select="newsEndDate/month" data-type="number"
								order="descending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="descending"
							/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$sortBy='title'">
				<xsl:choose>
					<xsl:when test="$orderSecond='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/month" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/day" data-type="number"
								order="{$orderSecond}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/month" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/day" data-type="number"
								order="{$orderSecond}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$orderSecond='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newslocality" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/month" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/day" data-type="number"
								order="{$orderSecond}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newslocality" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/month" data-type="number"
								order="{$orderSecond}"/>
							<xsl:sort select="newsdate/day" data-type="number"
								order="{$orderSecond}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- muss hier überschrieben werden, wegen dem viewType	-->
	<xsl:template match="item" mode="format2" priority="1.2">

		<xsl:param name="itemPosition"/>
		<xsl:variable name="newsComparator">
			<xsl:choose>
				<xsl:when test="newsArchivDate/day != ''">
					<xsl:call-template name="getFormatDateFromString">
						<xsl:with-param name="date"
							select="concat(newsArchivDate/year,' ',newsArchivDate/month,' ',newsArchivDate/day)"/>
						<xsl:with-param name="inFormat" select="'yyyy M d'"/>
						<xsl:with-param name="outFormat" select="'yyyyMMdd'"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="getFormatDateFromString">
						<xsl:with-param name="date" select="concat(2099,' ',12,' ',01)"/>
						<xsl:with-param name="inFormat" select="'yyyy M d'"/>
						<xsl:with-param name="outFormat" select="'yyyyMMdd'"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$viewType = 'archiv'">
				<xsl:if test="$newsComparator &lt; $currentTime">
					<xsl:apply-templates select="." mode="date">
						<xsl:with-param name="itemPosition">
							<xsl:value-of select="$itemPosition"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:when>
			<xsl:when test="$viewType = 'live'">
				<xsl:if test="$newsComparator &gt; $currentTime">
					<xsl:apply-templates select="." mode="date">
						<xsl:with-param name="itemPosition">
							<xsl:value-of select="$itemPosition"/>
						</xsl:with-param>
					</xsl:apply-templates>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="date">
					<xsl:with-param name="itemPosition">
						<xsl:value-of select="$itemPosition"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="item" mode="date" priority="2">
		<xsl:param name="itemPosition"/>
		<div class="newsItem">
			<h2 class="newsHeadline">
				<a>
					<xsl:attribute name="href">
						<xsl:text>?newsNr=</xsl:text>
						<xsl:value-of select="@timestamp"/>
					</xsl:attribute>
					<xsl:value-of select="newsname"/>
				</a>
			</h2>
			<!--<span class="newsDate">
				<xsl:call-template name="formatDateForOverview">
					<xsl:with-param name="day" select="newsdate/day"/>
					<xsl:with-param name="month" select="newsdate/month"/>
					<xsl:with-param name="year" select="newsdate/year"/>
				</xsl:call-template>
			</span>-->
			<div class="clear">&#160;</div>
			<xsl:if test="text!=''">
				<div class="newsContent">
					<xsl:value-of select="substring(text, 0, 300)"/>
					<a class="moreLink">
						<xsl:attribute name="href">
							<xsl:text>?newsNr=</xsl:text>
							<xsl:value-of select="@timestamp"/>
						</xsl:attribute>
						<xsl:if test="string-length(text)&gt;300">
							<xsl:text> ...more</xsl:text>
						</xsl:if>
					</a>
				</div>
			</xsl:if>
		</div>
		<xsl:if test="$itemPosition mod 10 = 0">
			<a href="#">nach oben</a>
		</xsl:if>
	</xsl:template>

	<xsl:template name="formatDateForOverview">
		<xsl:param name="day"/>
		<xsl:param name="month"/>
		<xsl:param name="year"/>

		<xsl:variable name="date-string">
			<xsl:if test="string-length($day) &lt;  2">
				<xsl:text>0</xsl:text>
			</xsl:if>
			<xsl:value-of select="$day"/>
			<xsl:text>.</xsl:text>
			<xsl:if test="string-length($month) &lt;  2">
				<xsl:text>0</xsl:text>
			</xsl:if>
			<xsl:value-of select="$month"/>
			<xsl:text>.</xsl:text>
			<xsl:value-of select="$year"/>
		</xsl:variable>
		<xsl:value-of select="$date-string"/>
	</xsl:template>

	<xsl:template name="newsAnchor">
		<xsl:value-of select="position()"/>
	</xsl:template>
	
	<xsl:template match="afterTxt | preTxt" mode="format">
		<xsl:apply-templates mode="format"/>
	</xsl:template>

</xsl:stylesheet>