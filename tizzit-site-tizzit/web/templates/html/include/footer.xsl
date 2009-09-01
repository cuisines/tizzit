<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="../../../../../tizzit-site-master/web/templates/includes/footer3.xsl"/>
	<xsl:template match="footer" mode="footer" priority="0.5">
						<xsl:apply-templates select="printView"/>
						<!--<xsl:apply-templates select="pdf"/>-->
						<xsl:apply-templates select="mail-to-a-friend"/>
						<xsl:apply-templates select="favorite"/>			
	</xsl:template>
	<xsl:template match="pdf" priority="1.2">
		<div class="footer-img">
			<a href="content.pdf" target="_blank">
				<img src="/httpd/img/icon_pdf.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="content.pdf" target="_blank">
				<xsl:text>PDF</xsl:text>
			</a>
		</div>
	</xsl:template>
	<xsl:template match="mail-to-a-friend" priority="1.2">
		<div class="footer-img">
			<a href="javascript:mailTo();">
				<img src="/httpd/img/icon_mail.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="javascript:mailTo();">
				<xsl:text disable-output-escaping="no">Mailen</xsl:text>
			</a>
		</div>
	</xsl:template>
	<xsl:template match="favorite" priority="1.4">
		<xsl:choose>
			<xsl:when test="contains($userAgentString,'Mac')">
				<div class="footer-link">[Apfel-D] Bookmark</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'ie'">
				<div class="footer-link">
					<a>
						<xsl:attribute name="href">javascript:favorite();</xsl:attribute>
						<xsl:text>Bookmark</xsl:text>
					</a>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'mozilla'">
				<div class="footer-link">
					<xsl:text>[Ctrl-D] Bookmark</xsl:text>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'opera'">
				<div class="footer-link">
					<xsl:text>[Ctrl-T] Bookmark</xsl:text>
				</div>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
