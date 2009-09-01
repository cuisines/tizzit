<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="flash" mode="format">
		<div class="flash">
		    <embed width="100%" src="{url}" />
		    <div style="text-align:center;"><a href="{url}" target="_blank">Flash Film vergrößern</a></div>
		</div>
	</xsl:template>

</xsl:stylesheet>