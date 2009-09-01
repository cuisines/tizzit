<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- ==============================
        servlet/courselist
        =============================== -->

    <xsl:param name="ta" select="'all'"/>
    <xsl:param name="country" select="'all'"/>


    <xsl:template match="content">
        <content>
            <xsl:if test="normalize-space($ta) != '' and normalize-space($country) != ''">
                <courselist serverurl="{normalize-space(url2lms)}servlet/courselist" >
                    <xsl:if test="$country != 'all'">
                        <param name="country" value="{$country}" />
                    </xsl:if>
                    <xsl:if test="$ta != 'all'">
                        <param name="ta" value="{$ta}" />
                    </xsl:if>
                </courselist>
            </xsl:if>
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