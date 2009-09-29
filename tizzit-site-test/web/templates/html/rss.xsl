<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:include href="variables.xsl"/>
    
    <xsl:variable name="viewIdInternalLink">
        <xsl:value-of select="//source/all/content/internalLink/internalLink/@viewid"/>            
    </xsl:variable>
    
    <xsl:variable name="doc" select="document(concat('cocoon:/content-',$viewIdInternalLink,'.xml'))"/>  
    
    <xsl:template match="/">
        <rss version="0.91" >
            <channel>
                <title>Tizzit.org News</title>
                <link>http://tizzit.org/</link>
                <description>Tizzit Content Management System</description>
                <language>en-en</language>
                <copyright>2009 - Tizzit.org</copyright>
                <xsl:apply-templates select="$doc//news/newslist/item">
                    <xsl:sort select="newsdate/year" data-type="number" order="descending"/>
                    <xsl:sort select="newsdate/month" data-type="number" order="descending"/>
                    <xsl:sort select="newsdate/day" data-type="number" order="descending"/>
                </xsl:apply-templates>
            </channel>
        </rss>
    </xsl:template>
    
    <xsl:template match="item" priority="2">
        <item>
            <title><xsl:value-of select="newsname"/></title>
            <link>
                <xsl:text>http://</xsl:text>
                <xsl:value-of select="$serverName"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="$language"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="$url"/>
                <xsl:text>/page.html?newsNr=</xsl:text>
                <xsl:value-of select="@timestamp"/>
            </link>
            <description><xsl:apply-templates select="text"/></description>
        </item>
    </xsl:template>
    
    <xsl:template match="picture"/>
	
</xsl:stylesheet>