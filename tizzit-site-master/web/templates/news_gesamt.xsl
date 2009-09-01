<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
    
	<xsl:include href="news.xsl"/>

	<xsl:template match="content" mode="format" priority="1.2">
	    <xsl:choose>
	        <xsl:when test="$newsNr != 0">
	            <xsl:apply-templates select="fulltextsearch/news" mode="newsDetail" />
	        </xsl:when>
	        <xsl:otherwise>
   	            <xsl:apply-templates select="fulltextsearch" mode="newsList" />
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>
	
	<xsl:template match="fulltextsearch" mode="newsList" priority="1.1">	
		<xsl:choose>
			<xsl:when test="news/newslist//item/newsname!=''">
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
					<xsl:call-template name="news"/>
				</table>
			</xsl:when>
			<xsl:otherwise>Zurzeit liegen leider keine aktuellen Nachrichten und Termine vor. <br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="news">
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
	
</xsl:stylesheet> 