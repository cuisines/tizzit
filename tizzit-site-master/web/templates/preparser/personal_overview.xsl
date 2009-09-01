<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!-- ==============================
        servlet/personalcourselist
    =============================== -->

    <xsl:variable name="userid" select="normalize-space(//lms/authentication/user/@id)"/>

    <xsl:template match="content">
        <content>
            <courselist serverurl="{normalize-space(url2lms)}servlet/personalcourselist" >                
                <xsl:if test="$userid != ''">
                    <param name="userid" value="{$userid}"/>
                </xsl:if>
            </courselist>            
            <xsl:apply-templates/>
        </content>
    </xsl:template>

    <!-- uebernehmen der restlichen html-knoten dem $template -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>	
    </xsl:template>

</xsl:stylesheet>