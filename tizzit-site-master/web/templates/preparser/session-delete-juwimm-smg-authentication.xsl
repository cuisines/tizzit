<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:core-smg="http://www.juwimm.com/core-smg/v1"
    xmlns:session="http://apache.org/cocoon/session/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="loginname" select="''"/>
    <xsl:param name="siteId" select="''"/>
    <xsl:param name="clean" select="''"/>

    <xsl:template match="ID">
        <ID>
            <session:deletecontext name="lms"/>
            <createcontext name="lms"/>
            <removexml context="lms" path="/"/>
           <!-- <session:setxml context="lms" path="/">
                
            </session:setxml>-->
            <xsl:apply-templates/>
        </ID>
    </xsl:template>


    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
