<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--<xsl:include href="secondlevel.xsl"/>-->
    <xsl:import href="../../../../tizzit-site-master/web/navmodules/templates/master.xsl"/>
    
    <xsl:template match="navigation" priority="3">
        <xsl:apply-templates select="viewcomponent/viewcomponent[showType='2' or showType='0']"/>
    </xsl:template>
    
    <xsl:template match="viewcomponent" priority="2">
        <xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType=2 or showType=0])">
            <div id="leftmenue">
                <div id="leftmenue_top">&#160;</div>
                <div id="leftmenue_middle">
                    <xsl:apply-templates select="viewcomponent[showType=2 or showType=0]" mode="first"/>
                </div>
                <div id="leftmenue_bottom">&#160;</div>
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="viewcomponent" mode="first" priority="2">
        <div class="firstlink">
            <xsl:choose>
                <xsl:when test="@onAxisToRoot='true' and @hasChild='true' ">
                    <div class="clicked">
                        <div class="clap">
                            <xsl:apply-templates select="." mode="links"/>
                        </div>                      
                    </div>
                </xsl:when>	
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
            <xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType=2 or showType=0])">			
                <div class="thirdlinks">
                    <xsl:apply-templates select="viewcomponent[showType=2 or showType=0]" mode="third"/>
                </div>
            </xsl:if>
        </div>
    </xsl:template>
    
    
    <xsl:template match="viewcomponent" mode="third" priority="2">
        <div>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="position()=last()">
                        <xsl:text>last-of-thirdlink</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>thirdlink</xsl:text>
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
            <xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='2' or showType='0'])">			
                <xsl:apply-templates select="viewcomponent[showType='2' or showType='0']" mode="last"/>
            </xsl:if>
        </div>
    </xsl:template>
    
    <xsl:template match="viewcomponent" mode="fourth" priority="1">
        <div>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="position()=last()">
                        <xsl:text>last-of-fourthlink</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>fourthlink</xsl:text>
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
            <xsl:if test="@onAxisToRoot='true' and (viewcomponent[showType='2' or showType='0'])">			
                <xsl:apply-templates select="viewcomponent[showType='2' or showType='0']" mode="last"/>
            </xsl:if>
        </div>            
    </xsl:template>
    
    <xsl:template match="viewcomponent[template='disclaimer']" priority="0.5"/>
</xsl:stylesheet>