<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:template name="flash">
        <xsl:param name="flashvars" select="''"/>
        <xsl:param name="width" select="''"/>
        <xsl:param name="height" select="''"/>        
        <xsl:param name="version" select="'9'"/>      
        <xsl:param name="urlFlashViewer" select="''"/>
        <object id="Flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version={$version},0,0,0"  width="{$width}" height="{$height}" align="middle">
            <param name="allowScriptAccess" value="sameDomain" />
            <param name="movie" value="{$urlFlashViewer}" />
            <param name="menu" value="false" />
            <param name="quality" value="high" />
            <param name="bgcolor" value="#ffffff" />                         
            <param name="FlashVars" value="{$flashvars}"/>                      
            <embed FlashVars="{$flashvars}" src="{$urlFlashViewer}" name="Flash" menu="false" quality="high" bgcolor="#ffffff" width="{$width}" height="{$height}" align="middle" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
        </object>
    </xsl:template>

</xsl:stylesheet>