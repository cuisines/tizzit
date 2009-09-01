<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="serviceheading" mode="format" priority="1"/>
    <xsl:template match="clinicheading" mode="format" priority="1"/>

    <xsl:template match="serviceheading" mode="servicelist">
        <a name="service"/><div class="subline"><xsl:value-of select="."/></div>
    </xsl:template>

    <xsl:template match="clinicheading" mode="servicelist">
        <a name="clinic"/><div class="subline"><xsl:value-of select="."/></div>
    </xsl:template>

	<xsl:template match="clinicservices" mode="format">
	    <div class="clinicservices-heading">
	        Verweise: <a href="#service"><xsl:value-of select="../serviceheading"/></a>&#160;<a href="#clinic"><xsl:value-of select="../clinicheading"/></a>&#160;<xsl:apply-templates select="internalLink" mode="format"/>
	    </div>
        <xsl:apply-templates select="../serviceheading" mode="servicelist"/>
        <xsl:apply-templates select="../../serviceDefs" mode="servicelist"/>
        <xsl:apply-templates select="../clinicheading" mode="servicelist"/>
        <xsl:apply-templates select="cliniclist" mode="cliniclist"/>
	</xsl:template>
	
	<xsl:template match="cliniclist" mode="cliniclist">
        <div class="clinicservice">
    	    <table border="0" cellpadding="0" cellspacing="0" width="100%">
    	        <xsl:apply-templates select="item" mode="cliniclist"/>
    	    </table>
	    </div>
	</xsl:template>
 
	<xsl:template match="item" mode="cliniclist">
        <tr>
	        <td class="cliniclist-clinic"><xsl:value-of select="name"/></td>
	        <td align="right" class="cliniclist-cliniclinks">
	            <nobr>
    	            <a>
    	                <xsl:attribute name="href">javascript:toggle('<xsl:value-of select="generate-id(.)"/>')</xsl:attribute>
    	                <xsl:text>Leistungsspektrum anzeigen</xsl:text>
    	            </a>&#160;
    	            <a>
    	                <xsl:attribute name="href">/<xsl:value-of select="internalLink/internalLink/@language"/>/<xsl:value-of select="internalLink/internalLink/@url"/>/page.html</xsl:attribute>
    	                <xsl:text>Zur Webseite</xsl:text>
    	            </a>
    	        </nobr>
	        </td>
	        <tr id="{generate-id(.)}" style="display:none;">
	            <td colspan="2" class="cliniclist-items">
                    <xsl:apply-templates select="services" mode="cliniclist"/>
                </td>
            </tr>
	    </tr>
	</xsl:template>

    <xsl:template match="services" mode="cliniclist">
        <xsl:for-each select="./child::*">
            <xsl:sort select="name" order="ascending"/>            
            <xsl:variable name="actService"><xsl:value-of select="name()"/></xsl:variable>
            <xsl:variable name="actSubService">sub<xsl:value-of select="name()"/></xsl:variable>
            <xsl:if test=".!=''">
                <xsl:choose>
                    <xsl:when test="../../subService[@dcfname = $actSubService]/child::* != ''">
                    <div>
        	            <a>
        	                <xsl:attribute name="href">javascript:toggle('<xsl:value-of select="generate-id(.)"/>')</xsl:attribute>
        	                <xsl:value-of select="//serviceDefs/serviceDef[@name=$actService]"/>
        	            </a>
        	            <div id="{generate-id(.)}" style="display:none;" class="subservice">
        	                <xsl:for-each select="../../subService[@dcfname = $actSubService]/child::*">        	                    
                                <xsl:variable name="actSubServiceDefs"><xsl:value-of select="name()"/></xsl:variable>
        	                    <xsl:if test=".!=''">
        	                        <div><xsl:value-of select="//subServiceDefs[@name = $actService]/serviceDef[@name = $actSubServiceDefs]"/></div>
        	                    </xsl:if>
        	                </xsl:for-each>
        	            </div>
    	            </div>
                    </xsl:when>
                    <xsl:otherwise>
                        <div>
                            <xsl:value-of select="//serviceDefs/serviceDef[@name=$actService]"/>
                        </div>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>            
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="serviceDefs" mode="servicelist">
        <div class="clinicservice">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
    	        <xsl:apply-templates select="serviceDef" mode="servicelist"/>
    	    </table>
	    </div>
    </xsl:template>

    <xsl:template match="serviceDef" mode="servicelist">
        <xsl:variable name="serviceName"><xsl:value-of select="@name"/></xsl:variable>
        <xsl:if test="/source/all/content/clinicservices/cliniclist/item/services/child::*[name() = $serviceName] != ''">
            <tr>
    	        <td class="cliniclist-clinic"><xsl:value-of select="."/></td>
    	        <td align="right" class="cliniclist-cliniclinks">
    	            <a>
    	                <xsl:attribute name="href">javascript:toggle('<xsl:value-of select="generate-id(.)"/>')</xsl:attribute>
    	                <xsl:text>Kliniken anzeigen</xsl:text>
    	            </a>
    	        </td>    	        
	        </tr>
	        <tr id="{generate-id(.)}" style="display:none;">	            
	            <td colspan="2" class="cliniclist-items">
	                <xsl:for-each select="/source/all/content/clinicservices/cliniclist/item">
	                    <xsl:sort select="name" order="ascending"/>
                        <xsl:if test="services/child::*[name() = $serviceName] != ''">
                        <a>
                            <xsl:attribute name="href">/<xsl:value-of select="internalLink/internalLink/@language"/>/<xsl:value-of select="internalLink/internalLink/@url"/>/page.html</xsl:attribute>
                            <xsl:value-of select="name"/>
                        </a><br/></xsl:if>
	                </xsl:for-each>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>