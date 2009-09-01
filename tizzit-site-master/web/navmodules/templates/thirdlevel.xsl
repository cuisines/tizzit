<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="master.xsl"/>

	<xsl:template match="navigation" priority="0.75">
		<xsl:if test="viewcomponent/viewcomponent">
			<xsl:apply-templates select="viewcomponent/viewcomponent"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="viewcomponent" priority="0.75">
		<div>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="following-sibling::viewcomponent[1]">firstlink</xsl:when>
					<!--Dem letzten link eine andere Klasse zuweisen, falls Trennstriche zwischen den Links sind-->
					<xsl:otherwise>last-of-firstlink</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="." mode="links"/>
			<xsl:apply-templates select="viewcomponent" mode="second"/>
		</div>
	</xsl:template>
</xsl:stylesheet>

