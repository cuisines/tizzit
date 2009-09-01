<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="content" mode="include" priority="2">
		<form name="search" method="post" action="" onSubmit="javascript:checkOnSearch();" accept-charset="iso-8859-1">
			<input type="hidden" name="conquest-searchquery" size="11" class="lf"/>
			<input type="hidden" name="conquest-searchquery-is-query" value="true"/>
			<table border="0" cellpadding="6">
				<tr>
					<td>Suchbegriff:</td>
					<td>
						<input type="text" size="30" name="words" value=""/>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="button" onClick="javascript:checkOnSearch();" value="Suche starten"/>
						<br/>
						<br/>
					</td>
				</tr>
				<tr>
					<td>Anzeige:</td>
					<td>
						<select name="format">
							<option value="long">mit Textauszug</option>
							<option value="short">ohne Textauszug</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Sprache wählen:</td>
					<td>
						<select name="s_language">
							<option value="deutsch">deutsch</option>
							<option value="englisch">englisch</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
