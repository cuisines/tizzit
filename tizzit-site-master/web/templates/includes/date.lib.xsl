<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:java="http://xml.apache.org/xslt/java"
                xmlns:simpledateformat="xalan://java.text.SimpleDateFormat"
                xmlns:date="xalan://java.util.Date"
                extension-element-prefixes="simpledateformat date"
                exclude-result-prefixes="java simpledateformat">

    <!-- 
    Example Usage:
        <xsl:call-template name="getDate">
            <xsl:with-param name="format" select="'yyyyMMdd'"/>
        </xsl:call-template>
    -->
	<xsl:template name="getDate">
        <xsl:param name="format" select="''"/>
        <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new($format), java:java.util.Date.new())"/>
	</xsl:template>

    <!-- 
    Example Usage:
        <xsl:call-template name="getFormatDateFromString">
            <xsl:with-param name="date" select="'2004 9 1'"/>
            <xsl:with-param name="inFormat" select="'yyyy M d'"/>
            <xsl:with-param name="outFormat" select="'yyyy MM dd'"/>
        </xsl:call-template>    
    -->
	<xsl:template name="getFormatDateFromString">
	    <xsl:param name="date" />
	    <xsl:param name="inFormat" />
        <xsl:param name="outFormat" />
        
        <xsl:variable name="javaInFormat" select="simpledateformat:new($inFormat)"/>
        <xsl:variable name="javaOutFormat" select="simpledateformat:new($outFormat)"/>
        <xsl:variable name="javaDate" select="simpledateformat:parse($javaInFormat, $date)" />
        
        <xsl:value-of select="simpledateformat:format($javaOutFormat, $javaDate)" />
	</xsl:template>

    <!-- 
    Example Usage:
  
    -->
	<xsl:template name="compareDate">
	    <xsl:param name="date1" />
	    <xsl:param name="date2" />
	    <xsl:param name="inFormat1" />
        <xsl:param name="inFormat2" />
        
        <xsl:variable name="javaInFormat1" select="simpledateformat:new($inFormat1)"/>
        <xsl:variable name="javaInFormat2" select="simpledateformat:new($inFormat2)"/>
        <xsl:variable name="javaDate1" select="simpledateformat:parse($javaInFormat1, $date1)" />
        <xsl:variable name="javaDate2" select="simpledateformat:parse($javaInFormat2, $date2)" />
        
        <xsl:variable name="compareFormat" select="'yyyyMMdd'"/>
        <xsl:variable name="dateString1" select="simpledateformat:format($compareFormat, $javaDate1)"/>
        <xsl:variable name="dateString2" select="simpledateformat:format($compareFormat, $javaDate2)"/>
        
        <xsl:choose>
            <xsl:when test="dateString1 = dateString2"><xsl:text>0</xsl:text></xsl:when>
            <xsl:when test="dateString1 &gt; dateString2"><xsl:text>1</xsl:text></xsl:when>
            <xsl:otherwise><xsl:text>-1</xsl:text></xsl:otherwise>
        </xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>