<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:template match="footer" mode="footer">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<xsl:apply-templates select="printView"/>
				<xsl:apply-templates select="favorite"/>
				<xsl:apply-templates select="mail-to-a-friend"/>
				<xsl:apply-templates select="pdf"/>
				<xsl:apply-templates select="zoom"/>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="pdf">
		<td width="11">
			<a href="javascript:pdf();" title="als PDF">
				<img src="/cms/img/pdf-icon.gif" border="0" />
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="mail-to-a-friend">
		<td width="11">
			<a href="javascript:mailTo();" title="als Mail">
				<img src="/cms/img/mailto-icon.gif" border="0" />
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="zoom">
		<td width="11">
			<a href="javascript:zoom();" title="Zoom in/out">
				<img src="/cms/img/skalieren-icon.gif" border="0" />
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="favorite">
		<td width="11">
			<a title="zu Favoriten">
				<xsl:attribute name="href">javascript:favorite();</xsl:attribute>
				<img src="/cms/img/favorit-icon.gif" border="0" />
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>

	<xsl:template match="printView">
		<td width="11">
			<a href="javascript:printView();" title="Druckansicht">
				<img src="/cms/img/druckansicht-icon.gif" border="0" />
			</a>
		</td>
		<td width="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\..\tests\footer.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
