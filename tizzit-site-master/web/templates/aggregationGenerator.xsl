<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="layout" select="'default'"/>
    <xsl:param name="content-template" select="'standard'"/>

    <xsl:template match="/">
        <xsl:apply-templates select="$aggregation/*"/>
    </xsl:template>

    <!-- uebernehmen der restlichen html-knoten dem $template -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>	
    </xsl:template>

</xsl:stylesheet>