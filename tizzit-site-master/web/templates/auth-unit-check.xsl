<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="unitId"/>
	<xsl:param name="unitCheck">unit_<xsl:value-of select="$unitId"/></xsl:param>

	<xsl:template match="@*|node()" priority="-2">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>	

<xsl:template match="authentication">    
	<authentication>
	    <!-- Abfrage, ob es eine role gibt, die mit unit_[unitId] beginnt -->
		<xsl:if test="roles/role[starts-with(.,$unitCheck)]">
			<xsl:apply-templates/>		
		</xsl:if>	    
	</authentication>
</xsl:template>
</xsl:stylesheet>

