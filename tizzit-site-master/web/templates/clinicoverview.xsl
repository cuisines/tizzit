<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">

	<xsl:template match="cliniclist" mode="format">
   	    <table width="535" border="0" cellpadding="0" cellspacing="0" class="list-container">
            <xsl:apply-templates select="item" mode="format"></xsl:apply-templates>
        </table>
	</xsl:template>
	
	<xsl:template match="item" mode="format">
	    <tr>
	        <td class="list-headline" id="container-{generate-id(.)}" name="container-{generate-id(.)}">
	            <table border="0" cellpadding="0" cellspacing="0" width="100%">
	                <tr>
	                    <td width="99%"><xsl:value-of select="name"/></td>
	                    <td width="1%" align="right"><a href="javascript:toggle('{generate-id(.)}')">Details</a></td>
	                </tr>
	            </table>
	        </td>
	    </tr>
	    <tr id="{generate-id(.)}" name="{generate-id(.)}" style="display:none;">
	        <td class="list-detail">
	            <table border="0" cellpadding="0" cellspacing="0" width="100%">
	                <tr>
	                    <td>
	                        <xsl:apply-templates select="content" mode="format"/>
	                    </td>
	                </tr>
	                <tr>
	                    <td align="right">
        	                <xsl:apply-templates select="internalLink"/>
	                    </td>
	                </tr>
	            </table>
	        </td>
	    </tr>
	</xsl:template>

    <xsl:template match="aggregation" mode="clinicoverview">
        <xsl:apply-templates select="include" mode="clinicoverview"/>
    </xsl:template>
    
    <xsl:template match="include" mode="clinicoverview">
        <xsl:choose>
            <xsl:when test="include">                
                <xsl:apply-templates select="include" mode="clinicoverview"/>                
            </xsl:when>
            <xsl:otherwise>
                <div>
                    <xsl:apply-templates select="content" mode="clinicoverview"/>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="content" mode="clinicoverview">
        <xsl:if test="normalize-space(salutation) != '' or normalize-space(title) != ''">
            <xsl:if test="salutation"><xsl:value-of select="salutation"/>&#160;</xsl:if>
            <xsl:if test="title"><xsl:value-of select="title"/></xsl:if>
            <br/>
        </xsl:if>
        <xsl:if test="firstname"><xsl:value-of select="firstname"/>&#160;</xsl:if>
        <xsl:if test="lastname"><xsl:value-of select="lastname"/></xsl:if>    
    </xsl:template>
	
	<xsl:template match="internalLink">
	    <a href="/{internalLink/@language}/{internalLink/@url}/page.html" style="color:#395d76;text-decoration: underline;">Link zur Klinik</a>
	</xsl:template>
	
</xsl:stylesheet>