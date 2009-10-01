<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="common.xsl"/>

    <xsl:template match="content" mode="include-before" priority="2">
        <div id="loopedSlider">	
            <div class="container">
                <div class="slides">
                    <div><xsl:call-template name="content1"/></div>
                    <div><xsl:call-template name="content2"/></div>
                    <div><xsl:call-template name="content3"/></div>
                </div>
            </div>
            <a href="#" class="previous"><img src="/httpd/img/spacer.gif" alt=""/></a>
            <a href="#" class="next"><img src="/httpd/img/spacer.gif" alt=""/></a>
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
    
    <xsl:template match="content[@dcfname='homeContent']" mode="format" priority="1">
        <div class="homeContent">
            <xsl:apply-templates mode="format"/>
        </div>
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
        <img src="/img/ejbimage/{../picture[@dcfname='sl2_picture']/image/filename}?id={../picture[@dcfname='sl2_picture']/image/@src}" alt="Tizzit Bilder" />
    </xsl:template>
    
    <xsl:template name="content3">
        <span id="video_container">
            <a href="http://www.macromedia.com/go/getflashplayer">Get the Flash Player</a> to see this player.
        </span>
        <script language="JavaScript" type="text/JavaScript" src="/httpd/js/swfobject.js">&#160;</script>
        <script language="JavaScript" type="text/JavaScript">
            <xsl:variable name="autostart">
                <xsl:text>false</xsl:text>
            </xsl:variable>
            <xsl:variable name="controlbar">
                <xsl:text>bottom</xsl:text>
            </xsl:variable>
            <xsl:variable name="videoWidth">
                <xsl:text>476px</xsl:text>
            </xsl:variable>
            <xsl:variable name="videoHeight">
                <xsl:text>270px</xsl:text>
            </xsl:variable>
            <![CDATA[
                var s1 = new SWFObject("/httpd/flash/player-viral.swf","ply","]]><xsl:value-of select="$videoWidth"/><![CDATA[","]]><xsl:value-of select="$videoHeight "/><![CDATA[","9","#FFFFFF");
                s1.addParam("allowfullscreen","true");
                s1.addParam("allownetworking","false");
                s1.addParam("allowscriptaccess","always");
                s1.addParam("flashvars","]]><xsl:text>file=</xsl:text><xsl:value-of select="../videoUrl"/><![CDATA[&]]><xsl:text>autostart=</xsl:text><xsl:value-of select="$autostart"/><![CDATA[&]]><xsl:text>plugins=none</xsl:text><![CDATA[&]]><xsl:text>fullscreen=true</xsl:text><![CDATA[&]]><xsl:text>controlbar=</xsl:text><xsl:value-of select="$controlbar"/><![CDATA[&]]><xsl:text>image=/img/ejbimage/</xsl:text><xsl:value-of select="../picture[@dcfname='video_picture']/image/filename"/><xsl:text>?id=</xsl:text><xsl:value-of select="../picture[@dcfname='video_picture']/image/@src"/><![CDATA[");
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
        
        <xsl:text>filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/img/ejbimage/</xsl:text>
        <xsl:value-of select="../picture[@dcfname=$bg]/image/filename"/>
        <xsl:text>?id=</xsl:text>
        <xsl:value-of select="../picture[@dcfname=$bg]/image/@src"/>
        <xsl:text>',sizingMethod='crop');</xsl:text>
        <xsl:text>_background:none;</xsl:text>
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
    
    <xsl:template match="information_1">
        <ctmpl:module name="information_1">
            <xsl:choose>
                <xsl:when test="//newslist != ''">
                    <div class="latestNews">
                        <h1>Latest News</h1>
                        <xsl:apply-templates select="//newslist" mode="latestnews"/>
                        <div class="clear">&#160;</div>
                        <xsl:if test="//internalLink[@dcfname='linkToNews']/internalLink/@url!=''">
                            <div class="allNews">
                                <xsl:call-template name="allNews"/>
                            </div>
                        </xsl:if>
                    </div>  
                </xsl:when>
                <xsl:otherwise>
                    <div class="clear">&#160;</div>
                </xsl:otherwise>
            </xsl:choose>
        </ctmpl:module> 
    </xsl:template>
    
    <xsl:template match="newslist" mode="latestnews">
        <xsl:choose>
            <xsl:when test="count(item)&gt;2">
                <div class="newsLeft">
                    <xsl:apply-templates select="item" mode="latestnews">
                        <xsl:sort select="newsdate/year" data-type="number" order="descending"/>
                        <xsl:sort select="newsdate/month" data-type="number" order="descending"/>
                        <xsl:sort select="newsdate/day" data-type="number" order="descending"/>
                    </xsl:apply-templates>
                </div>
                <div class="newsRight">
                    <xsl:apply-templates select="item" mode="latestnews">
                        <xsl:sort select="newsdate/year" data-type="number" order="descending"/>
                        <xsl:sort select="newsdate/month" data-type="number" order="descending"/>
                        <xsl:sort select="newsdate/day" data-type="number" order="descending"/>
                        <xsl:with-param name="counter" select="'1'"/>
                    </xsl:apply-templates>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="item" mode="latestnews">
                    <xsl:sort select="newsdate/year" data-type="number" order="descending"/>
                    <xsl:sort select="newsdate/month" data-type="number" order="descending"/>
                    <xsl:sort select="newsdate/day" data-type="number" order="descending"/>
                </xsl:apply-templates>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="item" mode="latestnews">
        <xsl:param name="counter"/>

        <xsl:choose>
            <xsl:when test="$counter='1'">
                <xsl:if test="position()&gt;2 and position()&lt;5">
                    <div class="newsItem">
                        <div class="newsDate">
                            <xsl:value-of select="newsdate/month"/>
                            <xsl:text>/</xsl:text>
                            <xsl:value-of select="newsdate/day"/>
                            <xsl:text>/</xsl:text>
                            <xsl:value-of select="newsdate/year"/>
                        </div>
                        <h2 class="newsHeadline">
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@language"/>
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@url"/>
                                    <xsl:text>/page.html?newsNr=</xsl:text>
                                    <xsl:value-of select="@timestamp"/>
                                </xsl:attribute>
                                <xsl:value-of select="newsname"/>
                            </a>
                        </h2>
                        <div class="newsContent">
                            <xsl:value-of select="substring(text, 0, 150)"/>
                            <xsl:if test="string-length(text)&gt;150">
                                &#160;
                                <a class="moreLink">
                                    <xsl:attribute name="href">
                                        <xsl:text>/</xsl:text>
                                        <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@language"/>
                                        <xsl:text>/</xsl:text>
                                        <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@url"/>
                                        <xsl:text>/page.html?newsNr=</xsl:text>
                                        <xsl:value-of select="@timestamp"/>
                                    </xsl:attribute>
                                    <xsl:text>...more</xsl:text>
                                </a>
                            </xsl:if>
                        </div>
                    </div>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="position()&gt;0 and position()&lt;3">
                    <div class="newsItem">
                        <div class="newsDate">
                            <xsl:value-of select="newsdate/month"/>
                            <xsl:text>/</xsl:text>
                            <xsl:value-of select="newsdate/day"/>
                            <xsl:text>/</xsl:text>
                            <xsl:value-of select="newsdate/year"/>
                        </div>
                        <h2 class="newsHeadline">
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@language"/>
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@url"/>
                                    <xsl:text>/page.html?newsNr=</xsl:text>
                                    <xsl:value-of select="@timestamp"/>
                                </xsl:attribute>
                                <xsl:value-of select="newsname"/>
                            </a>
                        </h2>
                        <div class="newsContent">
                            <xsl:value-of select="substring(text, 0, 150)"/>
                            <xsl:if test="string-length(text)&gt;150">
                                &#160;
                                <a class="moreLink">
                                    <xsl:attribute name="href">
                                        <xsl:text>/</xsl:text>
                                        <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@language"/>
                                        <xsl:text>/</xsl:text>
                                        <xsl:value-of select="//internalLink[@dcfname='linkToNews']/internalLink/@url"/>
                                        <xsl:text>/page.html?newsNr=</xsl:text>
                                        <xsl:value-of select="@timestamp"/>
                                    </xsl:attribute>
                                    <xsl:text>...more</xsl:text>
                                </a>
                            </xsl:if>
                        </div>
                    </div>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="allNews">
        <xsl:apply-templates select="//internalLink[@dcfname='linkToNews']/internalLink" mode="format"/>
        <div class="clear">&#160;</div>
    </xsl:template>
    
</xsl:stylesheet>