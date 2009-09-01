<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="requestQuery" select="''"/>
	<xsl:param name="printQuery">
	<xsl:choose>
	    <xsl:when test="$requestQuery=''">?print=1</xsl:when>
	    <xsl:otherwise><xsl:value-of select="$requestQuery"/>&amp;print=1</xsl:otherwise>
	</xsl:choose>
	</xsl:param>

	<xsl:template match="footer">
	    <div class="footer">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<xsl:apply-templates select="printView"/>
				<xsl:apply-templates select="favorite"/>
				<xsl:apply-templates select="mail-to-a-friend"/>
				<xsl:apply-templates select="pdf"/>
				<xsl:apply-templates select="zoom"/>
			</tr>
		</table>
		</div>
	</xsl:template>

	<xsl:template match="pdf">
		<td width="11">
			<a href="/{$language}/{$url}/content.pdf{$requestQuery}" target="_blank">
				<img src="/cms/img/pdf-icon.gif" border="0"/>
			</a>
		</td>
		<td align="left">&#160;&#160;<a href="/{$language}/{$url}/content.pdf{$requestQuery}" target="_blank">als&#160;PDF</a></td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="mail-to-a-friend">
		<td width="11">
			<a href="javascript:mailTo();">
				<img src="/cms/img/mailto-icon.gif" border="0"/>
			</a>
		</td>
		<td align="left">&#160;&#160;<a href="javascript:mailTo();">
				<xsl:text disable-output-escaping="no">als&#160;Mail</xsl:text>
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="zoom">
		<td width="11">
			<a href="javascript:zoom();">
				<img src="/cms/img/skalieren-icon.gif" border="0"/>
			</a>
		</td>
		<td align="left">&#160;&#160;<a href="javascript:zoom();">Zoom&#160;in/out</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="favorite">
		<td width="11">
			<a><xsl:attribute name="href">javascript:favorite();</xsl:attribute>
				<img src="/cms/img/favorit-icon.gif" border="0"/>
			</a>
		</td>
		<td align="left">&#160;&#160;<a><xsl:attribute name="href">javascript:favorite();</xsl:attribute>zu&#160;Favoriten</a></td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="printView">
		<td width="11">
			<a href="/{$language}/{$url}/content.html{$printQuery}" target="_blank">			    
				<img src="/cms/img/druckansicht-icon.gif" border="0"/>
			</a>
		</td>
		<td align="left">&#160;&#160;<a href="/{$language}/{$url}/content.html{$printQuery}" target="_blank">Druckansicht</a></td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>
</xsl:stylesheet>
