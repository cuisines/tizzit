<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:template match="source" priority="2">
		<ctmpl:module name="teaseraggregation">
			<xsl:if test="//item">
				<div class="teaseraggregation">
					<xsl:apply-templates select="//item" mode="aggreg"/><!--
					<xsl:apply-templates select="all/iteration/item" mode="aggreg"/>-->
				</div>
			</xsl:if>
		</ctmpl:module>
	</xsl:template>
	<!--Hauptelemente matchen und arrangieren, s. auch in den entsprechenden includes-->

	<xsl:template match="item" mode="aggreg">
		<div class="teaseritem">
			<xsl:apply-templates select="source/all/content"/>
		</div>
	</xsl:template>

</xsl:stylesheet>

