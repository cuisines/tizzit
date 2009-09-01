<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ctmpl="http://www.conquest-cms.net/template">
    <xsl:template match="minipage">
        <xsl:if test="normalize-space(minipage-head) != ''">
            <ctmpl:module name="minipages">
                <div id="teaser" class="teaser">
                    <xsl:apply-templates select="." mode="content"/>
                </div>
            </ctmpl:module>
        </xsl:if> 
    </xsl:template>
    
    <xsl:template match="minipages" mode="content">
        <!-- Headline optional -->
        <xsl:apply-templates select="minipage-head"/>
        <!-- Image optional -->
        <xsl:apply-templates select="minipage-picture/image[@src]" mode="teaser"/>
        <div class="teaserbody">
            <!-- Platzhalter unter Bild, wenn es content oder link gibt -->
            <xsl:if
                test="normalize-space(minipage-content)!='' or
                normalize-space(minipage-content//p)!='' or
                internalLink/internalLink/@viewid">
                <div class="teaserspacer">&#160;</div>
            </xsl:if>
            <!-- content -->
            <xsl:if
                test="normalize-space(minipage-content)!='' or
                normalize-space(minipage-content//p)!=''">
                <div class="teasercontent">
                    <xsl:apply-templates select="minipage-content" mode="format"/>
                </div>
            </xsl:if>
            <!-- Link -->
            <xsl:if test="internalLink/internalLink">
                <xsl:apply-templates select="internalLink" mode="teaser"/>
            </xsl:if>
        </div>
    </xsl:template>
    
    <!-- Teaser ggf. verlinken -->
    <xsl:template match="image" mode="teaser" priority="0.5">
        <div class="teaserimage">
            <xsl:choose>
                <xsl:when test="../../internalLink/internalLink/@viewid">
                    <a href="/{$language}/{../../internalLink/internalLink/@url}/page.html"
                        title="{../../internalLink/internalLink}">
                        <xsl:apply-templates select="." mode="teaser2"/>
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="." mode="teaser2"/>
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    <xsl:template match="image" mode="teaser2" priority="0.5">
        <img border="0" alt="{.}" width="198">
            <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                <xsl:otherwise>/dummy.jpg</xsl:otherwise>
            </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
        </img>
    </xsl:template>
    <xsl:template match="internalLink" mode="teaser" priority="1.1">
        <div class="teaserlink">
            <xsl:apply-templates select="." mode="format"/>
        </div>
    </xsl:template>
    <xsl:template match="minipage-content" mode="format">
        <xsl:apply-templates mode="format"/>
    </xsl:template>
    
    <xsl:template match="minipage-head">
        <xsl:if test=".!=''">
            <div class="teaserheadline">
                <xsl:value-of select="."/>
            </div>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
