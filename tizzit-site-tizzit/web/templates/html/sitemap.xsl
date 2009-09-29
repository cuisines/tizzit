<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:include href="common.xsl"/>
	
	<xsl:template match="content" mode="format" priority="1">
		<xsl:apply-templates select="../sitemap"/>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="sitemap">
		<xsl:apply-templates select="navigation" mode="sitemap"/>
	</xsl:template>
	
	<xsl:template match="navigation" mode="sitemap">
		<xsl:apply-templates select="viewcomponent" mode="sitemap"/>
	</xsl:template>
	 
	<xsl:template match="viewcomponent" mode="sitemap">
		<div class="sitemap">
			<div class="sitemap_left">&#160;</div>
			<div class="root">Tizzit.org</div>
			<div class="s_links">
				<xsl:apply-templates select="viewcomponent" mode="treeView"/>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="viewcomponent" mode="treeView">
		<div class="s_firstlevel">
			<xsl:call-template name="writeLink">
				<xsl:with-param name="linkName" select="linkName"/>
				<xsl:with-param name="language" select="language"/>
				<xsl:with-param name="url" select="url"/>
			</xsl:call-template>
		</div>
		<xsl:choose>
			<xsl:when test="@hasChild='true'">
				<xsl:apply-templates select="viewcomponent" mode="treeViewSecond"/>
			</xsl:when>
			<xsl:otherwise><div class="clear">&#160;</div></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="viewcomponent" mode="treeViewSecond">
		<div class="s_secondlevel">
			<xsl:call-template name="writeLink">
				<xsl:with-param name="linkName" select="linkName"/>
				<xsl:with-param name="language" select="language"/>
				<xsl:with-param name="url" select="url"/>
			</xsl:call-template>
		</div>
		<xsl:choose>
			<xsl:when test="@hasChild='true'">
				<xsl:apply-templates select="viewcomponent" mode="treeViewThird"/>
			</xsl:when>
			<xsl:otherwise><div class="clear">&#160;</div></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="viewcomponent" mode="treeViewThird">
		<div class="s_secondlevel">
			<xsl:call-template name="writeLink">
				<xsl:with-param name="linkName" select="linkName"/>
				<xsl:with-param name="language" select="language"/>
				<xsl:with-param name="url" select="url"/>
			</xsl:call-template>
		</div>
	</xsl:template>
	
	<xsl:template name="writeLink">
		<xsl:param name="linkName"/>
		<xsl:param name="url"/>
		<xsl:param name="language"/>
		
		<a href="/{$language}/{url}/page.html"><xsl:value-of select="$linkName"/></a>
	</xsl:template>
	
</xsl:stylesheet>