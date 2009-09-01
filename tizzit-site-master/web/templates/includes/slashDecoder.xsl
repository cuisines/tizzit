<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
Beispiel:
		<xsl:call-template name="decode">
			<xsl:with-param name="encoded" select="$url"/>
		</xsl:call-template>
-->



	<xsl:template name="decode">
		<xsl:param name="encoded"/>
		<xsl:choose>
			<xsl:when test="contains($encoded,'%2F')">
				<xsl:value-of select="substring-before($encoded,'%2F')"/>/<xsl:call-template name="decode"><xsl:with-param name="encoded" select="substring-after($encoded,'%2F')"/></xsl:call-template>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="$encoded"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<xsl:template name="decplus">
		<xsl:param name="encoded"/>
		<xsl:choose>
			<xsl:when test="contains($encoded,'+')">
				<xsl:value-of select="substring-before($encoded,'+')"/>%20<xsl:call-template name="decplus"><xsl:with-param name="encoded" select="substring-after($encoded,' ')"/></xsl:call-template>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="$encoded"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!--
	<xsl:template name="decplus">
		<xsl:param name="encoded"/>
		<xsl:choose>
			<xsl:when test="contains($encoded,' ')">
				<xsl:value-of select="substring-before($encoded,' ')"/>+<xsl:call-template name="decplus"><xsl:with-param name="encoded" select="substring-after($encoded,' ')"/></xsl:call-template>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="$encoded"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
 -->
 
<xsl:template name="dec">
	<xsl:param name="encoded"/>
		<xsl:call-template name="decode">
			<xsl:with-param name="encoded">
					<xsl:call-template name="decplus">
						<xsl:with-param name="encoded" select="$url"/>
					</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>	
</xsl:template>

</xsl:stylesheet>

