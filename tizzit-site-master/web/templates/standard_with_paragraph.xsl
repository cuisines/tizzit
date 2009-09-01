<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="standard.xsl"/>

	<xsl:template match="iteration" priority="1">
		<ctmpl:module name="content">
			<div class="content">				
				<xsl:apply-templates mode="para"/>
				<xsl:apply-templates select="../content" mode="include"/>
			</div>
		</ctmpl:module>
	</xsl:template>


	<xsl:template match="item" mode="para">
		<div class="paragraph">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" class="parapicture" width="1%">
						<xsl:apply-templates select="picture" mode="thumbnail"/>
					</td>
					<td valign="top" class="paracontent" width="99%">
						<h1 class="subhead">
							<xsl:apply-templates select="subhead" mode="format"/>
						</h1>
						<xsl:apply-templates select="content" mode="format"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>





<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\tests\content.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
