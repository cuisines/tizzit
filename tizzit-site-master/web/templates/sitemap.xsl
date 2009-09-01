<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Dieser Parameter muss im includierenden stylesheet gesetzt werden:
	<xsl:param name="rootLinkName" select="'Ginsengportal'"/>
 -->
	<xsl:template match="content" mode="format" priority="1.1">
		<xsl:apply-templates select="sitemap"/>
	</xsl:template>

	<xsl:template match="sitemap">
		<!--Reloader iframe-->
		<iframe width="0" height="0" name="reloader">
		</iframe>
		<xsl:apply-templates mode="sitemap"/>
	</xsl:template>

	<xsl:template match="navigation" mode="sitemap">
		<xsl:apply-templates select="viewcomponent" mode="root"/>
	</xsl:template>

	<xsl:template match="viewcomponent" mode="root">
		<div class="layer">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<img src="../../httpd/img/sitemap_start.gif"/>
					</td>
					<td>
						<img src="../../httpd/img/spacer.gif" width="10" border="0"/>
					</td>
					<td valign="top">
						<!--	wenn root Knoten = Gesamtroot dann link auf Home, wenn nicht dann root auf Unit Startseite-->
						<xsl:choose>
							<xsl:when test="linkName='root'">
								<a href="/"><xsl:value-of select="$rootLinkName"/></a>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="." mode="menuelinks"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</table>
			<xsl:apply-templates select="viewcomponent" mode="level1"/>
		</div>
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
									<img src="../../httpd/img/sitemap_plus.gif" border="0">
										<xsl:attribute name="name">image_<xsl:value-of select="@id"/></xsl:attribute>
									</img>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<img src="../../httpd/img/sitemap_page.gif"/>
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