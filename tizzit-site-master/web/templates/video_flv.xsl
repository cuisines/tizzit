<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
         
    <xsl:variable name="videoUrl" select="normalize-space(//video_url)"/>
    <xsl:variable name="videoPlayer" select="'/httpd/flash/videoplattform.swf'"/>
    <xsl:variable name="flashVars" select="concat('videoUrl=',$videoUrl)"/>
    <xsl:variable name="width" select="normalize-space(//width_video)"/>
    <xsl:variable name="height" select="normalize-space(//height_video)"/>
    
    <xsl:template match="content" priority="2" mode="include-before">
        <div class="videoplayer">
            <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="100%" height="100%" id="videoplattform" align="middle">
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="movie" value="{$videoPlayer}" />                
                <param name="quality" value="high" />
                <param name="bgcolor" value="#ffffff" />
                <param name="FlashVars" value="{$flashVars}" />                
                <embed src="{$videoPlayer}" quality="high" bgcolor="#ffffff" width="100%" height="100%" name="videoplattform" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" FlashVars="{$flashVars}"/>
            </object>
        </div>
    </xsl:template>
    
    <xsl:template match="width_video | height_video | jumppoints | video_url" mode="format" priority="1.2"/>
    
</xsl:stylesheet>