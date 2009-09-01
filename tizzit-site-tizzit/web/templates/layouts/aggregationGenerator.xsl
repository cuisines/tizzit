<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--includes the master.xsl that combines the mastertemplate with the generated modules-->
    <xsl:include href="../../../../tizzit-site-master/web/templates/aggregationGenerator.xsl"/>

    <!-- Master nach layout und template auswaehlen -->
    <xsl:variable name="aggregationtype">
        <xsl:choose>
            <!-- layout-->
            <xsl:when test="$layout='popup'">popup</xsl:when>
            <xsl:when test="$layout='print'">print</xsl:when>
            <!-- template dependend, uncomment if needed -->
            <!--
            <xsl:when test="$content-template='home'">home</xsl:when>
            -->
            <xsl:otherwise>
                <xsl:value-of select="$layout"/>                
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:variable>
    
    <!--reads the aggregation into the variable aggregation-->
    <xsl:variable name="aggregation" select="document(concat('aggregation-',$aggregationtype, '.xml'))"/>

</xsl:stylesheet>