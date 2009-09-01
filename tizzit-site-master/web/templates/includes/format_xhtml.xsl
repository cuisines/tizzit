<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- hier werden die formatanweiseungen innerhalb des Fliesstextes ausgefuehrhrt-->

	<xsl:template match="a[@type='anchor']" mode="format" priority="1.1">
		<!-- Schreibt das Attribut id in das zuletzt geschrieben Tag -->
		<div class="hidden_anchor">
			<xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute> &#160;
		</div>
	</xsl:template>

</xsl:stylesheet>