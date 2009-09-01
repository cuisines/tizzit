<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- START Bild und Bildunterschrif entsprechend des Type Wertes positionieren -->

    <!--END Bildunterschrift-->
    <!--Bild einfügen-->
    <xsl:template match="image" mode="type">
        <!--wenn Bild verlinkt ist -->
        <xsl:choose>
            <!-- alte img syntax -->
            <xsl:when test="not(./child::*)">
                <xsl:choose>
                    <xsl:when test="name(../..)='a' or name(../..)='internalLink'">
                        <xsl:apply-templates select="." mode="image"/>
                        <br/>
                        <xsl:apply-templates select="." mode="bu"/>
                    </xsl:when>
                    <!--wenn Bildtyp auf center steht, nur Thumbnail anzeigen mit Link auf grosses Bild -->
                    <xsl:when test="@type='center'">
                        <xsl:if test="not(@src=0)">
                            <div>
                                <a>
                                    <xsl:attribute name="href">javascript:MM_openBrWindow('/<xsl:value-of select="$clientCode"/>/popupimg.html?ejbimageid=<xsl:value-of select="@src"/>','bild','height=100,top=10,width=10,left=90');</xsl:attribute>
                                    <img hspace="0" vspace="0" border="0">
                                        <xsl:attribute name="alt">
                                            <xsl:value-of select="."/>
                                        </xsl:attribute>
                                        <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                                            <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                                            <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                                        </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=t</xsl:attribute>
                                    </img>
                                </a>
                            </div>
                            <div class="bu">
                                <xsl:apply-templates select="."/>
                            </div>
                        </xsl:if>
                    </xsl:when>
                    <!--Sonst Bild linksbuendig darstellen mit BU rechts daneben -->
                    <xsl:otherwise>
                        <xsl:if test="not(@src=0)">
                            <table cellspacing="0" cellpadding="0" width="100%">
                                <tr>
                                    <td valign="top">
                                       <xsl:apply-templates select="." mode="image"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <div class="bu">
                                            <xsl:apply-templates select="."/>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- neue img syntax, CQ 2.2 -->
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="name(../..)='a' or name(../..)='internalLink'">
                        <xsl:apply-templates select="." mode="newimage"/>
                        <br/>
                        <xsl:apply-templates select="legend[.!='']" mode="format"/>
                    </xsl:when>
                    <!--wenn Bildtyp auf center steht, nur Thumbnail anzeigen mit Link auf grosses Bild -->
                    <xsl:when test="@type='center'">
                        <xsl:if test="not(@src=0)">
                            <div>
                                <a>
                                    <xsl:attribute name="href">javascript:MM_openBrWindow('/<xsl:value-of select="$clientCode"/>/popupimg.html?ejbimageid=<xsl:value-of select="@src"/>','bild','height=100,top=10,width=10,left=90');</xsl:attribute>
                                    <img hspace="0" vspace="0" border="0">
                                        <xsl:attribute name="alt">
                                            <xsl:value-of select="alttxt"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                                            <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                                            <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                                        </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=t</xsl:attribute>
                                    </img>
                                </a>
                            </div>
                            <xsl:apply-templates select="legend[.!='']" mode="format"/>
                        </xsl:if>
                    </xsl:when>
                    <!--Sonst Bild linksbuendig darstellen mit BU rechts daneben -->
                    <xsl:otherwise>
                        <xsl:if test="not(@src=0)">
                            <table cellspacing="0" cellpadding="0" width="100%">
                                <tr>
                                    <td valign="top">
                                        <xsl:apply-templates select="." mode="newimage"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <xsl:apply-templates select="legend[.!='']" mode="format"/>
                                    </td>
                                </tr>
                            </table>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- alte img syntax -->        
    <xsl:template match="image" mode="image">
        <img hspace="0" vspace="0" border="0">
            <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
        </img>
    </xsl:template>
    
    <xsl:template match="image" mode="bu">
        <xsl:if test="not(.='')">
            <div class="bu">
                <xsl:value-of select="."/>
            </div>
        </xsl:if>
    </xsl:template>
    
    <!-- neue img syntax, CQ 2.2 -->
    <xsl:template match="image" mode="newimage">
        <img hspace="0" vspace="0" border="0">
            <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                <xsl:otherwise>/dummy.jpg</xsl:otherwise>
            </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
            <xsl:attribute name="alt"><xsl:apply-templates select="alttext"/></xsl:attribute>
        </img>
    </xsl:template>
    
    <xsl:template match="image" mode="teaser">
        <xsl:choose>
            <!-- alte img syntax -->   
            <xsl:when test="not(./child::*)">
                <img hspace="0" vspace="0" border="0" class="teaserimg">
                    <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                        <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                        <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                    </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
                </img>
            </xsl:when>
            <!-- neue img syntax, CQ 2.2 -->
            <xsl:otherwise>
                <img hspace="0" vspace="0" border="0" class="teaserimg">
                    <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                        <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                        <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                    </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
                    <xsl:attribute name="alt"><xsl:apply-templates select="alttext"/></xsl:attribute>
                </img>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="picture" mode="format">
        <xsl:apply-templates mode="type"/>
    </xsl:template>
    
    <xsl:template match="image" mode="thumbnail">
        <xsl:choose>
            <!-- alte img syntax -->   
            <xsl:when test="not(./child::*)">
                <xsl:if test="not(@src=0)">
                    <div class="thumbimage">
                        <a>
                            <xsl:attribute name="href">javascript:MM_openBrWindow('/<xsl:value-of
                                select="$clientCode"/>/popupimg.html?ejbimageid=<xsl:value-of select="@src"/>','bild','height=100,top=10,width=10,left=90');</xsl:attribute>
                            <img hspace="0" vspace="0" border="0">
                                <xsl:attribute name="alt">
                                    <xsl:value-of select="."/>
                                </xsl:attribute>
                                <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                                    <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                                    <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                                </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=t</xsl:attribute>
                            </img>
                        </a>
                    </div>
                    <div class="bu">
                        <xsl:apply-templates select="."/>
                    </div>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <!-- neue img syntax, CQ 2.2 -->
                <xsl:if test="not(@src=0)">
                    <div class="thumbimage">
                        <a>
                            <xsl:attribute name="href">javascript:MM_openBrWindow('/<xsl:value-of
                                select="$clientCode"/>/popupimg.html?ejbimageid=<xsl:value-of select="@src"/>','bild','height=100,top=10,width=10,left=90');</xsl:attribute>
                            <img hspace="0" vspace="0" border="0">
                                <xsl:attribute name="alt">
                                    <xsl:value-of select="alttxt"/>
                                </xsl:attribute>
                                <xsl:attribute name="src">/img/ejbimage<xsl:choose>
                                    <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                                    <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                                </xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=t</xsl:attribute>
                            </img>
                        </a>
                    </div>
                    <div class="bu"><xsl:apply-templates select="legend" mode="format"/></div>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="legend" mode="format" priority="-0.6">
        <div class="bu">
            <xsl:apply-templates mode="format"/>
        </div>
    </xsl:template>
    
</xsl:stylesheet>
