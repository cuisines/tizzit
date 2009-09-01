<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fi="http://apache.org/cocoon/forms/1.0#instance"
    xmlns:fb="http://apache.org/cocoon/forms/1.0#binding"  xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
    <xsl:template match="fd:form">
        <fb:context xmlns:fb="http://apache.org/cocoon/forms/1.0#binding"
            xmlns:fd="http://apache.org/cocoon/forms/1.0#definition" path="result">
            <xsl:apply-templates/>
        </fb:context>
    </xsl:template>
    <xsl:template match="fd:field">
        <fb:value>
            <xsl:attribute name="id">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <xsl:attribute name="path">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <xsl:if test="./fd:datatype/@base != ''">
                <fd:convertor>
                    <xsl:attribute name="datatype">
                        <xsl:value-of select="./fd:datatype/@base"/>
                    </xsl:attribute>
                </fd:convertor>
            </xsl:if>
        </fb:value>
    </xsl:template>
    
   
    <xsl:template match="fd:booleanfield">
        <fb:value>
            <xsl:attribute name="id">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <xsl:attribute name="path">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <fd:convertor datatype="boolean"/>
        </fb:value>
    </xsl:template>
</xsl:stylesheet>
