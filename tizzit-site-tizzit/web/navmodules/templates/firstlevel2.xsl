<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="../../../../tizzit-site-master/web/navmodules/templates/master.xsl"/>

	<xsl:template match="navigation" priority="1">
		<xsl:if test="viewcomponent/viewcomponent[showType='2']">
					<xsl:apply-templates select="viewcomponent/viewcomponent[showType='2']"/>
		</xsl:if>
	</xsl:template>
    
    <xsl:template match="viewcomponent" priority="3">
        <div class="firstlink">
            
            <xsl:choose>
                <xsl:when test="@id=$viewComponentId">
                    <div class="fl_bg_l">&#160;</div>
                    <div class="flm_bg">
                        <div class="clicked">
                            <div class="actualClicked">
                                <xsl:apply-templates select="." mode="links"/>
                            </div>
                        </div>
                    </div>
                    <div class="fl_bg">&#160;</div>
                </xsl:when>						
                <xsl:when test="@onAxisToRoot='true'">
                    <div class="fl_bg_l">&#160;</div>
                    <div class="flm_bg">
                        <div class="clicked">
                            <xsl:apply-templates select="." mode="links"/>
                        </div>
                    </div>
                    <div class="fl_bg">&#160;</div>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="." mode="links"/>
                </xsl:otherwise>
            </xsl:choose>			
            <!--<xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='3' or showType='0'])">
                <div class="secondlinks">
                    <xsl:apply-templates select="viewcomponent[showType='3' or showType='0']" mode="second"/>
                </div>
            </xsl:if>-->
        </div>
    </xsl:template>

</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
