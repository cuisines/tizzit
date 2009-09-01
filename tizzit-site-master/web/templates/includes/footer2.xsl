<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:template match="footer" mode="footer">
				<xsl:apply-templates select="printView"/>
				<xsl:apply-templates select="favorite"/>
				<xsl:apply-templates select="mail-to-a-friend"/>
				<xsl:apply-templates select="pdf"/>
				<xsl:apply-templates select="zoom"/>
	</xsl:template>

	<xsl:template match="pdf">
		<div class="pdf">
			<a href="javascript:pdf();">als&#160;PDF</a>
		</div>
	</xsl:template>

	<xsl:template match="mail-to-a-friend">
		<div class="mail-to-a-friend">
			<a href="javascript:mailTo();">
				<xsl:text disable-output-escaping="no">als&#160;Mail</xsl:text>
			</a>
		</div>
	</xsl:template>

	<xsl:template match="zoom">
		<div class="zoom">
			<a href="javascript:zoom();">Zoom&#160;in/out</a>
		</div>
	</xsl:template>

	<xsl:template match="favorite">
		<div class="favorite">
			<a>
				<xsl:attribute name="href">javascript:favorite();</xsl:attribute>zu&#160;Favoriten</a>
		</div>
	</xsl:template>

	<xsl:template match="printView">
		<div class="printView">
			<a href="javascript:printView();">Druckansicht</a>
		</div>
	</xsl:template>
</xsl:stylesheet>