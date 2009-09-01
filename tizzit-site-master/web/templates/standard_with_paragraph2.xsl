<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="standard.xsl"/>

	<xsl:template match="iteration" priority="0.5">
		<ctmpl:module name="content">
			<div class="content">				
				<xsl:apply-templates select="../content | ." mode="includeBefore"/>
				<xsl:apply-templates mode="para"/>
				<xsl:apply-templates select="../content | ." mode="include"/>
			</div>
		</ctmpl:module>
	</xsl:template>


	<xsl:template match="item" mode="para">
		<div class="paragraph">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top" class="paracontent">
						<h1 class="subhead">
							<xsl:apply-templates select="subhead" mode="format"/>
						</h1>
						<div>
							<xsl:apply-templates select="content" mode="format"/>
						</div>
					</td>
					<td valign="top" class="parapicture" align="right">
						<xsl:apply-templates select="picture" mode="thumbnail"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

<!-- Leerausgabe -->
	<xsl:template match="iteration | content" mode="includeBefore" priority="-1"/>

</xsl:stylesheet>
