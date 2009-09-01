<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="master.xsl"/>

	<xsl:template match="navigationBackward" priority="1">
		<xsl:if test="viewcomponent">
			<div class="breadcrumb-items">
				<xsl:apply-templates select="viewcomponent[1]" mode="breadcrumb"/>
			</div>
		</xsl:if>
	</xsl:template>


	<xsl:template match="viewcomponent" mode="breadcrumb">
		<xsl:choose>
			<!--Wenn noch mehr als ein item folgt, Link schreiben-->
			<xsl:when test="following-sibling::viewcomponent[1]">
				<xsl:apply-templates select="." mode="breadcrumblink"/>
			</xsl:when>
			<!--Wenn nur noch ein item folgt, nur Linkname schreiben-->
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="lastbreadcrumb"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
    
    <xsl:template match="viewcomponent" mode="breadcrumblink">
    		<div class="breadcrumb-item">
    			<xsl:apply-templates select="." mode="links"/>
    		</div>
    		<!-- naechste breadcrumb -->
            <xsl:apply-templates select="following-sibling::viewcomponent[1]" mode="breadcrumb"/>    
    </xsl:template>

	<xsl:template match="viewcomponent" mode="lastbreadcrumb">
		<div class="breadcrumb-item">
			<div class="clicked">
				<xsl:apply-templates select="linkName"/>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>