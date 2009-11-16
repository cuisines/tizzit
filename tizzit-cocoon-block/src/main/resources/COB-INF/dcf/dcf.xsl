<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="siteShort" select="''" />
	<xsl:param name="languageCode" select="'en'" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:variable name="title">
		<xsl:text>DCF overview</xsl:text>
		<xsl:if test="$languageCode != ''">
			<xsl:text> (</xsl:text>
			<xsl:value-of select="$languageCode" />
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:variable>

	<xsl:template match="availableTemplates">
		<html>
			<head>
				<title>
					<xsl:value-of select="$title" />
				</title>
				<style type="text/css">
					<![CDATA[
					table, td, tr, fieldset {
						border: 1px solid #000;
						border-collapse: collapse;
					}
					table {
						margin: 5px 0 15px 0;
					}
					td {
						padding: 5px;
					}
					legend {
						font-size: 20px;
						font-weight: bold;
					}
					.warn {
						padding: 15px;
						background-color: #FFFFCE;
					}
				]]>
				</style>
			</head>
			<body>
				<fieldset>
					<legend>
						<xsl:value-of select="$title" />
					</legend>
					<xsl:if test="$languageCode = ''">
						<p class="warn">Missing 'lang' parameter!</p>
					</xsl:if>
					<ul>
						<xsl:apply-templates />
					</ul>
				</fieldset>
			</body>
		</html>
	</xsl:template>


	<xsl:template match="item">
		<li>
			<a target="_blank">
				<xsl:attribute name="href">
					<xsl:text>/</xsl:text>
					<xsl:value-of select="$siteShort" />
					<xsl:text>/dcf/</xsl:text>
					<xsl:value-of select="@filename" />
					<xsl:text>.xml?lang=</xsl:text>
					<xsl:value-of select="$languageCode" />
				</xsl:attribute>
				<xsl:value-of select="." />
			</a>
			<table>
				<tr>
					<th colspan="2">Attributes</th>
				</tr>
				<tr>
					<th>Name</th>
					<th>Value</th>
				</tr>
				<xsl:for-each select="@*">
					<tr>
						<td>
							<xsl:value-of select="name()" />
						</td>
						<td>
							<xsl:value-of select="." />
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</li>
	</xsl:template>

</xsl:stylesheet>
