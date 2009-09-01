<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
     <xsl:template match="content" mode="format">
        <xsl:apply-templates select="." mode="include"/>
	    <xsl:apply-templates select="category"/>
     </xsl:template>
     
     <xsl:template match="category">
         <h3><xsl:value-of select="."/></h3>
             <xsl:for-each select="../faq/item">
                <p>
                <a href="javascript:toggle('{generate-id(.)}')"><b><xsl:value-of select="question"/></b></a><br/>
                <div style="display: none;" id="{generate-id(.)}">
                    <ul><xsl:apply-templates select="answer" mode="format"/></ul>
                </div>
                </p>
             </xsl:for-each>
     </xsl:template>

</xsl:stylesheet>