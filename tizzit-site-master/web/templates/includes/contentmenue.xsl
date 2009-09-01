<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    <!--<xsl:param name="viewComponentId" select="0"/>-->
    <!--Contentmenue -->
    <xsl:template match="navigation" mode="format">
        <xsl:apply-templates select="viewcomponent/viewcomponent" mode="format"/>
        <br/>
    </xsl:template>
    <!--Seiten mit Template 'subsum' nicht in Navigation anzeigen-->
    <xsl:template match="viewcomponent[template='subsum']" mode="format" priority="1"/>
    <!-- link schreiben -->
    <xsl:template match="viewcomponent" mode="format">
        <xsl:choose>
            <xsl:when test="extUrl">
                <xsl:choose>
                    <xsl:when test="viewIndex='4'">
                        <a target="_blank">
                            <xsl:attribute
                                    name="href">/index_4.html?extUrl=<xsl:value-of select="extUrl"/>
                            </xsl:attribute>
                            <xsl:value-of select="linkName"/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <a target="_self">
                            <xsl:attribute name="href">
                                <xsl:value-of select="extUrl"/>
                            </xsl:attribute>
                            <xsl:value-of select="linkName"/>
                        </a>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="viewLevel='7'">
                <a target="_self">
                    <xsl:attribute name="href">
                        <xsl:value-of select="url"/>
                    </xsl:attribute>
                    <xsl:value-of select="linkName"/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <a>
                    <xsl:call-template name="href"/>
                    <xsl:value-of select="linkName"/>
                </a>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
<!-- external Link  -->
    <xsl:template match="externalLink" mode="format" priority="-0.5">
        <xsl:choose>
            <xsl:when test="starts-with(a/text(), '{popup:')">
                <xsl:apply-templates mode="popup"/>
            </xsl:when>
            <xsl:otherwise>            
                <xsl:apply-templates select="a" mode="format"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
   ======== Externen Link im Popup oeffnen, alte Syntax, deprecated seit CQ 2.4.6, 26.6.2007, Hato ===========           
   Beispiel: {popup:[height=222] [width=333] [top=20] [left=30] [scrollbars=yes] [popurl=http://google.de]}-->      
    <xsl:template match="a" mode="popup">
                <xsl:variable name="height"><xsl:value-of select="substring-before(substring-after(text(),'height='),']')"/></xsl:variable>
                <xsl:variable name="width"><xsl:value-of select="substring-before(substring-after(text(),'width='),']')"/></xsl:variable>
                <xsl:variable name="left"><xsl:value-of select="substring-before(substring-after(text(),'left='),']')"/></xsl:variable>
                <xsl:variable name="top"><xsl:value-of select="substring-before(substring-after(text(),'top='),']')"/></xsl:variable>               
               <xsl:variable name="scrollbars"><xsl:value-of select="substring-before(substring-after(text(),'scrollbars='),']')"/></xsl:variable>  
               <xsl:variable name="popurl"><xsl:value-of select="substring-before(substring-after(text(),'popurl='),']')"/></xsl:variable>               
                <a href="#">
                    <xsl:attribute name="onclick">
                    MM_openBrWindow('<xsl:value-of 
                            select="$popurl"/>','popup','width=<xsl:value-of 
                            select="$width"/>,height=<xsl:value-of 
                            select="$height"/>,top=<xsl:value-of 
                            select="$top"/>,left=<xsl:value-of 
                            select="$left"/>,scrollbars=<xsl:value-of 
                            select="$scrollbars"/>,resizable=yes');         
                    </xsl:attribute>
                    <xsl:value-of select="substring-after(.,']}')"/>
                </a>
    </xsl:template>
    
    <!--internalLink-->
    <xsl:template match="internalLink" mode="format">
        <xsl:choose>
            <xsl:when test="internalLink">
                <xsl:apply-templates select="internalLink" mode="now"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="." mode="now"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--internalLink-->
    <xsl:template match="internalLink" mode="now">
        <xsl:choose>
            <!--		Wenn der interne Link auf die eigene Seite zeigt, nur Ankerlinks ansteuern und nicht Seite neuladen-->
            <xsl:when test="$viewComponentId=@viewid">
                <a>
                    <xsl:apply-templates select="@displayType" mode="format"/>
                    <xsl:attribute name="href">#<xsl:value-of select="@anchor"/>
                    </xsl:attribute>
                    <xsl:apply-templates mode="format"/>
                </a>
            </xsl:when>   <!--  
                ======== Internen Link im Popup oeffnen, alte Syntax, deprecated seit CQ 2.4.6, 26.6.2007, Hato ===========           
                Beispiel: {popup:[height=222] [width=333] [top=20] [left=30] [scrollbars=yes]}-->
            <xsl:when test="starts-with(text(), '{popup:')">
                <xsl:variable name="height"><xsl:value-of select="substring-before(substring-after(text(),'height='),']')"/></xsl:variable>
                <xsl:variable name="width"><xsl:value-of select="substring-before(substring-after(text(),'width='),']')"/></xsl:variable>
                <xsl:variable name="left"><xsl:value-of select="substring-before(substring-after(text(),'left='),']')"/></xsl:variable>
                <xsl:variable name="top"><xsl:value-of select="substring-before(substring-after(text(),'top='),']')"/></xsl:variable>               
                <xsl:variable name="scrollbars"><xsl:value-of select="substring-before(substring-after(text(),'scrollbars='),']')"/></xsl:variable>               
                <a href="#">
                    <xsl:attribute name="onclick">
                        MM_openBrWindow('/<xsl:value-of
                            select="@language"/>/<xsl:value-of
                                select="@url"/>/popup.html<xsl:if
                                    test="@anchor">#<xsl:value-of 
                                        select="@anchor"/></xsl:if>','popup','width=<xsl:value-of 
                                            select="$width"/>,height=<xsl:value-of 
                                                select="$height"/>,top=<xsl:value-of 
                                                    select="$top"/>,left=<xsl:value-of 
                                                        select="$left"/>,scrollbars=<xsl:value-of 
                                                            select="$scrollbars"/>,resizable=yes');         
                    </xsl:attribute>
                    <xsl:choose>
                        <xsl:when test="picture">
                            <xsl:apply-templates select="picture" mode="format"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="substring-after(.,']}')"/>                            
                        </xsl:otherwise>
                    </xsl:choose>
                </a>
            </xsl:when>                   
            <xsl:otherwise>
                <a>
                    <xsl:apply-templates select="@displayType" mode="format"/>
                    <xsl:attribute name="href">
                        <xsl:choose>
                            <!-- popup window -->
                            <xsl:when test="../popup/windowTitle!=''">
                                <xsl:apply-templates select="../popup" mode="popupsrc"/>
                            </xsl:when>
                            <!-- self -->
                            <xsl:otherwise>
                                <xsl:apply-templates select="." mode="internalLinkSrc"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:apply-templates mode="format"/>
                </a>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template match="internalLink" mode="internalLinkSrc" priority="-0.1">
        <xsl:text>/</xsl:text>
        <xsl:value-of select="@language"/>
        <xsl:text>/</xsl:text>
        <xsl:value-of select="@url"/>
        <xsl:text>/page.html</xsl:text>
        <xsl:if test="@anchor">#<xsl:value-of select="@anchor"/></xsl:if>
    </xsl:template>
    
    
    <!-- displayType in internal und external Link in css Klasse umwandeln -->
    <xsl:template match="@displayType" mode="format" priority="-0.1">
        <xsl:if test=".='inline'">
            <xsl:attribute name="class">
                <xsl:text>inline</xsl:text>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>
        
    <!--
Link that opens with header-->
    <xsl:template match="a[@header='true']" mode="format">
        <a>
            <xsl:attribute name="target">_blank</xsl:attribute>
            <xsl:attribute name="href">/<xsl:value-of
                    select="$clientCode"/>/index_4.html?extUrl=<xsl:value-of select="@href"/>&amp;level=7</xsl:attribute>
            <xsl:apply-templates mode="format"/>
        </a>
    </xsl:template>
    <!--document-->
    <xsl:template match="document" mode="format">
        <xsl:choose>
            <xsl:when test="document">
                <xsl:apply-templates select="document" mode="format"/>
            </xsl:when>
            <!--
            Flash als Flash darstellen.
            Beispiel: flash:[height=222] [width=333]-->
            <xsl:when test="starts-with(text(), 'flash:')">
                <xsl:variable name="height"><xsl:value-of select="substring-before(substring-after(text(),'height='),']')"/></xsl:variable>
                <xsl:variable name="width"><xsl:value-of select="substring-before(substring-after(text(),'width='),']')"/></xsl:variable>
                <object name="movie"
                    classid="CLSID:D27CDB6E-AE6D-11cf-96B8-444553540000"
                    codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" 
                    id="movie" 
                    height="{$height}" 
                    width="{$width}" >
                    <param name="movie">
                        <xsl:attribute
                            name="value">/img/ejbfile<xsl:choose>
                                <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                                <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                            </xsl:choose>?id=<xsl:value-of select="@src"/>
                        </xsl:attribute>
                    </param>
                    <param name="allowScriptAccess" value="always" />
                    <param name="quality" value="high"/>
                    <param name="scale" value="exactfit"/>
                    <embed name="movie" quality="high" scale="exactfit"
                        bgcolor="#FFFFFF" type="application/x-shockwave-flash"
                        pluginspace="http://www.macromedia.com/go/getflashplayer"                        
                        height="{$height}" 
                        width="{$width}" >
                        <xsl:attribute name="src">/img/ejbfile<xsl:choose>
                            <xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
                            <xsl:otherwise>/dummy.jpg</xsl:otherwise>
                        </xsl:choose>?id=<xsl:value-of select="@src"/></xsl:attribute>
                    </embed>
                </object>
            </xsl:when>
            <!--normalerweise Dokument zum Download anbieten -->
            <xsl:otherwise>
                <a>
                    <xsl:call-template name="hrefdocument"/>
                    <xsl:attribute name="target">_blank</xsl:attribute>
                    <xsl:value-of select="."/>
                </a>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="hrefdocument">
        <xsl:attribute name="href">/img/ejbfile/<xsl:value-of select="@documentName"/>?id=<xsl:value-of select="@src"/></xsl:attribute>
    </xsl:template>
    
    <xsl:template name="href">
        <xsl:choose>
            <xsl:when test="extUrl">
                <xsl:attribute name="target">_blank</xsl:attribute>
                <xsl:attribute name="href">
                    <xsl:value-of select="extUrl"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="target">_self</xsl:attribute>
                <xsl:attribute name="href">/<xsl:value-of
                        select="$language"/>/<xsl:value-of select="url"/>/content.html</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="submenue">
        <div class="submenue">
            <xsl:apply-templates select="navigation/viewcomponent/viewcomponent" mode="submenue"/>
        </div>
    </xsl:template>
    <xsl:template match="viewcomponent" mode="submenue">
        <div class="submenuefield">
            <xsl:apply-templates select="." mode="format"/>
        </div>
    </xsl:template>
    <xsl:template match="next">
        <xsl:apply-templates select="show/navigation" mode="next"/>
    </xsl:template>
    <xsl:template match="viewcomponent" mode="next">
        <div class="next">
            <a>
                <xsl:call-template name="href"/>nächste Seite</a>
        </div>
    </xsl:template>
    <xsl:template match="druck">
        <xsl:apply-templates select="show/navigation" mode="druck"/>
    </xsl:template>
    <xsl:template match="viewcomponent" mode="druck">
        <div class="druck">
            <a>
                <xsl:call-template name="href"/>Druckansicht</a>
        </div>
    </xsl:template>
    <!--Fuer Traverse menue -->
    <xsl:template match="viewcomponent" mode="nextsymbol">
        <a>
            <xsl:call-template name="hreftmenu"/>
            <xsl:attribute name="title">
                <xsl:value-of select="linkName"/>
            </xsl:attribute><![CDATA[>>]]></a>
    </xsl:template>
    <xsl:template match="viewcomponent" mode="prevsymbol">
        <a>
            <xsl:call-template name="hreftmenu"/>
            <xsl:attribute name="title">
                <xsl:value-of select="linkName"/>
            </xsl:attribute><![CDATA[<<]]></a>
    </xsl:template>
    <xsl:template name="hreftmenu">
        <xsl:choose>
            <xsl:when test="extUrl">
                <xsl:attribute name="target">_blank</xsl:attribute>
                <xsl:attribute name="href">
                    <xsl:value-of select="extUrl"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="target">_self</xsl:attribute>
                <xsl:attribute name="href">/<xsl:value-of
                        select="$language"/>/<xsl:value-of select="url"/>/page.html</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- START popup for internal and external Links, since CQ 2.4.6, Hato, 26.06.2007 -->
    <xsl:template match="a[parent::externalLink[popup/windowTitle!='']]" mode="format" priority="2.3">
        <a>	
            <xsl:apply-templates select="@displayType" mode="format"/>
            <xsl:apply-templates select="../popup" mode="srcAttr"/>
            <xsl:apply-templates mode="format"/>
        </a>
    </xsl:template>
    
    <xsl:template match="popup" mode="srcAttr">
        <xsl:attribute name="href">
            <xsl:apply-templates select="." mode="popupsrc"/>
        </xsl:attribute>		
    </xsl:template>
    
    <xsl:template match="popup" mode="popupsrc" >
        <xsl:text>javascript:MM_openBrWindow('</xsl:text>
        <xsl:apply-templates select="." mode="theUrl"/>		
        <xsl:text>','</xsl:text>
        <xsl:value-of select="windowTitle"/>
        <xsl:text>','</xsl:text>
        <xsl:apply-templates select="." mode="features"/>
        <xsl:text>');</xsl:text>
    </xsl:template>
    
    <xsl:template match="popup" mode="theUrl">
        <xsl:choose>
            <xsl:when test="name(..)='internalLink'">
                <xsl:apply-templates select="../internalLink" mode="internalLinkSrc"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="../a/@href"/>				
            </xsl:otherwise>
        </xsl:choose>		
    </xsl:template>
    
    <xsl:template match="popup" mode="features">
        <xsl:text>top=50,left=50,dependant=yes</xsl:text>
        <xsl:text>,width=</xsl:text>
        <xsl:value-of select="width"/>
        <xsl:text>,height=</xsl:text>
        <xsl:value-of select="height"/>
        <xsl:text>,resizable=</xsl:text>
        <xsl:choose>
            <xsl:when test="@resizeable='true'">yes</xsl:when>
            <xsl:otherwise>no</xsl:otherwise>
        </xsl:choose>
        <xsl:text>,scrollbars=</xsl:text>
        <xsl:choose>
            <xsl:when test="@scrollbars='true'">yes</xsl:when>
            <xsl:otherwise>no</xsl:otherwise>
        </xsl:choose>
    </xsl:template>    
    <!-- END popup for internal and external Links, since CQ 2.4.6, Hato, 26.06.2007 -->
    
    
    
</xsl:stylesheet>
