<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">



<!-- line anfang  -->
<xsl:template match="zeile" mode="format">


<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td>
<xsl:apply-templates select="datum" mode="datum"/>
<img src="/cms/img/spacer.gif" width="19" height="19"/>
<xsl:apply-templates select="inhalt" mode="text"/>
</td>
</tr>
</table>


</xsl:template>
<!-- ende line -->




<xsl:template match="datum" mode="datum">
<xsl:value-of select="."/>
</xsl:template>

<xsl:template match="inhalt" mode="text">
<xsl:value-of select="."/>
</xsl:template>

</xsl:stylesheet>









<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\..\master2.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
