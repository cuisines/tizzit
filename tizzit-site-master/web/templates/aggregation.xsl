<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							  xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="variables.xsl"/>

 
	<xsl:template match="@*|node()" priority="-2">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="cssresource">
		<modules>
			<ctmpl:module name="css">
				<xsl:apply-templates select="static-link" mode="cssresource"/>
				<xsl:apply-templates select="dynamic-link" mode="cssresource"/>
			</ctmpl:module>
		</modules>
	</xsl:template>

	<xsl:template match="contentresource">
		<xsl:apply-templates mode="contentresource"/>
	</xsl:template>

	<xsl:template match="navresource">
		<xsl:apply-templates mode="navresource"/>
	</xsl:template>

	<xsl:template match="navresourcetemplate">
		<xsl:apply-templates mode="navresourcetemplate"/>
	</xsl:template>
	
	<xsl:template match="navresourcelocaltemplate">						 
		<xsl:apply-templates mode="navresourcelocaltemplate"/>
	</xsl:template>

	<xsl:template match="navresourcelocal">
		<xsl:apply-templates mode="navresourcelocal"/>
	</xsl:template>

	<xsl:template match="contentModule">
		<xsl:apply-templates mode="contentModule"/>
	</xsl:template>

	<xsl:template match="cached-include" mode="contentresource">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>contentresource-<xsl:value-of select="$viewComponentId"/>-<xsl:value-of select="$template"/>-<xsl:value-of select="$currentDate"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresource">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresource-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresourcetemplate">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcetemplate-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
	<xsl:template match="cached-include" mode="navresourcelocaltemplate">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcelocaltemplate-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresourcelocal">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcelocal-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

<!--Der clientCode wird angehaengt, um ungewuenschtes Caching ueber Mandantengrenzen hinweg zu vermeiden (z.B. bei /teaseraggregation/-->
	<xsl:template match="cached-include" mode="contentModule">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0" ignoreErrors="true">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>
				<xsl:value-of select="$language"/>/<xsl:value-of select="@src"/>?<xsl:value-of select="$clientCode"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
	<xsl:template match="static-link" mode="cssresource">
		<link type="text/css" rel="stylesheet" href="{@src}"/>
	</xsl:template>

	<!-- <dynamic-link type="template" prefix="/css/" /> -->
	<xsl:template match="dynamic-link" mode="cssresource">
		<xsl:choose>
			<xsl:when test="@type = 'template'">
				<xsl:if test="count(except[@type='template' and @value=$template]) = 0">
					<xsl:choose>
						<xsl:when test="@media = 'print'">
							<link type="text/css" rel="stylesheet" href="{@prefix}{$template}.css?media=print"/>
						</xsl:when>
						<xsl:otherwise>
							<link type="text/css" rel="stylesheet" href="{@prefix}{$template}.css"/>							
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>				
			</xsl:when>
			<xsl:otherwise>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>

