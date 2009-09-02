<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	
    <xsl:template match="content" mode="include-after" priority="2">
        <script type="text/javascript" src="/httpd/js/mootools.js"><![CDATA[&#160;]]></script>
        <script type="text/javascript" src="/httpd/js/slideshow.js"><![CDATA[&#160;]]></script>
        <script type="text/javascript" src="/httpd/js/slideshow.push.js"><![CDATA[&#160;]]></script>
        <script type="text/javascript">		
            <![CDATA[
	            window.addEvent('domready', function(){
	            var data = {
            ]]>
	        
            <xsl:if test="../images/item/image-name/image/filename!=''">
                <xsl:apply-templates select="../images/item"/>
	        </xsl:if>
            
        	<![CDATA[
	            };
	            var myShow = new Slideshow.Push('show', data, { captions: false, controller: true, delay: 5000, duration: 2500, height: 400, hu: '/img/ejbimage/', transition: 'elastic:out', width: 500 });
	          });
	        ]]>
        </script>
        <div class="clear">&#160;</div>
        <div id="show" class="slideshow">
            <xsl:if test="../images/item/image-name/image/filename!=''">
                <img src="/img/ejbimage/{../images/item[1]/image-name/image/filename}?id={../images/item[1]/image-name/image/@src}" alt="" />
            </xsl:if>
            <div id="p-top-r">&#160;</div>
            <div id="p-top-l">&#160;</div>
        </div>
        <div class="clear">&#160;</div>
    </xsl:template>
    
    <xsl:template match="item">
        <xsl:text>'</xsl:text>
        <xsl:value-of select="image-name/image/filename"/>
        <xsl:text>?id=</xsl:text>
        <xsl:value-of select="image-name/image/@src"></xsl:value-of>
        <xsl:text>': { caption: '</xsl:text>
        <xsl:value-of select="image-name/image/alttext"/>
        <xsl:text>' }</xsl:text>
        <xsl:if test="position()!=last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
	
</xsl:stylesheet>