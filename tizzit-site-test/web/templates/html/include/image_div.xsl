<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   
    <xsl:template match="picture" mode="format">
        <xsl:apply-templates mode="type"/>
    </xsl:template>
    
    <xsl:template match="image" mode="type">        
        <xsl:choose>
            <xsl:when test="name(../..)='a' or name(../..)='internalLink'">
                <xsl:apply-templates select="." mode="newimage"/>
                <br/>
                <xsl:apply-templates select="legend" mode="format"/>
            </xsl:when>
            <!--wenn Bildtyp auf center steht, mittig anzeigen-->
            <xsl:when test="@type='center'">
                <xsl:apply-templates select="." mode="center_image"/>
            </xsl:when>
            <!--Wenn Bildtyp auf right -->
            <xsl:when test="@type='right'">
                <xsl:apply-templates select="." mode="right_image"/>
            </xsl:when>     
            <!--Sonst Bild linksbuendig darstellen mit BU rechts daneben -->
            <xsl:otherwise>
                <xsl:apply-templates select="." mode="left_image"/>   
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="image" mode="left_image">        
        <div class="left_image">
            <xsl:attribute name="style">
                <xsl:text>width:</xsl:text>
                <xsl:choose>
                    <xsl:when test="$cfg-enableMaxContentImageWidth = 'true'">
                        <xsl:choose>
                            <xsl:when test="@width &gt; $cfg-maxContentImageWidth">
                                <xsl:value-of select="$cfg-maxContentImageWidth"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="@width"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="@width"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>px;</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="." mode="newimage"/>
            <xsl:apply-templates select="legend" mode="format"/>
        </div>
    </xsl:template>
    
    <xsl:template match="image" mode="center_image">
        <div class="center_image">
            <xsl:attribute name="style">
                <xsl:text>width:</xsl:text>
                <xsl:choose>
                    <xsl:when test="$cfg-enableShadowboxContentThumbnails = 'true'">
                        <xsl:value-of select="$cfg-shadowboxContentThumbnailsImageWidth"/>
                    </xsl:when>
                    <xsl:when test="$cfg-enableMaxContentImageWidth = 'true'">
                        <xsl:choose>
                            <xsl:when test="@width &gt; $cfg-maxContentImageWidth">
                                <xsl:value-of select="$cfg-maxContentImageWidth"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="@width"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="@width"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>px;</xsl:text>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="$cfg-enableShadowboxContentThumbnails = 'true'">
                    <a>
                        <xsl:attribute name="id">
                            <xsl:text>single_3</xsl:text>
                        </xsl:attribute>
                        <xsl:attribute name="href">
                            <xsl:text>/img/ejbimage</xsl:text>
                            <xsl:choose>
                                <xsl:when test="filename != ''">
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="filename"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>/dummy.jpg</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:text>?id=</xsl:text>
                            <xsl:apply-templates select="@src"/>
                            <xsl:text>&amp;typ=s</xsl:text>
                        </xsl:attribute>
                        <xsl:apply-templates select="." mode="newimage"/>
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="." mode="newimage"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="legend" mode="format"/>
        </div>
    </xsl:template>
    
    <xsl:template match="image" mode="right_image">        
        <div class="right_image">
            <xsl:attribute name="style">
                <xsl:text>width:</xsl:text>
                <xsl:choose>
                    <xsl:when test="$cfg-enableMaxContentImageWidth = 'true'">
                        <xsl:choose>
                            <xsl:when test="@width &gt; $cfg-maxContentImageWidth">
                                <xsl:value-of select="$cfg-maxContentImageWidth"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="@width"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="@width"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>px;</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="." mode="newimage"/>
            <xsl:apply-templates select="legend" mode="format"/>
        </div>
    </xsl:template>
    
    <xsl:template match="image" mode="newimage">
        <img style="border:0px;" align="left">
            <xsl:attribute name="src">
                <xsl:text>/img/ejbimage</xsl:text>
                <xsl:choose>
                    <xsl:when test="filename != ''">
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="filename"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>/dummy.jpg</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>?id=</xsl:text>
                <xsl:apply-templates select="@src"/>
                <xsl:text>&amp;typ=</xsl:text>
                <xsl:choose>
                    <xsl:when test="$cfg-enableShadowboxContentThumbnails = 'true'">
                        <xsl:text>t</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>s</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="alt">
                <xsl:choose>
                    <xsl:when test="alttext !=''">
                        <xsl:apply-templates select="alttext"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>AlternativeText</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="style">
                <xsl:text>width:</xsl:text>
                <xsl:choose>
                    <xsl:when test="$cfg-enableShadowboxContentThumbnails = 'true'">
                        <xsl:value-of select="$cfg-shadowboxContentThumbnailsImageWidth"/>
                    </xsl:when>
                    <xsl:when test="$cfg-enableMaxContentImageWidth = 'true'">
                        <xsl:choose>
                            <xsl:when test="@width &gt; $cfg-maxContentImageWidth">
                                <xsl:value-of select="$cfg-maxContentImageWidth"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="@width"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="@width"/>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>px;</xsl:text>
            </xsl:attribute>
        </img>
    </xsl:template>
    
    <xsl:template match="legend" mode="format">
        <xsl:if test="not(.='')">
            <div class="bu">
                <xsl:value-of select="."/>
            </div>
        </xsl:if>
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
