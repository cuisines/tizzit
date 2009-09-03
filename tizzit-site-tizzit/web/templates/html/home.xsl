<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="common.xsl"/>

    <xsl:template match="content" mode="include-after" priority="2">
        <div id="loopedSlider">	
            <div class="container">
                <div class="slides">
                    <div><xsl:call-template name="content1"/></div>
                    <div><xsl:call-template name="content2"/></div>
                    <div><xsl:call-template name="content3"/></div>
                </div>
            </div>
            <a href="#" class="previous"><img src="/httpd/img/home/arrow_l.png" alt="Previous" border="0"/></a>
            <a href="#" class="next"><img src="/httpd/img/home/arrow_r.png" alt="Next" border="0"/></a>
            <ul class="pagination">
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
            </ul>	
        </div>
        <script type="text/javascript" charset="utf-8">
            <![CDATA[
                $(function(){
                    $('#loopedSlider').loopedSlider();
                });
            ]]>
        </script>
        <div class="clear">&#160;</div>
    </xsl:template>
    
    <xsl:template name="content1">
        <span id="kwicks">
            <ul class="kwicks">
                <li id="kwick1">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_01'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <p class="kwick_bg_01">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'01'"/>
                        </xsl:call-template>
                    </p>
                </li>
                <li id="kwick2">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_02'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <p class="kwick_bg_02">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'02'"/>
                        </xsl:call-template>
                    </p>
                </li>
                <li id="kwick3" class="kwick_last">
                    <xsl:attribute name="style">
                        <xsl:call-template name="writeBgImage">
                            <xsl:with-param name="bg" select="'bg_03'"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <p class="kwick_bg_03">
                        <xsl:call-template name="writeLink">
                            <xsl:with-param name="link" select="'03'"/>
                        </xsl:call-template>
                    </p>
                </li>
            </ul>
        </span>
    </xsl:template>
    
    <xsl:template name="content2">
        <img src="/httpd/img/home/tizzit_coverflow.png" alt="Tizzit Bilder" />
    </xsl:template>
    
    <xsl:template name="content3">
        <div id="video_container">
            <a href="http://www.macromedia.com/go/getflashplayer">Get the Flash Player</a> to see this player.
        </div>
        <script language="JavaScript" type="text/JavaScript" src="/httpd/js/swfobject.js">&#160;</script>
        <script language="JavaScript" type="text/JavaScript">
            <xsl:variable name="autostart">
                <xsl:text>false</xsl:text>
            </xsl:variable>
            <xsl:variable name="controlbar">
                <xsl:text>bottom</xsl:text>
            </xsl:variable>
            <xsl:variable name="videoWidth">
                <xsl:text>510px</xsl:text>
            </xsl:variable>
            <xsl:variable name="videoHeight">
                <xsl:text>270px</xsl:text>
            </xsl:variable>
            <![CDATA[
                        var s1 = new SWFObject("/httpd/flash/player-viral.swf","ply","]]><xsl:value-of select="$videoWidth"/><![CDATA[","]]><xsl:value-of select="$videoHeight "/><![CDATA[","9","#FFFFFF");
                        s1.addParam("allowfullscreen","true");
                        s1.addParam("allownetworking","false");
                        s1.addParam("allowscriptaccess","always");
                        s1.addParam("flashvars","]]><xsl:text>file=</xsl:text><xsl:value-of select="../videoUrl"/><![CDATA[&]]><xsl:text>autostart=</xsl:text><xsl:value-of select="$autostart"/><![CDATA[&]]><xsl:text>plugins=none</xsl:text><![CDATA[&]]><xsl:text>fullscreen=true</xsl:text><![CDATA[&]]><xsl:text>controlbar=</xsl:text><xsl:value-of select="$controlbar"/><![CDATA[&]]><xsl:text>image=/httpd/img/home/video_preview.jpg</xsl:text><![CDATA[");
                        s1.write("video_container");
                    ]]>
        </script>
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
