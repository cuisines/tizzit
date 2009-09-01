<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:template match="source" priority="1">
		<xsl:apply-templates select="all/content"/>
	</xsl:template>
	<!--Hauptelemente matchen und arrangieren, s. auch in den entsprechenden includes-->


	<xsl:template match="content" priority="1">
	<!--<ctmpl:module name="teaser">-->
		<div class="teaser">			
			<xsl:apply-templates select="../picture/image" mode="teaser"/>			
			<div class="teasercontent">	
			<div class="teaserheadline">
				<xsl:value-of select="../../head/title"/>
			</div>
				<xsl:apply-templates select="." mode="format"/>
				<xsl:apply-templates select="../internalLink" mode="teaser"/>
			</div>
		</div>
		<!--</ctmpl:module>-->
	</xsl:template>


	<xsl:template match="internalLink" mode="teaser">
		<div class="internalLink">
			<xsl:apply-templates select="." mode="format"/>
		</div>
	</xsl:template>

</xsl:stylesheet>












<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\tests\content.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
