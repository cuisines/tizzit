<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../../../tizzit-site-master/web/navmodules/templates/breadcrumbs.xsl"/>

    <xsl:template match="navigationBackward" priority="2">
        <xsl:if test="viewcomponent">
            <div class="breadcrumb-items">
                <div class="first-item">
                    <div class="breadcrumb-items">
                        <a href="/">Home</a>
                    </div>
                </div>
                <xsl:apply-templates select="viewcomponent[1]" mode="breadcrumb"/>
            </div>
            <br class="clear"/>
        </xsl:if>
    </xsl:template>
   
    <xsl:template match="viewcomponent" mode="breadcrumb" priority="2">
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
    
    <xsl:template match="viewcomponent" mode="breadcrumblink" priority="2">
        <div class="breadcrumb-item">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="visible='true'">
                        <xsl:text>breadcrumb-item</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>breadcrumb-item-none</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:apply-templates select="." mode="links"/>
        </div>
        <!-- naechste breadcrumb -->
        <xsl:apply-templates select="following-sibling::viewcomponent[1]" mode="breadcrumb"/>    
    </xsl:template>
    
    <xsl:template match="viewcomponent" mode="lastbreadcrumb" priority="2">
        <div class="breadcrumb-item">
            <div class="clicked">
                <xsl:apply-templates select="linkName"/>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>