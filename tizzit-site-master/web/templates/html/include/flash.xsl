<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="flashObject">
        <xsl:param name="width"/>
        <xsl:param name="height"/>
        <xsl:param name="url"/>
        <xsl:param name="flashvars"/>
        
        <object id="Flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"  width="{$width}" height="{$height}" align="middle">
            <param name="allowScriptAccess" value="sameDomain" />
            <param name="movie" value="{$url}" />
            <param name="menu" value="false" />
            <param name="quality" value="high" />
            <param name="bgcolor" value="#ffffff" />
            <param name="FlashVars" value="{$flashvars}"/>
            <embed FlashVars="{$flashvars}" src="{$url}" name="Flash" menu="false" quality="high" bgcolor="#ffffff" width="{$width}" height="{$height}" align="middle" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
        </object>
        
    </xsl:template>

</xsl:stylesheet>