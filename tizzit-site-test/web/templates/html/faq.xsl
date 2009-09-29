<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="common.xsl"/>
    <xsl:include href="../../../../tizzit-site-master/web/templates/faq.xsl"/>
    
    <xsl:template match="content" mode="format" priority="1">
        <xsl:apply-templates select="textbefore" mode="format"/>
        <xsl:apply-templates select="category"/>
    </xsl:template>
    
    <xsl:template match="category" priority="1">
        <xsl:if test="../faq/item/question!=''">
            <xsl:for-each select="../faq/item">
                <div class="faq_item">
                    <a href="javascript:toggle('{generate-id(.)}')">
                        <h2><xsl:value-of select="question"/></h2>
                    </a>
                    <div style="display:none;" id="{generate-id(.)}">
                        <div class="answer">
                            <xsl:apply-templates select="answer" mode="format"/>
                        </div>
                    </div>
                </div>
                <xsl:if test="position()!=last()">
                    <hr/>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="textbefore" mode="format">
        <xsl:apply-templates mode="format"/>
    </xsl:template>
    
</xsl:stylesheet>