<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:include href="common.xsl"/> 
    
    <xsl:template match="content" mode="include" priority="3">
        <div class="clear">&#160;</div>
        <br/>
        <xsl:choose>
            <xsl:when test="../videoChooser='1' and ../shadowBoxLinkTxt!=''">
                <a class="option">
                    <xsl:attribute name="href">
                        <xsl:text>/httpd/flash/player-viral.swf?file=</xsl:text>
                        <xsl:value-of select="../videoUrl"/>
                        <xsl:text>&amp;autostart=</xsl:text>
                        <xsl:choose>
                            <xsl:when test="../videoAutostart/autostartSelect!=''">
                                <xsl:value-of select="../videoAutostart/autostartSelect"/>
                            </xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                        </xsl:choose>
                        <xsl:text>&amp;plugins=none&amp;fullscreen=true&amp;controlbar=</xsl:text>
                        <xsl:choose>
                            <xsl:when test="../videoControlbar/videoControlbarSelect!=''">
                                <xsl:text>bottom</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                        </xsl:choose>
                        <xsl:text>&amp;image=/img/ejbimage/</xsl:text>
                        <xsl:value-of select="../picture/image/filename"/>
                        <xsl:text>?id=</xsl:text>
                        <xsl:value-of select="../picture/image/@src"/>
                    </xsl:attribute>
                    <xsl:attribute name="rel">
                        <xsl:text>shadowbox;width=</xsl:text>
                        <xsl:value-of select="../videoWidth"/>
                        <xsl:text>;height=</xsl:text>
                        <xsl:value-of select="../videoHeight"/>
                    </xsl:attribute>
                    <xsl:value-of select="../shadowBoxLinkTxt"/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <div id="container"><a href="http://www.macromedia.com/go/getflashplayer">Get the Flash Player</a> to see this player.</div>
                <script language="JavaScript" type="text/JavaScript" src="/httpd/js/swfobject.js">&#160;</script>
                <script language="JavaScript" type="text/JavaScript">
                    <xsl:variable name="autostart">
                        <xsl:choose>
                            <xsl:when test="../videoAutostart/autostartSelect!=''">
                                <xsl:value-of select="../videoAutostart/autostartSelect"/>
                            </xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="controlbar">
                        <xsl:choose>
                            <xsl:when test="../videoControlbar/videoControlbarSelect!=''">
                                <xsl:text>bottom</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <![CDATA[
                        var s1 = new SWFObject("/httpd/flash/player-viral.swf","ply","]]><xsl:value-of select="../videoWidth"/><![CDATA[","]]><xsl:value-of select="../videoHeight"/><![CDATA[","9","#FFFFFF");
                        s1.addParam("allowfullscreen","true");
                        s1.addParam("allownetworking","false");
                        s1.addParam("allowscriptaccess","always");
                        s1.addParam("flashvars","]]><xsl:text>file=</xsl:text><xsl:value-of select="../videoUrl"/><![CDATA[&]]><xsl:text>autostart=</xsl:text><xsl:value-of select="$autostart"/><![CDATA[&]]><xsl:text>plugins=none</xsl:text><![CDATA[&]]><xsl:text>fullscreen=true</xsl:text><![CDATA[&]]><xsl:text>controlbar=</xsl:text><xsl:value-of select="$controlbar"/><![CDATA[&]]><xsl:text>image=/img/ejbimage/</xsl:text><xsl:value-of select="../picture/image/filename"/><xsl:text>?id=</xsl:text><xsl:value-of select="../picture/image/@src"/><![CDATA[");
                        s1.write("container");
                    ]]>
                </script>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="../videoDownload != ''">
            <br/>
            <div class="video-downloadlink">
                <a>
                    <xsl:attribute name="href">
                        <xsl:choose>
                            <xsl:when test="contains(../videoDownload, 'http')">
                                <xsl:value-of select="../videoDownload"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>http://</xsl:text>
                                <xsl:value-of select="../videoDownload"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="../videoDownloadText != ''">
                            <xsl:value-of select="../videoDownloadText"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>Dieses Video herunterladen</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </a>
            </div>
            <br/>
        </xsl:if>
        <xsl:apply-templates select="../contentAfterVideo" mode="format"/>
    </xsl:template>
    
    <xsl:template match="contentAfterVideo" mode="format">
        <xsl:apply-templates mode="format"/>
    </xsl:template>
    
</xsl:stylesheet>