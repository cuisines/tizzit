<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:template match="teaserInclude[@dcfname='teaserInclude']" priority="2">
        <ctmpl:module name="teaser">
            <xsl:choose>
                <xsl:when test="teaserInclude != ''">
                    <div class="teaseraggregation">
                        <xsl:apply-templates select="teaserInclude" mode="teaserInclude2"/>
                        <div class="clear">&#160;</div>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    &#160;
                </xsl:otherwise>
            </xsl:choose>
        </ctmpl:module>
    </xsl:template>
    
    <xsl:template match="teaserInclude[@dcfname='teaserInclude_bottom']" priority="2">
        <ctmpl:module name="teaser_bottom">
            <xsl:choose>
                <xsl:when test="teaserInclude != ''">
                    <div class="teaseraggregation">
                        <xsl:apply-templates select="teaserInclude" mode="teaserInclude2"/> 
                        <div class="clear">&#160;</div>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    &#160;
                </xsl:otherwise>
            </xsl:choose>
        </ctmpl:module>
    </xsl:template>
    
    <xsl:template match="teaserInclude" mode="teaserInclude2" priority="2">
        <xsl:apply-templates select="teaser" mode="teaser"/>
    </xsl:template>
    
    <xsl:template match="teaser" mode="teaser">
        <div class="teaseritem">
            <xsl:apply-templates select="teaser" mode="teasercontent"/>
        </div>
    </xsl:template>
    
    <xsl:template match="teaser" mode="teasercontent" priority="3">
        <xsl:choose>
            <xsl:when test="headline!=''">
                <h1 class="teaserheadline">
                    <xsl:apply-templates select="headline" mode="teaser"/>
                </h1>
            </xsl:when>    
        </xsl:choose>
        <xsl:if test="picture/image !=''">
            <div class="teaserimage">	
                <xsl:apply-templates select="picture/image" mode="teaser"/>
                <div class="clear">&#160;</div>
            </div>
        </xsl:if> 
        <div class="teaser_content">         
            <xsl:apply-templates select="content" mode="format"/>
            <xsl:if test="content/p/documents/document!=''">
                <div class="teaser_documents">
                    <xsl:apply-templates select="content/p/documents" mode="teaser"/>
                </div>
               <div class="clear">&#160;</div>
            </xsl:if>
        </div>   
        <xsl:if test="internalLink/internalLink !=''">
            <xsl:apply-templates select="internalLink/internalLink" mode="teaser"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="internalLink" mode="teaser" priority="2.5">
        <div class="teaserlink">
            <a href="/{internalLink/@language}/{internalLink/@url}/page.html">
                <img src="/httpd/img/spacer.gif" width="13" height="13" alt="weiter" border="0"/>
            </a>
        </div>
        <div class="clear">&#160;</div>
    </xsl:template>
    
    <xsl:template match="headline" mode="teaser">
        <xsl:apply-templates mode="format"/>
    </xsl:template>
    
    <xsl:template match="documents" mode="teaser">
        <a target="_blank">
            <xsl:attribute name="href">
                <xsl:text>/img/ejbfile?id=</xsl:text>
                <xsl:value-of select="document/@src"/>
            </xsl:attribute>
            <xsl:value-of select="document"/>
        </a>
    </xsl:template>
    
</xsl:stylesheet>