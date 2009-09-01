<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:template match="teaserInclude" priority="0.1">
        <ctmpl:module name="teaser">
            <div class="teaseraggregation">
                <xsl:apply-templates select="teaserInclude/teaser/teaser" mode="teaser"/> &#160;
            </div>
        </ctmpl:module>
    </xsl:template>
    
    <xsl:template match="teaser" mode="teaser">
        <div class="teaserItem">
            <div class="teaser">
                <div class="teaserHeadline">
                    <xsl:value-of select="teaserHeadline"/>
                    &#160;
                </div>
                <xsl:apply-templates select="singleTeaser/item" mode="singleTeaser"/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="item" mode="singleTeaser" priority="0.1">
        <xsl:if test="picture/image">
            <div class="teaserimage">
                <xsl:apply-templates select="picture/image" mode="newimage"/>
            </div>
        </xsl:if>
        
        <div class="teaserBody">
            <xsl:if test="teaserContent//*!=''">
                <div class="teasercontent">
                    <xsl:apply-templates select="teaserContent/*" mode="format"/>
                </div>
            </xsl:if>
            <xsl:apply-templates select="linkIteration/item/links"/>
        </div>
        
    </xsl:template>
    
    <!-- teaserlinks schreiben -->
    <xsl:template match="links">
        <xsl:if test=".!=''">
            <div class="teaserLink">
                <xsl:apply-templates mode="format"/>
            </div>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
