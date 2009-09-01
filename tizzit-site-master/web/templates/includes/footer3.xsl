<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template match="footer" mode="footer">
		<xsl:apply-templates select="printView"/>
		<xsl:apply-templates select="pdf"/>
		<xsl:apply-templates select="brochure"/>
		<xsl:apply-templates select="mail-to-a-friend"/>
		<br/><br/>
		<xsl:apply-templates select="favorite"/>
		<xsl:apply-templates select="zoom"/>
	</xsl:template>
	
	<xsl:template match="pdf" priority="1.1">
		<div class="footer-img">
			<a href="content.pdf" target="_blank">
				<img alt="footer-icon" src="/httpd/img/icon_pdf.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="content.pdf" target="_blank">
				<xsl:text>als&#160;PDF</xsl:text>
			</a>
		</div>
	</xsl:template>
	
	<xsl:template match="brochure" priority="1.1">
		<div class="footer-img">
			<a href="broschuere/content.pdf" target="_blank">
				<img alt="footer-icon" src="/httpd/img/icon_broschuere.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="Broschuere/content.pdf" target="_blank">
				<xsl:text>Broschüre</xsl:text>
			</a>
		</div>
	</xsl:template>
	
	<xsl:template match="mail-to-a-friend" priority="1.1">
		<div class="footer-img">
			<a href="javascript:mailTo();">
				<img alt="footer-icon" src="/httpd/img/icon_mail.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="javascript:mailTo();">
				<xsl:text disable-output-escaping="no">als&#160;Mail</xsl:text>
			</a>
		</div>
	</xsl:template>
	
	<xsl:template match="zoom" priority="1.1">
		<xsl:choose>
			<xsl:when test="$userAgent = 'ie'">
				<div class="footer-img">
					<a href="javascript:zoom();">
						<img alt="footer-icon" src="/httpd/img/icon_zoom.gif" border="0" class="footer-icon"/>
					</a>
				</div>
				<div class="footer-link">
					<a href="javascript:zoom();">
						<xsl:text>Zoom&#160;in/out</xsl:text>
					</a>
				</div>                
			</xsl:when>
			<xsl:when test="$userAgent = 'mozilla'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_zoom.gif" border="0" class="footer-icon"/>
				</div>
				<div class="footer-link">
					<xsl:text>[Ctrl +/-] Zoom in/out</xsl:text>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'opera'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_zoom.gif" border="0" class="footer-icon"/>
				</div>
				<div class="footer-link">
					<xsl:text>[+/-] Zoom in/out</xsl:text>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'safari'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_zoom.gif" border="0" class="footer-icon"/>
				</div>
				<div class="footer-link">
					[<img alt="footer-icon" src="/httpd/img/icon_apple.gif" border="0" style="vertical-align:bottom;"/>+/-] Zoom in/out
				</div>
			</xsl:when>			
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="favorite" priority="1.1">
		<xsl:choose>
			<xsl:when test="$userAgent = 'ie'">
				<div class="footer-img">
					<a><xsl:attribute name="href">javascript:favorite();</xsl:attribute>
						<img alt="footer-icon" src="/httpd/img/icon_bookmark.gif" border="0"  class="footer-icon"/>
					</a>
				</div>
				<div class="footer-link">
					<a>
						<xsl:attribute name="href">javascript:favorite();</xsl:attribute>
						<xsl:text>zu&#160;Favoriten</xsl:text>
					</a>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'mozilla'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_bookmark.gif" border="0"  class="footer-icon"/>
				</div>
				<div class="footer-link">
					<xsl:text>[Ctrl-D] Bookmark</xsl:text>
				</div>
			</xsl:when>
			<xsl:when test="$userAgent = 'opera'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_bookmark.gif" border="0"  class="footer-icon"/>
				</div>
				<div class="footer-link">
					<xsl:text>[Ctrl-T] Bookmark</xsl:text>
				</div>                
			</xsl:when>
			<xsl:when test="$userAgent = 'safari'">
				<div class="footer-img">
					<img alt="footer-icon" src="/httpd/img/icon_bookmark.gif" border="0"  class="footer-icon"/>
				</div>
				<div class="footer-link">
					[<img alt="footer-icon" src="/httpd/img/icon_apple.gif" border="0" style="vertical-align:bottom;"/>-D] Bookmark
				</div>				
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="printView" priority="1.1">
		<div class="footer-img">
			<a href="print.html" target="_blank">			    
				<img alt="footer-icon" src="/httpd/img/icon_druck.gif" border="0" class="footer-icon"/>
			</a>
		</div>
		<div class="footer-link">
			<a href="print.html" target="_blank">Druckansicht</a>
		</div>
	</xsl:template>
</xsl:stylesheet>
