<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:include href="common.xsl"/>
	
	<xsl:template match="content" mode="include" priority="1.1">
		<xsl:apply-templates select="sitemap" mode="sitemap"/>
	</xsl:template>
	
	<xsl:template match="navigation" mode="sitemap">
		<xsl:apply-templates select="viewcomponent" mode="root"/>
	</xsl:template>
	 
	<xsl:template match="viewcomponent" mode="root">
		
		<div class="layer">
			<div class="layerRoot">
				<img src="/httpd/img/sitemap/sitemap_start.gif" alt="Sitemap start"/>
				<!-- root Knoten = Gesamtroot dann link auf Home, wenn nicht dann root auf Unit Startseite-->
				<xsl:choose>
					<xsl:when test="linkName='root'">
					    <a class="linkSitemap" href="/">Home</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="." mode="menuelinks"/>
					</xsl:otherwise>
				</xsl:choose>
			</div>
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
			<xsl:choose>
				<xsl:when test="@hasChild='true' and not(contains(statusInfo,'nosubmenu'))">
					<a>
						<xsl:attribute name="href">javascript:showNoShow('<xsl:value-of select="@id"
						/>','<xsl:value-of select="$language"/>')</xsl:attribute>
						<img src="/httpd/img/sitemap/sitemap_plus.gif" alt="Unterpunkte" border="0">
							<xsl:attribute name="name">image_<xsl:value-of select="@id"
							/></xsl:attribute>
						</img>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<img src="/httpd/img/sitemap/sitemap_page.gif" alt="keine Unterpunkte"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="." mode="menuelinks"/>
			<div>
				<xsl:attribute name="style">display:<xsl:choose>
					<xsl:when test="count(ancestor::viewcomponent)&gt;0">none</xsl:when>
					<xsl:otherwise>block</xsl:otherwise>
				</xsl:choose></xsl:attribute>
				<xsl:attribute name="id">sitemap<xsl:value-of select="@id"/></xsl:attribute>
				&#160; </div>
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
	</xsl:template>
	
	<!-- Leerausgabe -->
	<xsl:template match="sitemap" mode="format" priority="1.1"/>
	<xsl:template match="unitInformation" mode="format" priority="1.1"/>
	
</xsl:stylesheet>