<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:include href="variables.xsl"/>
    <xsl:include href="../../templates/html/include/flash.xsl"/>
    <xsl:include href="../../templates/html/include/tree2.xsl"/>
    
    <!-- statische Flashviewer URLs -->
    <xsl:variable name="urlScreenPreviewPresenter" select="'/flash/ScreenViewer.swf'"/>
    <xsl:variable name="urlLessonPreviewPresenter" select="'/flash/LessonViewer.swf'"/>
    <xsl:variable name="urlModulePreviewPresenter" select="'/flash/ModuleViewer8.swf'"/>
    
    
    <!-- richtigen flashviewer anhand des templates auswaehlen, ('module', 'lessen', uebrige - ggfs. zu ergaenzen-->
    <xsl:variable name="urlFlashViewer">
        <xsl:choose>
            <xsl:when test="starts-with($template,'module')">
                <xsl:value-of select="$urlModulePreviewPresenter"/>
            </xsl:when>
            <xsl:when test="starts-with($template,'lesson') or starts-with($template,'pointManagement')">
                <xsl:value-of select="$urlLessonPreviewPresenter"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$urlScreenPreviewPresenter"/>
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:variable>
    
    <!-- flashconfig laden von letzter Modulseite (lastNavigationRoot) -->
    <xsl:variable name="flashConfig" select="document(concat('cocoon://',$language,'/',//flashobject8/navigation/viewcomponent/url,'/content.xml'))/source/all/flashconfig"/>
    <xsl:variable name="moduleIdentifier" select="document(concat('cocoon://',$language,'/',//flashobject8/navigation/viewcomponent/url,'/content.xml'))/source/all/content/identifier"/>
    
    <!-- Breite und Hoehe aus flashconfig -->
    <xsl:variable name="width" select="$flashConfig/width"/>
    <xsl:variable name="height" select="$flashConfig/height"/>
    
    
    
    
    <!-- NOTE: SAXON or other XSLT2 Parser required for this! -->
    <!-- Flashvariablen zusammenstellen -->
    <xsl:variable name="flashvartree">
        <params>
            <param name="serverUrl" value="{concat('http://',$fullServerName,'/')}"/>
            <param name="contentUrl" value="{concat('http://',$fullServerName,'/',$language,'/',$url,'/content.scormpreview')}"/>
            <param name="layout" value="{$flashConfig/layout/value}"/>
            <param name="identifier" value="{$moduleIdentifier}" />
        </params>
    </xsl:variable>  
    <xsl:variable name="flashvars">
        <xsl:call-template name="tree2flashvars">
            <xsl:with-param name="tree" select="$flashvartree"/>
        </xsl:call-template>
    </xsl:variable>
    
    <xsl:template match="/">
        <modules>
            <!-- DEBUG Ausgaben als Kommentar in Modul -->
            <xsl:comment>
                URL zum config laden:
                <xsl:value-of select="concat('cocoon://',$language,'/',//flashobject8/navigation/viewcomponent/url,'/content.xml')"/>               
                Loaded moduleconfig:
                <xsl:value-of select="concat('cocoon://',$language,'/',//flashobject8/navigation/viewcomponent/url,'/content.xml')"/>                
                flashvars:      
                <xsl:value-of select="$flashvars"/>    
            </xsl:comment>
            <!-- Modul -->
            <ctmpl:module>
                <xsl:attribute name="name">
                    <xsl:value-of select="name(child::*)"/>
                </xsl:attribute>
                <div>
                    <xsl:attribute name="class">
                        <xsl:value-of select="name(child::*)"/>
                    </xsl:attribute>   
                    <object id="Flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"  width="{$width}" height="{$height}" align="middle">
                        <param name="allowScriptAccess" value="sameDomain" />
                        <param name="movie" value="{$urlFlashViewer}" />
                        <param name="menu" value="false" />
                        <param name="quality" value="best" />
                        <param name="bgcolor" value="#ffffff" />                         
                        <param name="FlashVars" value="{$flashvars}"/>                      
                        <embed FlashVars="{$flashvars}" src="{$urlFlashViewer}" name="Flash" menu="false" quality="high" bgcolor="#ffffff" width="{$width}" height="{$height}" align="middle" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
                    </object>
                </div>             
            </ctmpl:module>
            <ctmpl:module name="flashobject8_merckserono">
                <script type="text/javascript">
                    
                    <xsl:variable name="urlFlashViewer">/flash/ModuleViewer8.swf</xsl:variable>
                    <xsl:variable name="flashvars">
                        <xsl:text>serverUrl:'http://</xsl:text>
                        <xsl:value-of select="$serverName"/>
                        <xsl:text>/',contentUrl:'/en/courses/</xsl:text>
                        <xsl:value-of select="$moduleIdentifier"/>
                        <xsl:text>/content.scormpreview',</xsl:text>
                        <xsl:text>identifier:'</xsl:text>
                        <xsl:value-of select="$moduleIdentifier"/>
                        <xsl:text>',layout:'</xsl:text>
                        <xsl:value-of select="$flashConfig/layout/value"/>
                        <xsl:text>',initAfterOnload:'true'</xsl:text>
                    </xsl:variable>
                    <xsl:variable name="width">1000</xsl:variable>
                    <xsl:variable name="height">700</xsl:variable>
                    <![CDATA[
					var flashvars = {]]><xsl:value-of select="$flashvars"/><![CDATA[};			
					var params = {bgcolor:"#ffffff"};   
					var attributes = {};
					
					swfobject.embedSWF("]]><xsl:value-of select="$urlFlashViewer"/><![CDATA[", "myFlash", "]]><xsl:value-of select="$width"/><![CDATA[", "]]><xsl:value-of select="$height"/><![CDATA[", "8.0.0", "expressInstall.swf", flashvars, params, attributes);  
					
				    function initFlashFunctions() {
				        var obj = swfobject.getObjectById("myFlash");
					    obj.initFlash();
					    obj.initFlash2();
				    }
					
					]]>
                </script> 
                <div id="myFlash">&#160;</div>
            </ctmpl:module>
        </modules>
    </xsl:template>
    
</xsl:stylesheet>