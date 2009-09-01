<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

    <xsl:include href="../aggregation.xsl"/>
	
	<!--
	<xsl:template match="cached-include" mode="navresource" priority="1.1">		
		<xsl:choose>
			<xsl:when test="(@src = 'teaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/teaser='false') or (@src = 'unitteaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/unitteaser='false')">
				<div><![CDATA[Die ]]><xsl:value-of select="@src"/><![CDATA[ wurde entfernt, weil es die Content xhtml gesagt hat]]></div>
			</xsl:when>
			<xsl:otherwise>
				<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
					<xsl:attribute name="src">
						<xsl:value-of select="$preStringCinclude"/>navresource-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
				</cinclude:cached-include>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	-->
	
	<xsl:template match="cached-include" mode="navresource" priority="1.1">
		
		<xsl:choose>
			<xsl:when test="(@src = 'teaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/teaser='false')  or (@src = 'unitteaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/unitteaser='false')">
				<![CDATA[<!-- Eine Aggregation wurde entfernt, weil es die Content Xhtml es gesagt hat -->	]]>				
			</xsl:when>
			<xsl:otherwise>				
				<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
					<xsl:attribute name="src">
						<xsl:value-of select="$preStringCinclude"/>navresource-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
				</cinclude:cached-include>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="cached-include" mode="navresourcelocal" priority="1.1">
		
		<xsl:choose>
			<xsl:when test="(@src = 'teaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/teaser='false')  or (@src = 'unitteaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/unitteaser='false')">
				<![CDATA[<!-- Eine Aggregation wurde entfernt, weil es die Content Xhtml es gesagt hat -->	]]>				
			</xsl:when>
			<xsl:otherwise>				
				<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
					<xsl:attribute name="src">
						<xsl:value-of select="$preStringCinclude"/>navresourcelocal-<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
				</cinclude:cached-include>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="cached-include" mode="contentresource" priority="1">
		<cinclude:cached-include xmlns:cinclude="http://apache.org/cocoon/include/1.0">
			<xsl:attribute name="src">
				<xsl:value-of select="$preStringCinclude"/>contentresource/<xsl:value-of select="$viewComponentId"/>/<xsl:value-of select="$template"/>/<xsl:value-of select="$currentDate"/>/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/<xsl:value-of select="@src"/><xsl:value-of select="$paramString"/></xsl:attribute>
		</cinclude:cached-include>
	</xsl:template>
	
</xsl:stylesheet>