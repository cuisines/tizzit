<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	
    <xsl:template match="content" mode="format" priority="3">
        <xsl:apply-templates mode="format"/>
        <xsl:if test="../images/item/image-name">
            <div id="myImageFlow" class="imageflow">
                <xsl:apply-templates select="../images/item/image-name"/>
            </div>
            <div class="clear">&#160;</div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="image-name">
        <div class="image">
            <a href="/img/ejbimage/{image/filename}?id={image/@src}" rel="group" id="single_3" title="{image/alttext}">
                <img src="/img/ejbimage/{image/filename}?id={image/@src}" width="130" alt="{image/alttext}" align="left"/>
            </a>
            <div class="clear">&#160;</div>
            <div class="imgTxt"><xsl:value-of select="image/alttext"/></div>
        </div>
    </xsl:template>
	
</xsl:stylesheet>