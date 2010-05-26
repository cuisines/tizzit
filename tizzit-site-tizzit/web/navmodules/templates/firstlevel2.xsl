<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="../../../../tizzit-site-master/web/navmodules/templates/master.xsl"/>

	<xsl:template match="navigation" priority="1">
		<xsl:if test="viewcomponent/viewcomponent[showType='2']">
		    <ul class="sf-menu">
	            <xsl:apply-templates select="viewcomponent/viewcomponent[showType='2']"/>
		    </ul>
		    <div class="clear">&#160;</div>
		</xsl:if>
	</xsl:template>
    
    <xsl:template match="viewcomponent" priority="3">
        <li>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="position()!=last()">
                        <xsl:text>firstlink</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>last-of-firstlink</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="@id=$viewComponentId">
                    <div class="fl_bg_l">&#160;</div>
                    <div class="flm_bg">
                        <div class="clicked">
                            <div class="actualClicked">
                                <xsl:value-of select="linkName"/>
                            </div>
                        </div>
                    </div>
                    <div class="fl_bg">&#160;</div>
                </xsl:when>						
                <xsl:when test="@onAxisToRoot='true'">
                    <div class="fl_bg_l">&#160;</div>
                    <div class="flm_bg">
                        <div class="clicked">
                            <xsl:value-of select="linkName"/>
                        </div>
                    </div>
                    <div class="fl_bg">&#160;</div>
                </xsl:when>
                <xsl:otherwise>
                    <div class="link">
                        <xsl:value-of select="linkName"/>
                    </div>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="@hasChild='true' and (viewcomponent[showType='2' or showType='0'])">
                <ul class="secondlinks">
                    <xsl:apply-templates select="viewcomponent[showType='2' or showType='0']" mode="second"/>
                </ul>
            </xsl:if>
        </li>
    </xsl:template>
    
    <xsl:template match="viewcomponent" priority="3" mode="second">
        <li>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="position()!=last()">
                        <xsl:text>secondlink</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>last-of-secondlink</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="@id=$viewComponentId">
                    <div class="actualClicked">
                        <xsl:apply-templates select="." mode="links"/>
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
        </li>
       <xsl:if test="position()=last()">
            <div class="last-item">&#160;</div>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>