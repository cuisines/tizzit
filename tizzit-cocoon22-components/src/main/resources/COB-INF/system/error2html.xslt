<?xml version="1.0"?>
<!-- CVS $Id: error2html.xslt,v 1.9 2003/09/03 10:50:48 bruno Exp $ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:error="http://apache.org/cocoon/error/2.1">
	<!--<xsl:param name="contextPath" select="string('/cocoon')"/>-->
	<xsl:param name="contextPath" />
	<xsl:param name="pageTitle" select="//error:notify/error:title"/>
	<xsl:param name="statusCode"/>
	<xsl:param name="liveserver"/>
	<xsl:param name="requestURI" />

	<xsl:template match="error:notify">
		<html>
			<head>
				<title><xsl:value-of select="$pageTitle" />!</title>
				<style>body,div {font-family: Arial,Verdana; font-size: 13px; color: #000000}
		h1 { font-size: 13px;color: #336699; margin: 0px 0px 20px 0px; padding: 0px;}
		.message { font-size: 13px; padding: 10px 20px 10px 10px; font-weight: bold; border-bottom: 1px solid #336699; margin: 2px 0px 2px 0px;}
		.error-description { padding: 0px 0px 0px 5px; font-weight:bold; background:#336699; color:#FFFFFF; }
		.error-full { padding: 10px 20px 10px 20px; border: 1px solid #336699;}
		a { color:#000000; }
		a.toggle       { color:#FFFFFF; text-decoration:none;}
		a.toggle:hover { text-decoration:underline; }</style>
				<script language="JavaScript"><![CDATA[
				  function toggleVisibility(id) {
					if (document.getElementById(id).style.display=="none") {
                      document.getElementById(id).style.display="";
					} else {
                      document.getElementById(id).style.display="none";
					}
				  }
				//]]>
				</script>
			</head>
			<body>
				<table cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<div class="message">
								<img src="{$contextPath}/cms/img/error-roteskreuz.gif" width="18" height="18" align="left" vspace="0" hspace="10"/><xsl:value-of select="$pageTitle" />!</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="message">
								<img src="error-trythis.jpg" width="70" height="96" align="left" vspace="0" hspace="10"/>The requested URL was not found on this server.<br/>
		   <br/>
           If you entered the URL manually please check your<br/>
		   spelling and try again...<br/>
		   <br/>
		   If you think this is a server error, please contact the webmaster.</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="message" style="padding-left:20px;">Error <xsl:value-of select="$statusCode"/> - For additional information please click <a href="javascript:toggleVisibility('debug');">here</a>.</div>
						</td>
					</tr>
					<tr>
						<td>
				<xsl:choose>
					<xsl:when test="$liveserver='false'">
						<div id="debug">
							<xsl:apply-templates select="error:extra"/>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<div id="debug" style="display:none;">
							<xsl:apply-templates select="error:extra"/>
						</div>
					</xsl:otherwise>
				</xsl:choose>
						</td>
					</tr>
				</table>
				<br/>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="error:extra">
		<div class="error-description">
			<xsl:value-of select="@error:description"/> [<a class="toggle" id="{@error:description}-switch" href="javascript:toggleVisibility('{@error:description}')">show</a>]</div>

		<xsl:choose>
			<xsl:when test="$liveserver='false' and contains(@error:description,'cause')">
				<div id="{@error:description}" class="error-full">
					<xsl:value-of select="."/>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div id="{@error:description}" style="display: none" class="error-full">
					<xsl:value-of select="."/>
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
