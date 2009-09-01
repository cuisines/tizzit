<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="language" select="'deutsch'"/>
	<xsl:param name="url" select="''"/>
	<xsl:param name="print" select="0"/>
	<xsl:param name="anchor" select="''"/>
	<xsl:param name="liveserver" select="''"/>
	<xsl:param name="viewComponentId" select="''"/>
	
<xsl:template match="/" priority="1.2">
	<html>
		<head>
		</head>
		<body>
			<xsl:attribute name="onload">parent.changeContent(<xsl:value-of select="$viewComponentId"/>)</xsl:attribute>
			<div id="reloadLayer">
				<xsl:text disable-output-escaping="yes">&lt;!--</xsl:text>
					<xsl:apply-templates select="sitemapPart/navigation" mode="reloader"/>
				<xsl:text disable-output-escaping="yes">--&gt;</xsl:text>
			</div>
		</body>
	</html>		
</xsl:template>

<xsl:template match="navigation" mode="reloader">
	<xsl:apply-templates select="viewcomponent" mode="reloader"/>
</xsl:template>

<xsl:template match="viewcomponent" mode="reloader">
	<xsl:apply-templates select="viewcomponent" mode="level1"/>
</xsl:template>

	<xsl:template match="viewcomponent" mode="level1" name="sitemapRow">
		<div>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="count(following-sibling::viewcomponent)=0">layerSubLast</xsl:when>
					<xsl:otherwise>layerSub</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<xsl:choose>
							<xsl:when test="@hasChild='true'">
								<a>
									<xsl:attribute name="href">javascript:showNoShow('<xsl:value-of select="@id"/>','<xsl:value-of select="$language"/>')</xsl:attribute>
									<img src="/httpd/img/sitemap_plus.gif" border="0">
										<xsl:attribute name="name">image_<xsl:value-of select="@id"/></xsl:attribute>
									</img>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<img src="/httpd/img/sitemap_page.gif"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>&#160;</td>
					<td valign="top">
						<xsl:apply-templates select="." mode="menuelinks"/>
					</td>
				</tr>
			</table>
			<div>
				<xsl:attribute name="style">display:<xsl:choose><xsl:when test="count(ancestor::viewcomponent)&gt;0">none</xsl:when><xsl:otherwise>block</xsl:otherwise></xsl:choose></xsl:attribute>
				<xsl:attribute name="id">sitemap<xsl:value-of select="@id"/></xsl:attribute>
			</div>
		</div>
	</xsl:template>
	
	<!-- link schreiben -->
	<xsl:template match="viewcomponent" mode="menuelinks">
		<a>	<xsl:attribute name="href">/<xsl:value-of select="$language"/>/<xsl:value-of select="url"/>/page.html</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="linkName"/>
		</a>
		<br/>
	</xsl:template>
	

</xsl:stylesheet>