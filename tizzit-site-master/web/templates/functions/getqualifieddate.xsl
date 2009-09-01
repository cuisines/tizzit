<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:ctxfd="http://www.conquest-cms.net/template/xslt/functions/date"
    version="2.0"
    >

    <!-- 
    
    Erwartet als input parameter ein ConQuest Datumselement beliebigen namens. Z.B.:
        <launchdate dcfname="launchdate" label="Launch Date">
            <day>1</day>
            <month>4</month>
            <year>2005</year>
        </launchdate>    
    
    Gibt einen Datumsstring im Format YYYYMMDD zurück, mit dem nach Alter sortiert werden kann.
    
     -->

    <xsl:function name="ctxfd:getQualifiedDateString">
        <xsl:param name="inputdate"/>
        <xsl:variable name="date" select="$inputdate[1]"/>
        <xsl:variable name="qDate">
            <xsl:value-of select="$date/year"/>
            <xsl:choose>
                <xsl:when test="string-length($date/month/text()) = 1">
                    <xsl:value-of select="concat('0', $date/month/text())"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$date/month/text()"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="string-length($date/day/text()) = 1">
                    <xsl:value-of select="concat('0', $date/day/text())"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$date/day/text()"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:sequence select="$qDate"/>
    </xsl:function>

</xsl:stylesheet>