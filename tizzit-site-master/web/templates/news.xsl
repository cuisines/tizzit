<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="language" select="'deutsch'"/>
	<xsl:param name="url" select="''"/>
	<xsl:param name="sort" select="0"/>
	<xsl:param name="newsNr" select="0"/>
	<xsl:param name="print" select="0"/>
	<xsl:param name="anchor" select="''"/>
	<xsl:param name="liveserver" select="''"/>
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

    <xsl:param name="order">
        <xsl:choose>
		    <xsl:when test="//source/all/content/news/sortOrder/value!=''">
			    <xsl:value-of select="//source/all/content/news/sortOrder/value"/>
    		</xsl:when>
	    	<xsl:otherwise>descending</xsl:otherwise>
    	</xsl:choose>
	</xsl:param>

	<xsl:param name="oppOrder">
		<xsl:choose>
			<xsl:when test="$order='ascending'">descending</xsl:when>
			<xsl:otherwise>ascending</xsl:otherwise>
		</xsl:choose>
	</xsl:param>

	<xsl:template match="content" mode="format" priority="1.1">
	    <xsl:choose>
	        <xsl:when test="$newsNr != 0">
	            <xsl:apply-templates select="news" mode="newsDetail" />
	        </xsl:when>
	        <xsl:otherwise>
	            <xsl:apply-templates select="news" mode="newsList" />
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>

	<!--NEWS OVERVIEW: Anfang templates fuer news-->
	<xsl:template match="news" mode="newsList">
		<xsl:choose>
			<xsl:when test="//news/newslist//item/newsname!=''">
				<table cellspacing="0" cellpadding="6" border="0" style="border-collapse:collapse;">
					<tr>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href">page.html?sortBy=title&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Titel</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href">page.html?sortBy=date&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Datum</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="150">
							<a>
								<xsl:attribute name="href">page.html?sortBy=location&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Ort</span>
							</a>
						</th>
					</tr>
					<xsl:apply-templates select="." mode="sort"/>
				</table>
			</xsl:when>
			<xsl:otherwise>Zurzeit liegen leider keine aktuellen Nachrichten und Termine vor. <br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="news" mode="sort">
		<xsl:choose>

			<xsl:when test="$sortBy='date'">
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="ascending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:when test="$order='descending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsdate/year" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/month" data-type="number" order="descending"/>
							<xsl:sort select="newsdate/day" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/year" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/month" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="descending"/>
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
							<xsl:sort select="newslocality" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- JOBOVERVIEW-->
	<xsl:template match="item" mode="format2">
		<tr>
			<td style="border: 1px solid #888888" valign="top">
				<a>
					<xsl:attribute name="href">page.html?newsNr=<xsl:value-of select="@timestamp"/></xsl:attribute>
					<xsl:apply-templates select="newsname" mode="format"/>
				</a>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:if test="newsdate!=''">
					<xsl:apply-templates select="newsdate" mode="format"/>
					<xsl:apply-templates select="newsEndDate" mode="format"/>
				</xsl:if>
				<xsl:if test="newsdate-info!=''">
				    <xsl:if test="normalize-space(newsdate/year)!='' or normalize-space(newsEndDate/year)!=''">
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
	<!--===========================Anfang Templates fÃÂ?ÃÂ?ÃÂ?ÃÂ?ÃÂ?ÃÂ?ÃÂ?ÃÂÃÂ¼r deploystart und stop========-->
	<!--Hier werden auf dem Live Server die News ausgesucht, deren online start Zeit gegeben ist. In Integration werden alle dargestellt
-->
	<xsl:template match="item" mode="format">
		<xsl:if test="newsname!=''">
			<xsl:choose>
			<!--if it's the Live Server-->
				<xsl:when test="$liveserver='true'">
					<xsl:choose>
					<!--if there's no start date-->
					<xsl:when test="deploy-start-stop/start/year='' or not(deploy-start-stop)">
					<!--<xsl:when test="not(deploy-start-stop)">-->
						<xsl:apply-templates select="." mode="checkStopTime"/>
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
										<xsl:when test="$day &gt;= deploy-start-stop/start/day"><xsl:apply-templates select="." mode="checkStopTime"/>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$month &gt; deploy-start-stop/start/month"><xsl:apply-templates select="." mode="checkStopTime"/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$year &gt; deploy-start-stop/start/year"><xsl:apply-templates select="." mode="checkStopTime"/>
						</xsl:when>
					</xsl:choose>
					</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!--	If it's Integration Area show all the items-->
				<xsl:otherwise><xsl:apply-templates select="." mode="format2"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if></xsl:template>
	<!--Hier werden auf dem Live Server die news ausgesucht, deren online stop Zeit noch nicht gekommen ist. In Integration werden alle dargestellt
-->
	<xsl:template match="item" mode="checkStopTime">
		<!--If there's no stop date-->
		<xsl:choose>
			<xsl:when test="deploy-start-stop/stop/year='' or not(deploy-start-stop)">
				<xsl:apply-templates select="." mode="format2"/>
			</xsl:when>
			<xsl:otherwise>
				<!--	Test the Year -->
				<xsl:choose>
					<xsl:when test="$year = deploy-start-stop/stop/year">
						<!--Test the month-->
						<xsl:choose>
							<xsl:when test="$month = deploy-start-stop/stop/month">
								<!--Test the day-->
								<xsl:choose>
									<xsl:when test="$day &lt;= deploy-start-stop/stop/day">
										<xsl:apply-templates select="." mode="format2"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="$month &lt; deploy-start-stop/stop/month">
								<xsl:apply-templates select="." mode="format2"/>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$year &lt; deploy-start-stop/stop/year">
						<xsl:apply-templates select="." mode="format2"/>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =================== Anfang templates fuer singleNews ========================-->
	<xsl:template match="stickyPad" mode="newsDetail">
		<xsl:apply-templates select="newslist" mode="singleNews"/>
	</xsl:template>
	<!-- Anfang templates fuer singleNews-->
	<xsl:template match="news" mode="newsDetail">
		<xsl:apply-templates select="newslist" mode="newsDetail"/>
	</xsl:template>
	<!-- Das job mit der richtigen Nummer finden-->
	<xsl:template match="newslist" mode="newsDetail">
		<xsl:apply-templates select="item[@timestamp = $newsNr]" mode="newsDetail"/>
	</xsl:template>
	<!-- Die Tabelle darstellen mit den newsinformationen-->
	<xsl:template match="item" mode="newsDetail">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top">Titel:</td>
				<td width="19">
				</td>
				<td valign="top">
					<b>
						<xsl:apply-templates select="newsname"/>
					</b>
					<br/>
					<br/>
				</td>
			</tr>

			<xsl:if test="newsdate!=''">
				<tr>
					<td valign="top">Datum:</td>
					<td width="19">
					</td>
					<td valign="top">
						<xsl:apply-templates select="newsdate" mode="format"/><xsl:apply-templates select="newsEndDate" mode="format"/><br/>
						<br/>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="newsdate-info!=''">
				<tr>
					<td valign="top">Zeitpunkt:</td>
					<td width="19">
					</td>
					<td valign="top">
						<xsl:apply-templates select="newsdate-info"/>
						<br/>
						<br/>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="newslocality!=''">
				<tr>
					<td valign="top">Ort:</td>
					<td width="19">
					</td>
					<td valign="top">
						<xsl:apply-templates select="newslocality"/>
						<br/>
						<br/>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="../../unitInformation/@unitName!='root'">
			<tr>
				<td valign="top">Einrichtung:</td>
				<td width="19">
				</td>
				<td valign="top">
					<b>
						<xsl:apply-templates select="../../unitInformation/@unitName"/>
					</b>
					<br/>
					<br/>
				</td>
			</tr>
			</xsl:if>
		</table>
		<br/>
		<xsl:apply-templates select="text" mode="format"/>
		<br/>
		<xsl:call-template name="line"/>
		<br/>
		<a>
			<xsl:attribute name="href">page.html?sort=<xsl:value-of select="$sort"/>&amp;newsNr=0</xsl:attribute>zur&#252;ck</a>
	</xsl:template>
	<!-- ================================ END Templates fÃÂ?ÃÂÃÂ¼r singleNews====================-->
	<!-- Formatierungsobjekte an beliebiger Stelle einfÃÂ?ÃÂÃÂ¼gen-->
	<!--
	<xsl:template mode="format">
		<xsl:apply-imports/>
	</xsl:template>
	-->

    <xsl:template match="newsname" mode="format">
    	<xsl:apply-templates mode="format"/>
    </xsl:template>

    <xsl:template match="newsdate" mode="format">
    	<xsl:apply-templates mode="format"/>
    </xsl:template>

    <xsl:template match="newsEndDate" mode="format">
    	<xsl:if test="normalize-space(year) != ''">
        	<xsl:text>-&#160;</xsl:text><xsl:apply-templates mode="format"/>
    	</xsl:if>
    </xsl:template>

</xsl:stylesheet>