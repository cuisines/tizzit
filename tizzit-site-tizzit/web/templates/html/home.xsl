<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="common.xsl"/>

    <xsl:template match="content" mode="include-after" priority="2">
        <div id="kwicks">
            <ul class="kwicks">
                <li id="kwick1">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_01'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <div class="kwick_bg_01">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'01'"/>
                        </xsl:call-template>
                    </div>
                </li>
                <li id="kwick2">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_02'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <div class="kwick_bg_02">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'02'"/>
                        </xsl:call-template>
                    </div>
                </li>
                <li id="kwick3" class="kwick_last">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_03'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <div class="kwick_bg_03">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'03'"/>
                        </xsl:call-template>
                    </div>
                </li>
            </ul>
        </div>
        <div class="clear">&#160;</div>
    </xsl:template>
    
    <xsl:template name="writeBgImage">
        <xsl:param name="bg"/>
        
        <xsl:text>background-image:url('/img/ejbimage/</xsl:text>
        <xsl:value-of select="../picture[@dcfname=$bg]/image/filename"/>
        <xsl:text>?id=</xsl:text>
        <xsl:value-of select="../picture[@dcfname=$bg]/image/@src"/>
        <xsl:text>');</xsl:text>
    </xsl:template>
    
    <xsl:template name="writeLink">
        <xsl:param name="link"/>
        
        <a target="_self">
            <xsl:attribute name="href">
                <xsl:choose>
                    <xsl:when test="../internalLink[@dcfname=$link]/internalLink/@url">
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="../internalLink[@dcfname=$link]/internalLink/@language"/>
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="../internalLink[@dcfname=$link]/internalLink/@url"/>
                        <xsl:text>/page.html</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>#</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>            
            &#160;
        </a>
    </xsl:template>

</xsl:stylesheet>
