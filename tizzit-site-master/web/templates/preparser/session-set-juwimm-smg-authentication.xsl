<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:core-smg="http://www.juwimm.com/core-smg/v1"
    xmlns:session="http://apache.org/cocoon/session/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="loginname" select="''"/>
    <xsl:param name="siteId" select="''"/>

    <xsl:template match="ID">
        <ID>
            <session:createcontext name="lms"/>
            <session:setxml context="lms" path="/">
                <lms>
                    <authentication>
                        <!-- core-smg:person kann alle Personen aus der DB lesen, egal ob ADM oder Arzt -->
                        <core-smg:person>
                            <xsl:attribute name="siteId" select="$siteId"/>
                            <xsl:attribute name=" userName" select="$loginname"/>
                        </core-smg:person>
                        <!-- core-smg:responsibleEmployee holt den zugeortneten ADM aus SMG -->
                        <core-smg:responsibleEmployee>
                            <xsl:attribute name="siteId" select="$siteId"/>
                            <xsl:attribute name=" userName" select="$loginname"/>
                        </core-smg:responsibleEmployee>
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
