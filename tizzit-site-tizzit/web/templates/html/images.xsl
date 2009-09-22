<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	
    <xsl:template match="content" mode="format" priority="3">
        <xsl:apply-templates mode="format"/>
        <xsl:if test="../images/item/image-name">
            <script language="JavaScript" src="/httpd/js/imageflow.js" type="text/JavaScript"><![CDATA[&#160;]]></script>
            <div id="myImageFlow" class="imageflow">
                <xsl:apply-templates select="../images/item/image-name"/>
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="image-name">
        <img src="/img/ejbimage/{image/filename}?id={image/@src}" alt="{image/alttext}" longdesc="/img/ejbimage/{image/filename}?id={image/@src}" />
    </xsl:template>
	
</xsl:stylesheet>