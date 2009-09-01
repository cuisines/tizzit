<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="content" mode="format" priority="1.1">
        <xsl:apply-templates select="text" mode="format"/>
        <xsl:apply-templates select="babies" mode="format"/>            
    </xsl:template>

    <xsl:template match="text" mode="format" priority="1.1">
        <div class="prelisttext">
            <xsl:apply-templates mode="format"/>
        </div>
    </xsl:template>

    <xsl:template match="babies" mode="format">
        <xsl:apply-templates select="babylist" mode="format"/>
    </xsl:template>
    
    <xsl:template match="babylist" mode="format">
        <table width="100%" cellpadding="0" cellspacing="0" class="list-container">
            <xsl:apply-templates select="item" mode="format">
                <xsl:sort select="concat(dateofbirth/year,format-number(dateofbirth/month,'00'),format-number(dateofbirth/day,'00'))" order="descending"/>
            </xsl:apply-templates>
        </table>
    </xsl:template>
    
    <xsl:template match="item" mode="format">
        <tr>
            <td class="list-headline">
                <xsl:value-of select="babyname"/><xsl:text>&#160;geboren am&#160;</xsl:text>
                <xsl:call-template name="getDate">
                    <xsl:with-param name="year" select="dateofbirth/year"/>
                    <xsl:with-param name="month" select="dateofbirth/month"/>
                    <xsl:with-param name="day" select="dateofbirth/day"/>
                </xsl:call-template>
            </td>
            <td class="list-headline" align="right">
                <a href="javascript:toggle('{generate-id(.)}')">mehr...</a>
            </td>
        </tr>
        <tr id="{generate-id(.)}" name="{generate-id(.)}" style="display:none;">
            <td colspan="2" class="list-detail">
                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="125"><xsl:apply-templates select="picture" mode="format"/></td>
                        <td>
                            <b><xsl:value-of select="babyname"/></b><xsl:text>&#160;erblickte am&#160;</xsl:text>
                            <b><xsl:call-template name="getDate">
                                <xsl:with-param name="year" select="dateofbirth/year"/>
                                <xsl:with-param name="month" select="dateofbirth/month"/>
                                <xsl:with-param name="day" select="dateofbirth/day"/>
                            </xsl:call-template></b> das Licht der Welt.<br/><br/>
                            <xsl:text>Größe:&#160;</xsl:text><xsl:value-of select="size"/><xsl:text>cm</xsl:text><br/>
                            <xsl:text>Gewicht:&#160;</xsl:text><xsl:value-of select="weight"/><xsl:text>g</xsl:text><br/>
                            <xsl:choose>          
                                <xsl:when test="alternativetext=''">
                                    <xsl:text>Es freuen sich &#160;</xsl:text><xsl:value-of select="parents"/>&#160;<xsl:text>.</xsl:text><br/><br/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="alternativetext"/><xsl:text>&#160;</xsl:text><xsl:value-of select="parents"/>
                                </xsl:otherwise> 
                            </xsl:choose>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </xsl:template>

    <xsl:template name="getDate">
        <xsl:param name="format"/>
        <xsl:param name="year"/>
        <xsl:param name="month"/>
        <xsl:param name="day"/>
        <xsl:choose>
            <xsl:otherwise>
                <xsl:value-of select="$day"/><xsl:text>.&#160;</xsl:text><xsl:value-of select="$month"/><xsl:text>.&#160;</xsl:text><xsl:value-of select="$year"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="picture" mode="format" priority="1.1">
        <xsl:apply-templates mode="babyalbum"/>
    </xsl:template>
    
    <xsl:template match="image" mode="babyalbum">
        <div>
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
    </xsl:template>

</xsl:stylesheet>