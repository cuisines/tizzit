<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--includes the master.xsl that combines the mastertemplate with the generated modules-->
    <xsl:include href="../../../../tizzit-site-master/web/templates/master.xsl"/>
    
    <xsl:param name="layout" select="'default'"/>
    <xsl:param name="content-template" select="'standard'"/>
    
    <!-- Master nach layout und template auswaehlen -->
    <xsl:variable name="masterlayout">
        <xsl:choose>
            <!-- layout-->
            <xsl:when test="$layout='popup'">popup</xsl:when>
            <xsl:when test="$layout='print'">print</xsl:when>
            <!-- template -->    
            <!--<xsl:when test="$content-template='standard-without-teaser'">default-without-teaser</xsl:when>-->
            <xsl:when test="$content-template='home'">home</xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$layout"/>                
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:variable>
    
    <!--reads the mastertemplate into the variable template-->
    <xsl:variable name="template" select="document(concat('master-',$masterlayout,'.xml'))"/>
    
</xsl:stylesheet>