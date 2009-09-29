<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template" xmlns:error="http://apache.org/cocoon/error/2.1">
	
	<xsl:param name="statusCode" select="'0'"/>
	<xsl:param name="liveserver" select="'false'"/>
	<xsl:param name="requestURI" select="''"/>
	<xsl:param name="requestHost" select="''"/>
	<xsl:param name="language" select="''"/>
	
	<!-- Stacktrace Parameter set true to enable Stacktrace output, usually hidden for customers -->
	<xsl:param name="st" select="''"/>
	
	<xsl:template match="/">
		<page>
			<modules>
				<xsl:apply-templates select="error:notify" mode="headline"/>
				<xsl:apply-templates select="error:notify" mode="meta"/>
				<xsl:apply-templates select="error:notify" mode="content"/>
			</modules>
			<modules>
				<ctmpl:module name="css">
					<link href="/css/common.css" rel="stylesheet" type="text/css"/>
				</ctmpl:module>
			</modules>
			<!--<modules>
				<ctmpl:module name="firstlevel2">
					<div class="firstlevel2">
						<div class="firstlink">
							<a target="_self" href="/">Home</a>
						</div>
					</div>
				</ctmpl:module>
			</modules>-->
		</page>
	</xsl:template>
	
	<xsl:template match="error:notify" mode="headline">
		<ctmpl:module name="headline" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<xsl:choose>
				<xsl:when test="$language = 'de'">
					<h1 class="headline">SEITE NICHT GEFUNDEN</h1>	
				</xsl:when>
				<xsl:otherwise>
					<h1 class="headline">PAGE NOT FOUND</h1>
				</xsl:otherwise>
			</xsl:choose>
		</ctmpl:module>
	</xsl:template>
	
	<xsl:template match="error:notify" mode="content">
		<ctmpl:module name="content" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="content">
				<xsl:choose>
					<xsl:when test="$language = 'de'">
						<p>
							Die von Ihnen angeforderte Seite konnte nicht gefunden werden. Die Seite wurde möglicherweise
							entfernt, wurde umbenannt oder ist vorübergehend nicht verfügbar.<br/>
						</p>
						<div class="subline">Bitte versuchen Sie folgendes:</div>
						<ul>
							<li>Wenn Sie die Seite manuell in die Adressleiste eingegeben haben, überprüfen Sie die Schreibweise.</li>
							<li>Öffnen Sie <a href="/">die Startseite</a> und suchen Sie Links um die Informationen zu finden die Sie suchen.</li>
							<li>Benutzen Sie die Navigation um die gewünschte Seite aufzurufen.</li>
							<li>Klicken Sie den zurück Button Ihres Browsers und versuchen Sie einen anderen Link.</li>
						</ul>
					</xsl:when>
					<xsl:otherwise>
						<p>
							The page you requested cannot be found. The page you are looking for might have 
							been removed, had its name changed, or is temporarily unavailable.<br/>
						</p>
						<div class="subline">Please try the following:</div>
						<ul>
							<li>If you typed the page address in the Address bar, make sure that it is spelled correctly.</li>
							<li>Open the <a href="/">home page</a> and look for links to the information you want.</li>
							<li>Use the navigation bar to find the link you are looking for.</li>
							<li>Click the Back button to try another link.</li>
						</ul>
					</xsl:otherwise>
				</xsl:choose>
				<!-- For developers only: show stacktrace -->
				<xsl:if test="$st = 'true'">
					<xsl:call-template name="additional-info"/>
				</xsl:if>
			</div>
		</ctmpl:module>
	</xsl:template>
	
	<xsl:template name="additional-info">
		<br/>
		<table width="100%">
			<tr>
				<td style="font-weight:bold">Further information regarding this error (Code: <xsl:value-of select="$statusCode"/>)</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td align="left"><b>Error description</b></td>
				<td align="right" width="1%"><a href="javascript:toggle('N10037')">show</a></td>
			</tr>
			<tr style="display:none;" id="N10037" name="N10037">
				<td align="left" colspan="2"><xsl:value-of select="error:description"/></td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td align="left"><b>Error cause</b></td>
				<td align="right" width="1%"><a href="javascript:toggle('N10038')">show</a></td>
			</tr>
			<tr style="display:none;" id="N10038" name="N10038">
				<td align="left" colspan="2"><xsl:value-of select="error:extra[@error:description='cause']"/></td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td align="left"><b>Error stacktrace</b></td>
				<td align="right" width="1%"><a href="javascript:toggle('N10039')">show</a></td>
			</tr>
			<tr style="display:none;" id="N10039" name="N10039">
				<td align="left" colspan="2"><xsl:value-of select="error:extra[@error:description='stacktrace']"/></td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template match="error:notify" mode="meta">
		<ctmpl:module name="meta">
			<title>Error</title>
		</ctmpl:module>
	</xsl:template>
	
</xsl:stylesheet>