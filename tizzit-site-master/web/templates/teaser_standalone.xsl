<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">


	<xsl:template match="source" priority="1">
		<xsl:apply-templates select="all/content"/>
	</xsl:template>
	<!--Hauptelemente matchen und arrangieren, s. auch in den entsprechenden includes-->


	<xsl:template match="content" priority="1">
		<ctmpl:module name="teaser">
		<div id="teaser" class="teaser">
			<div class="teaserhead">	
				<table>
					<tr>
						<td valign="top">
							<xsl:apply-templates select="../picture/image" mode="teaser"/>
						</td>
						<td valign="bottom">
							<div class="headline">
								<xsl:value-of select="../../head/title"/>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="teasercontent">
				<xsl:apply-templates select="." mode="format"/>
				<xsl:if test="../internalLink/internalLink/@url!=''">
					<span class="internalLink">
						<xsl:apply-templates select="../internalLink" mode="format"/>
					</span>
				</xsl:if>
			</div>
		</div>
		</ctmpl:module>
	</xsl:template>

</xsl:stylesheet>













<!-- Stylus Studio meta-information - (c)1998-2004. Sonic Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\tests\content.xml" htmlbaseurl="" outputurl="" processortype="internal" profilemode="0" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
</metaInformation>
--><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
