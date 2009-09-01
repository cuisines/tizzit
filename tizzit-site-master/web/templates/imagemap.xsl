<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ctmpl="http://www.conquest-cms.net/template">
    <xsl:template match="text" mode="format">
        <div>
            <xsl:apply-templates mode="format"/>
        </div>
    </xsl:template>
    <xsl:template match="imagemap" mode="format">
        <xsl:apply-templates select="picture" mode="imagemap"/>
         <xsl:apply-templates select="picture" mode="bu"/>
        <xsl:apply-templates select="links" mode="format"/>
        <!-- Text unterhalb der Imagemap -->
        <xsl:apply-templates select="../content-below" mode="format"/>
    </xsl:template>
    <xsl:template match="content-below" mode="format">
        <div>
            <xsl:apply-templates mode="format"/>
        </div>
    </xsl:template>
    <xsl:template match="links" mode="format">
        <map name="map">
            <xsl:apply-templates select="item" mode="imagemap"/>
        </map>
    </xsl:template>
    <xsl:template match="item" mode="imagemap">
        <xsl:choose>
            <!-- Wenn der interne Link auf die eigene Seite zeigt, nur Ankerlinks ansteuern und nicht Seite neuladen -->
            <xsl:when test="$viewComponentId=internalLink/internalLink/@viewid">
                <area>
                    <xsl:attribute name="href">#<xsl:value-of
                            select="internalLink/internalLink/@anchor"/>
                    </xsl:attribute>
                    <xsl:attribute name="title">
                        <xsl:value-of select="linkname"/>
                    </xsl:attribute>
                    <xsl:attribute name="coords">
                        <xsl:value-of select="coords"/>
                    </xsl:attribute>
                    <xsl:attribute name="shape">
                        <xsl:value-of select="shape/value"/>
                    </xsl:attribute>
                </area>
            </xsl:when>           <!--  
           ======== Internen Link im Popup oeffnen ===========           
           Beispiel: {popup:[height=222] [width=333] [top=20] [left=30] [scrollbars=yes]}-->
           <xsl:when test="starts-with(internalLink/internalLink, '{popup:')">
                <xsl:variable name="height"><xsl:value-of select="substring-before(substring-after(internalLink/internalLink,'height='),']')"/></xsl:variable>
                <xsl:variable name="width"><xsl:value-of select="substring-before(substring-after(internalLink/internalLink,'width='),']')"/></xsl:variable>
                <xsl:variable name="left"><xsl:value-of select="substring-before(substring-after(internalLink/internalLink,'left='),']')"/></xsl:variable>
                <xsl:variable name="top"><xsl:value-of select="substring-before(substring-after(internalLink/internalLink,'top='),']')"/></xsl:variable>               
               <xsl:variable name="scrollbars"><xsl:value-of select="substring-before(substring-after(internalLink/internalLink,'scrollbars='),']')"/></xsl:variable>               
                <area href="#">
                    <xsl:attribute name="onclick">
                    MM_openBrWindow('/<xsl:value-of
                            select="internalLink/internalLink/@language"/>/<xsl:value-of
                            select="internalLink/internalLink/@url"/>/popup.html<xsl:if
                            test="internalLink/internalLink/@anchor">#<xsl:value-of 
                            select="internalLink/internalLink/@anchor"/></xsl:if>','popup','width=<xsl:value-of 
                            select="$width"/>,height=<xsl:value-of 
                            select="$height"/>,top=<xsl:value-of 
                            select="$top"/>,left=<xsl:value-of 
                            select="$left"/>,scrollbars=<xsl:value-of 
                            select="$scrollbars"/>,resizable=yes');         
                    </xsl:attribute>
                    <xsl:attribute name="title">
                        <xsl:value-of select="linkname"/>
                    </xsl:attribute>
                    <xsl:attribute name="coords">
                        <xsl:value-of select="coords"/>
                    </xsl:attribute>
                    <xsl:attribute name="shape">
                        <xsl:value-of select="shape/value"/>
                    </xsl:attribute>
                </area>
            </xsl:when> 
            <xsl:otherwise>
                <area>
                    <xsl:attribute name="href">/<xsl:value-of
                            select="internalLink/internalLink/@language"/>/<xsl:value-of
                            select="internalLink/internalLink/@url"/>/page.html<xsl:if
                            test="internalLink/internalLink/@anchor">#<xsl:value-of
                                select="internalLink/internalLink/@anchor"/>
                        </xsl:if>
                    </xsl:attribute>
                    <xsl:attribute name="title">
                        <xsl:value-of select="linkname"/>
                    </xsl:attribute>
                    <xsl:attribute name="coords">
                        <xsl:value-of select="coords"/>
                    </xsl:attribute>
                    <xsl:attribute name="shape">
                        <xsl:value-of select="shape/value"/>
                    </xsl:attribute>
                </area>
            </xsl:otherwise>
        </xsl:choose>       
    </xsl:template>
    
    <xsl:template match="image" mode="imagemap">
        <table cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td align="{@type}">
                	<img usemap="#map" border="0">
                			<xsl:attribute name="src">/img/ejbimage<xsl:choose>
                				<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                				<xsl:otherwise>/dummy.jpg</xsl:otherwise>
                			</xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
                	</img>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
