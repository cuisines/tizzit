<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:include href="variables.xsl"/>

	<xsl:include href="includes/links.xsl"/>

	<xsl:template match="/">
		<modules>
			<ctmpl:module>
				<xsl:attribute name="name">
					<xsl:value-of select="name(child::*)"/>
				</xsl:attribute>
				<div>
					<xsl:attribute name="class">
						<xsl:value-of select="name(child::*)"/>
					</xsl:attribute>
					<xsl:apply-templates />
				</div>
			</ctmpl:module>
		</modules>
	</xsl:template> 
 

	<xsl:template match="navigation">
		<xsl:apply-templates select="viewcomponent/viewcomponent[showType='3' or showType='0']"/>
	</xsl:template>

 
	<xsl:template match="viewcomponent">
		<div class="firstlink">
			<xsl:choose>
			<xsl:when test="@id=$viewComponentId">
				<div class="clicked">
					<div class="actualClicked">
						<xsl:apply-templates select="." mode="links"/>
					</div>
				</div>
			</xsl:when>						
			<xsl:when test="@onAxisToRoot='true'">
				<div class="clicked">
					<xsl:apply-templates select="." mode="links"/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="links"/>
			</xsl:otherwise>
			</xsl:choose>			
			<xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='3' or showType='0'])">
			    <div class="secondlinks">
				    <xsl:apply-templates select="viewcomponent[showType='3' or showType='0']" mode="second"/>
			    </div>
			</xsl:if>			
		</div>
	</xsl:template>


	<xsl:template match="viewcomponent" mode="second">
		<div class="secondlink">
			<xsl:choose>
			<xsl:when test="@id=$viewComponentId">
				<div class="clicked">
					<div class="actualClicked">
						<xsl:apply-templates select="." mode="links"/>
					</div>
				</div>
			</xsl:when>			
			<!--<xsl:when test="descendant::viewcomponent/@id=$viewComponentId">-->
			<xsl:when test="@onAxisToRoot='true'">			
				<div class="clicked">
					<xsl:apply-templates select="." mode="links"/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="links"/>
			</xsl:otherwise>
			</xsl:choose>
			<!--<xsl:if test="@id=$viewComponentId or descendant::viewcomponent/@id=$viewComponentId">-->
			<xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='3' or showType='0'])">			
			    <div class="thirdlinks">
				    <xsl:apply-templates select="viewcomponent[showType='3' or showType='0']" mode="third"/>
			    </div>
			</xsl:if>
		</div>
	</xsl:template>


	<xsl:template match="viewcomponent" mode="third">
		<div class="thirdlink">
			<xsl:choose>		
			<xsl:when test="@id=$viewComponentId">
				<div class="clicked">
					<div class="actualClicked">
						<xsl:apply-templates select="." mode="links"/>
					</div>
				</div>
			</xsl:when>			
			<!--<xsl:when test="descendant::viewcomponent/@id=$viewComponentId">-->
			<xsl:when test="@onAxisToRoot='true'">
				<div class="clicked">
					<xsl:apply-templates select="." mode="links"/>
				</div>
			</xsl:when>	
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="links"/>
			</xsl:otherwise>
			</xsl:choose>
			<!--<xsl:if test="@id=$viewComponentId or descendant::viewcomponent/@id=$viewComponentId">-->
			<xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='3' or showType='0'])">			
				<xsl:apply-templates select="viewcomponent[showType='3' or showType='0']" mode="last"/>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template match="viewcomponent" mode="last">
		<div class="lastlink">
			<xsl:choose>			
			<xsl:when test="descendant::viewcomponent/@id=$viewComponentId">
				<div class="clicked">
					<xsl:apply-templates select="." mode="links"/>
				</div>
			</xsl:when>
			<xsl:when test="@id=$viewComponentId">
				<div class="clicked">
					<div class="actualClicked">
						<xsl:apply-templates select="." mode="links"/>
					</div>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="links"/>
			</xsl:otherwise>
			</xsl:choose>
            <xsl:if test="@onAxisToRoot='true' and viewcomponent">
				<xsl:apply-templates select="viewcomponent" mode="rest"/>
			</xsl:if>
		</div>
	</xsl:template>
	
	<!-- Seiten mit bestimmten templates grundsaetzlich von der Darstellung ausschliessen -->
	<xsl:template match="viewcomponent[template='unitteaseraggregation']" priority="1.1"/>
	<xsl:template match="viewcomponent[template='unitteaseraggregation']" mode="second" priority="1"/>
	<xsl:template match="viewcomponent[template='unitteaseraggregation']" mode="third" priority="1"/>
	<xsl:template match="viewcomponent[template='unitteaseraggregation']" mode="last" priority="1"/>
	<xsl:template match="viewcomponent[template='teaseraggregation']" priority="1.1"/>
	<xsl:template match="viewcomponent[template='teaseraggregation']" mode="second" priority="1"/>
	<xsl:template match="viewcomponent[template='teaseraggregation']" mode="third" priority="1"/>
	<xsl:template match="viewcomponent[template='teaseraggregation']" mode="last" priority="1"/>

</xsl:stylesheet>