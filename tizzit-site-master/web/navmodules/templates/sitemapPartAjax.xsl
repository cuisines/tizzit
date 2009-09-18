<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="language" select="'deutsch'"/>

	<xsl:template match="/" priority="1.6">
		<div>
			<xsl:apply-templates select="sitemapPart/navigation/viewcomponent" mode="reloader"/>
		</div>
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

			<xsl:choose>
				<xsl:when test="@hasChild='true'">
					<a>
						<xsl:attribute name="href">javascript:showNoShow('<xsl:value-of select="@id"
								/>','<xsl:value-of select="$language"/>')</xsl:attribute>
						<img src="/httpd/img/sitemap/sitemap_plus.gif" border="0" alt="Unterpunkte">
							<xsl:attribute name="name">image_<xsl:value-of select="@id"
							/></xsl:attribute>
						</img>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<img src="/httpd/img/sitemap_page.gif" alt="keine Unterpunkte"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:apply-templates select="." mode="menuelinks"/>

			<div>
				<xsl:attribute name="style">display:<xsl:choose>
						<xsl:when test="count(ancestor::viewcomponent)&gt;0">none</xsl:when>
						<xsl:otherwise>block</xsl:otherwise>
					</xsl:choose></xsl:attribute>
				<xsl:attribute name="id">sitemap<xsl:value-of select="@id"/></xsl:attribute>
				&#160;
			</div>
		</div>
	</xsl:template>

	<!-- link schreiben -->
	<xsl:template match="viewcomponent" mode="menuelinks">
		<a class="linkSitemap">
			<xsl:attribute name="href">/<xsl:value-of select="$language"/>/<xsl:value-of
				select="url"/>/page.html</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			
			<xsl:value-of select="linkName"/>
		</a>
		<br/>
	</xsl:template>
	
	<!-- Seiten mit bestimmten templates grundsaetzlich von der Darstellung ausschliessen -->
	<xsl:template match="viewcomponent[contains(template,'teaseraggregation') or template='searchresult']" mode="menuelinks" priority="1.2" />
	<xsl:template match="viewcomponent[contains(template,'teaseraggregation') or template='searchresult']" mode="reloader" priority="1.2" />
	<xsl:template match="viewcomponent[contains(template,'teaseraggregation') or template='searchresult']" mode="level1" priority="1.2" />


</xsl:stylesheet>
