<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ctmpl="http://www.conquest-cms.net/template"
                xmlns:java="http://xml.apache.org/xslt/java"
                exclude-result-prefixes="java ctmpl">
	
	<!--reads the generated modules into the variable page-->
	<xsl:variable name="page" select="/page"/>

	<xsl:template match="/">
		<xsl:apply-templates select="$template/html" mode="layout"/>
	</xsl:template>

	<!-- include sections -->
	<xsl:template match="ctmpl:include" priority="1" mode="layout">
		<xsl:apply-templates select="$page/modules/ctmpl:module[@name = current()/@name]" mode="layout"/>
	</xsl:template>

    <!-- match ctmpl:foo="bar" attributes, replace them by bar="xxx" -->
    <xsl:template match="@ctmpl:*" priority="1" mode="layout">
        <xsl:attribute name="{.}">
            <xsl:variable name="localName" select="local-name()"/>
            <xsl:value-of select="normalize-space($page/modules/ctmpl:attribute[@name = $localName]/@value)"/>
        </xsl:attribute>
    </xsl:template>
	
	<!--herausfiltern des "value" Elements aus der Seite -->
	<xsl:template match="ctmpl:value" priority="1" mode="layout" >
        <xsl:choose>
            <xsl:when test="@type='date'">
                <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('yyyy'), java:java.util.Date.new())"/>
            </xsl:when>
        </xsl:choose>
	</xsl:template>

	<!-- uebernehmen der restlichen html-knoten dem $template -->
	<xsl:template match="@*|node()" priority="-2" mode="layout">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" mode="layout"/>
		</xsl:copy>	
	</xsl:template>

	<!--herausfiltern des "module" Elements aus der Seite -->
	<xsl:template match="ctmpl:module" priority="-1" mode="layout" >
			<xsl:copy-of select="child::*" />
	</xsl:template>

</xsl:stylesheet>