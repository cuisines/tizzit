<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="master.xsl"/>
	
	<xsl:template match="navigation" priority="1.5">
		<xsl:if test="viewcomponent/viewcomponent[showType='0']">
			<xsl:apply-templates select="viewcomponent/viewcomponent[showType='0']"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="/" priority="1.1">
		<modules>
			<ctmpl:module>
				<xsl:attribute name="name">
					<xsl:value-of select="name(child::*)"/>
				</xsl:attribute>
				<xsl:choose>
					<xsl:when test="leftmenue/navigation/viewcomponent/viewcomponent[showType='0']">
						<div>
							<xsl:attribute name="class">
								<xsl:value-of select="name(child::*)"/>
							</xsl:attribute>
							<xsl:apply-templates/>
						</div>
					</xsl:when>
					<xsl:otherwise><div>&#160;</div></xsl:otherwise>
				</xsl:choose>
			</ctmpl:module>
		</modules>
	</xsl:template>

</xsl:stylesheet>