<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   
    <xsl:template match="picture" mode="format">
        <xsl:apply-templates mode="type"/>
    </xsl:template>
    
    <xsl:template match="image" mode="type">        
        <xsl:choose>
            <xsl:when test="name(../..)='a' or name(../..)='internalLink'">
                <xsl:apply-templates select="." mode="newimage"/>
                <xsl:apply-templates select="legend" mode="format"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="." mode="image"/>   
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="image" mode="image">
        <div>
            <xsl:attribute name="class">
                <xsl:text>image </xsl:text>
                <xsl:text>image_</xsl:text>
                <xsl:value-of select="@type"/>
                <xsl:if test="@popup='true'">
                    <xsl:text> image_popup</xsl:text>
                </xsl:if>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="@popup='true'">
                    <a href="/img/ejbimage/{filename}?id={@src}" rel="group" id="single_3" title="{alttext}">
                        <xsl:apply-templates select="." mode="imageBuilder"/>
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="." mode="imageBuilder"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="legend!=''">
                <div class="bu">
                    <xsl:value-of select="."/>
                </div>
            </xsl:if>
        </div>
    </xsl:template>
    
    <xsl:template match="image" mode="imageBuilder">
        <img style="border:0px;" src="/img/ejbimage/{filename}?id={@src}" alt="{alttext}" title="{alttext}"/>
    </xsl:template>
    
    <xsl:template match="image" mode="teaser" priority="2">      
        <img border="0">
        	<xsl:attribute name="src">/img/ejbimage<xsl:choose>
        		<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
        		<xsl:otherwise>/dummy.jpg</xsl:otherwise>
        	</xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
            <xsl:attribute name="alt"><xsl:apply-templates select="alttext"/></xsl:attribute>
        </img>         
    </xsl:template>
    
</xsl:stylesheet>