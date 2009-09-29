<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:import href="../../../../tizzit-site-master/web/navmodules/templates/master.xsl"/>
    
    <xsl:template match="viewcomponent" priority="3">
        <div>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="position()!=1">
                        <xsl:text>firstlink</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>first-of-firstlink</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
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
    
</xsl:stylesheet>