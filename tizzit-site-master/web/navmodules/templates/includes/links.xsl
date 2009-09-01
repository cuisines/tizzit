<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- link schreiben -->
	<xsl:template match="viewcomponent" mode="links">
		<xsl:choose>
			<xsl:when test="extUrl">
				<xsl:choose>
					<xsl:when test="viewIndex='4'">
						<a target="_blank">
							<xsl:attribute name="href">/index_4.html?extUrl=<xsl:value-of select="extUrl"/></xsl:attribute>
							<xsl:value-of select="linkName"/>
						</a>
					</xsl:when>
					<xsl:when test="viewIndex='direct'">
						<a target="_blank">
							<xsl:attribute name="href"><xsl:value-of select="extUrl"/></xsl:attribute>
							<xsl:value-of select="linkName"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a target="_self">
							<xsl:attribute name="href">
								<xsl:value-of select="extUrl"/>
							</xsl:attribute>
							<xsl:value-of select="linkName"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- auskommentiert 9.5.05, Hato, Fuehrt zu Fehler in abz. Sinn nicht mehr nachvollziehbar. ggf. viewLevel=7 mit softdev klaeren -->
			<!--
			<xsl:when test="viewLevel='7'">
				<a target="_self">
					<xsl:attribute name="href">
						<xsl:value-of select="url"/>
					</xsl:attribute>
					<xsl:value-of select="linkName"/>
				</a>
			</xsl:when>
			-->			
			<xsl:when test="viewType='7'">
				<!-- Separator -->
				<div class="linkseparator">
					<xsl:value-of select="linkName"/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<a>
					<xsl:call-template name="href"/>
					<xsl:value-of select="linkName"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="href">
		<xsl:choose>
			<xsl:when test="extUrl">
				<xsl:attribute name="target">_blank</xsl:attribute>
				<xsl:attribute name="href">
					<xsl:value-of select="extUrl"/>
				</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="target">_self</xsl:attribute>
				<xsl:attribute name="href">/<xsl:value-of select="$language"/>/<xsl:value-of select="url"/>/page.html<xsl:if test="url/@anchor">#<xsl:value-of select="url/@anchor"/></xsl:if></xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
		
</xsl:stylesheet>