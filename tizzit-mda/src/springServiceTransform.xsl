<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:UML="omg.org/UML/1.4"
    version="1.0">

    <xsl:template match="@*|node()" priority="-2">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template
        match="UML:Class[UML:ModelElement.stereotype/UML:Stereotype/XMI.extension/referentPath/attribute::xmi.value= 'org.andromda.profile::service::Service']">
        <UML:Class>
            <xsl:attribute name="name"><xsl:value-of select="attribute::name"/>Spring</xsl:attribute>
            <xsl:attribute name="xmi.id"><xsl:value-of select="attribute::xmi.id"/></xsl:attribute>
            <xsl:apply-templates select="child::*"/>
        </UML:Class>
    </xsl:template>
    
	<xsl:template
		match="UML:Class[UML:ModelElement.stereotype/UML:Stereotype/XMI.extension/referentPath/attribute::xmi.value= 'org.andromda.profile::persistence::Entity']">
		<UML:Class>
			<xsl:attribute name="name"><xsl:value-of select="attribute::name"/>Hbm</xsl:attribute>
			<xsl:attribute name="xmi.id"><xsl:value-of select="attribute::xmi.id"/></xsl:attribute>
			<xsl:apply-templates select="child::*"/>
		</UML:Class>
	</xsl:template>
</xsl:stylesheet>
