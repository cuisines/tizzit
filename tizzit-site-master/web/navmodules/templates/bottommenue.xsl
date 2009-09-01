<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:include href="master.xsl"/>
    
    <xsl:template match="navigation" priority="1.5">
        <xsl:if test="viewcomponent/viewcomponent[showType='1']">
            <xsl:apply-templates select="viewcomponent/viewcomponent[showType='1']"/>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template match="viewcomponent" priority="1.1">
        <div class="">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="following-sibling::viewcomponent[showType='1']">firstlink</xsl:when>
                    <!--Dem letzten link eine andere Klasse zuweisen, falls Trennstriche zwischen den Links sind, die beim letzten nicht erscheinen sollen-->
                    <xsl:otherwise>last-of-firstlink</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>			
            <xsl:apply-templates select="." mode="clickdecision"/>				
            <!--<xsl:apply-templates select="viewcomponent" mode="second"/>-->
            <xsl:if test="@onAxisToRoot='true'">
                <xsl:apply-templates select="viewcomponent[showType='1' or showType='0']" mode="second"/>				
            </xsl:if>	
        </div>
    </xsl:template>
    
    <!--geaendert auf  @onAxisToRoot von Hato, -->
    <xsl:template match="viewcomponent" mode="clickdecision">
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
    </xsl:template>
</xsl:stylesheet>