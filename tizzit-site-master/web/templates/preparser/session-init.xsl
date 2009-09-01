<?xml version='1.0'?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:session="http://apache.org/cocoon/session/1.0"
    xmlns:web20="http://web20.conquest-cms.net/1.0">

    <xsl:template match="head">
        <head>
            <session:createcontext name="lms"/>
            <session:getxml context="lms" path="/"/>
            <xsl:apply-templates/>
        </head>
    </xsl:template>

    <!-- uebernehmen der restlichen html-knoten dem $template -->
    <xsl:template match="@*|node()">
        <xsl:copy copy-namespaces="yes">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>	
    </xsl:template>

</xsl:stylesheet>