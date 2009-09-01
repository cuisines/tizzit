<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--try link: http://10.0.0.177/ukw/dcf/dcf.html or other ip or other clientCode-->


	<xsl:param name="clientCode" select="''"/>

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>


	<xsl:template match="availableTemplates">
		<html>
		<head>
		<title>DCF Übersicht</title>
		</head>
		<body>
		<xsl:apply-templates/>
		</body>
		</html>
	</xsl:template>


	<xsl:template match="item">
		<a target="_blank">
			<xsl:attribute name="href">/<xsl:value-of select="$clientCode"/>/dcf/<xsl:value-of select="@filename"/>.xml</xsl:attribute>
			<xsl:value-of select="."/>
		</a>, filename: <xsl:value-of select="@filename"/>.xml
		<xsl:if test="@role">, nur für Rolle: <xsl:value-of select="@role"/></xsl:if>
	<br/>

</xsl:template>

<xsl:template match="bsninclude"></xsl:template>
</xsl:stylesheet>

<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\..\ukw\dcf\dcf.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
