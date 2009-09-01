<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:ctmpl="http://www.conquest-cms.net/template"
  xmlns:error="http://apache.org/cocoon/error/2.1">

    <xsl:param name="statusCode" select="'0'"/>
    <xsl:param name="liveserver" select="'false'"/>
    <xsl:param name="requestURI" select="''"/>
    <xsl:param name="requestHost" select="''"/>

	<xsl:template match="/">
	    <page>
	        <!-- content -->
    		<modules>
                <xsl:apply-templates select="error:notify" mode="headline"/>
    			<xsl:apply-templates select="error:notify" mode="meta"/>
    			<xsl:apply-templates select="error:notify" mode="content"/>
    		</modules>
    		<!-- css -->
    		<modules>
		    	<ctmpl:module name="css">
		    		<link href="/css/common.css" rel="stylesheet" type="text/css"/>
		    	</ctmpl:module>
	    	</modules>
    		<!-- home navigation - mandantenspezifisch - der apply sollte
    		     überschrieben werden. daher wird auch kein call sondern ein
    		     apply genutzt (nur falls jemand fragt) -->
    		<modules>
    		    <xsl:apply-templates select="error:notify" mode="navigation" />
    		</modules>
    	</page>
	</xsl:template>


	<xsl:template match="error:notify" mode="headline">
		<ctmpl:module name="headline" xmlns:ctmpl="http://www.conquest-cms.net/template">
            <div class="headline">Die Seite konnte nicht gefunden werden!</div>
		</ctmpl:module>
	</xsl:template>

	<xsl:template match="error:notify" mode="content">
		<ctmpl:module name="content" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="content">
    			<xsl:choose>
    			    <xsl:when test="$statusCode = '404'">
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a>, um zurück zur <a href="/">Startseite</a> zu gelangen.
    			    </xsl:when>
    			    <xsl:when test="$statusCode = '500'">
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a>, um zurück zur <a href="/">Startseite</a> zu gelangen.
    			    </xsl:when>
    			    <xsl:otherwise>
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a>, um zurück zur <a href="/">Startseite</a> zu gelangen.
    			    </xsl:otherwise>
    			</xsl:choose>
    			<xsl:call-template name="additional-info"/>
			</div>
		</ctmpl:module>
	</xsl:template>

    <xsl:template match="error:notify" mode="contentWithHeadline">
		<ctmpl:module name="content" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="content">
    			<div class="headline">Die Seite konnte nicht gefunden werden!</div>
    			<xsl:choose>
    			    <xsl:when test="$statusCode = '404'">
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a> um auf die <a href="/">Startseite</a> zurück zu gelangen.
    			    </xsl:when>
    			    <xsl:when test="$statusCode = '500'">
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a> um auf die <a href="/">Startseite</a> zurück zu gelangen.
    			    </xsl:when>
    			    <xsl:otherwise>
    			        Die Seite <b><xsl:value-of select="$requestURI"/></b> konnte auf <b><xsl:value-of select="$requestHost"/></b> nicht gefunden werden!<br/><br/>
    			        Falls Sie die URL manuell eingegeben haben, überprüfen Sie bitte die Schreibweise und versuchen es erneut.<br/><br/>
    			        Andernfalls klicken Sie bitte <a href="/">hier</a> um auf die <a href="/">Startseite</a> zurück zu gelangen.
    			    </xsl:otherwise>
    			</xsl:choose>
    			<xsl:call-template name="additional-info"/>
			</div>
		</ctmpl:module>
	</xsl:template>

	
	<xsl:template name="additional-info">
	    <xsl:variable name="cssDisplayValue">
	        <xsl:choose>
	            <xsl:when test="$liveserver = 'true'"><xsl:text>none</xsl:text></xsl:when>
	            <xsl:otherwise><xsl:text>block</xsl:text></xsl:otherwise>
	        </xsl:choose>
	    </xsl:variable>
	    <br/><br/>
        <table width="100%">
            <tr>
                <td>Weitere Infomationen zum Fehler (Code: <xsl:value-of select="$statusCode"/>)</td>
                <td align="right" width="1%"><a href="javascript:toggle('{generate-id(error:description)}_frame');javascript:toggle('{generate-id(error:extra[@error:description='cause'])}_frame');javascript:toggle('{generate-id(error:extra[@error:description='stacktrace'])}_frame');">Anzeigen</a></td>
            </tr>
        </table>
        
        <table width="100%" id="{generate-id(error:description)}_frame" style="display:{$cssDisplayValue};">
            <tr>
                <td align="left"><b>Error description</b></td>
                <td align="right" width="1%"><a href="javascript:toggle('{generate-id(error:description)}');">Anzeigen</a></td>
            </tr>
            <tr id="{generate-id(error:description)}" style="display:none;">
                <td align="left" colspan="2"><xsl:value-of select="error:description"/></td>
            </tr>
        </table>

        <table width="100%" id="{generate-id(error:extra[@error:description='cause'])}_frame" style="display:{$cssDisplayValue};">
            <tr>
                <td align="left"><b>Error cause</b></td>
                <td align="right" width="1%"><a href="javascript:toggle('{generate-id(error:extra[@error:description='cause'])}');">Anzeigen</a></td>
            </tr>
            <tr id="{generate-id(error:extra[@error:description='cause'])}" style="display:none;">
                <td align="left" colspan="2"><xsl:value-of select="error:extra[@error:description='cause']"/></td>
            </tr>
        </table>
            
        <table width="100%" id="{generate-id(error:extra[@error:description='stacktrace'])}_frame" style="display:{$cssDisplayValue};">
            <tr>
                <td align="left"><b>Error stacktrace</b></td>
                <td align="right" width="1%"><a href="javascript:toggle('{generate-id(error:extra[@error:description='stacktrace'])}');">Anzeigen</a></td>
            </tr>
            <tr id="{generate-id(error:extra[@error:description='stacktrace'])}" style="display:none;">
                <td align="left" colspan="2"><xsl:value-of select="error:extra[@error:description='stacktrace']"/></td>
            </tr>
        </table>
	</xsl:template>
	
	<xsl:template match="error:notify" mode="navigation">
        <ctmpl:module name="firstlevel3">
            <div class="firstlevel3">
                <div class="firstlink">
                    <a target="_self" href="/">Home</a>
                </div>
            </div>
        </ctmpl:module>	
	</xsl:template>
	
	<xsl:template match="error:notify" mode="meta">
		<ctmpl:module name="meta">
			<title>Fehler/Error</title>
		</ctmpl:module>
	</xsl:template>

</xsl:stylesheet>