<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
   
 
    <xsl:include href="../teaseraggregation.xsl"/>

    <xsl:template match="source" priority="2.1">
        <ctmpl:module name="unitteaser">
            <xsl:if test="//item">
                <div class="teaseraggregation">
                    <xsl:apply-templates select="//item" mode="aggreg"/>
                </div>
            </xsl:if>
        </ctmpl:module>
    </xsl:template>
       
</xsl:stylesheet>
