<?xml version='1.0'?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:session="http://apache.org/cocoon/session/1.0">

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="authentication">
        <authentication>
            <xsl:apply-templates/>
        </authentication>
    </xsl:template>

    <xsl:template match="ID">
        <ID>
            <session:setxml context="lms" path="/">
                <lms>
                    <authentication>
                        <user id="{normalize-space(.)}"/>
                    </authentication>
                </lms>
            </session:setxml>
            <xsl:apply-templates/>
        </ID>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>	
    </xsl:template>

</xsl:stylesheet>