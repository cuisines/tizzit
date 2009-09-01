<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

<!--This Template can stand alone or can be included by the teaseraggregation template of the mandant (wich ist mostly the best practice)-->

	<xsl:template match="source" priority="1">
		<xsl:apply-templates select="all/content"/>
	</xsl:template>
	<!--Hauptelemente matchen und arrangieren, s. auch in den entsprechenden includes-->

	<xsl:template match="content">
		<!--<ctmpl:module name="teaser">-->
			<div id="teaser" class="teaser">
				<div class="headline">
					<xsl:value-of select="../../head/title"/>
				</div>
				<div class="teaser2content">
					<xsl:apply-templates mode="format"/>
					<xsl:if test="../internalLink/internalLink/@url!=''">
						<span class="internalLink">
							<xsl:apply-templates select="../internalLink" mode="format"/>
						</span>
					</xsl:if>
				</div>
			</div>
		<!--</ctmpl:module>-->
	</xsl:template>

</xsl:stylesheet>







<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
