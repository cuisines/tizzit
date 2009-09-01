<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							  xmlns:ctmpl="http://www.conquest-cms.net/template"
							  xmlns:cinclude="http://apache.org/cocoon/include/1.0">

	<xsl:include href="../variables.xsl"/>

	<xsl:template match="contentresource" priority="0.1">
		<xsl:apply-templates mode="contentresource"/>
	</xsl:template>

	<xsl:template match="navresource" priority="0.1">
		<xsl:apply-templates mode="navresource"/>
	</xsl:template>

	<xsl:template match="navresourcetemplate" priority="0.1">
		<xsl:apply-templates mode="navresourcetemplate"/>
	</xsl:template>
	
	<xsl:template match="navresourcelocaltemplate" priority="0.1">						 
		<xsl:apply-templates mode="navresourcelocaltemplate"/>
	</xsl:template>

	<xsl:template match="navresourcelocal" priority="0.1">
		<xsl:apply-templates mode="navresourcelocal"/>
	</xsl:template>

	<xsl:template match="contentModule" priority="0.1">
		<xsl:apply-templates mode="contentModule"/>
	</xsl:template>

	<xsl:template match="cached-include" mode="contentresource" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>contentresource/<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$currentDate"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresource" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresource-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresourcetemplate" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcetemplate-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
	<xsl:template match="cached-include" mode="navresourcelocaltemplate" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcelocaltemplate-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="navresourcelocal" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>navresourcelocal-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>

	<xsl:template match="cached-include" mode="contentModule" priority="0.1">
		<cinclude:cached-include>
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>
				<xsl:value-of select="$language"/>/<xsl:value-of select="@src"/>?<xsl:value-of select="$clientCode"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
	
	<xsl:template match="cssresource" priority="0.1">
		<modules>
			<ctmpl:module name="css">
				<xsl:apply-templates select="static-link" mode="cssresource"/>
				<xsl:apply-templates select="dynamic-link" mode="cssresource"/>
			</ctmpl:module>
		</modules>
	</xsl:template>
	
	<xsl:template match="static-link" mode="cssresource" priority="0.1">
		<link type="text/css" rel="stylesheet" href="{@prefix}{@src}{@type}.css"/>
	</xsl:template> 

	<xsl:template match="dynamic-link" mode="cssresource" priority="0.1">
		<xsl:if test="@type = 'template'">
			<xsl:choose>
				<xsl:when test="@media = 'print'">
					<link type="text/css" rel="stylesheet" href="{@prefix}common.css?media=print"/>
				</xsl:when>
				<xsl:otherwise>
					<link type="text/css" rel="stylesheet" href="{@prefix}{$template}.css"/>							
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="@*|node()" priority="-2">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>