<?xml version="1.0"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0" 
	xmlns:ctmpl="http://www.conquest-cms.net/template"
	>
	
	<xsl:template match="@*|node()" priority="-2">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/">
		<html>
			<head>
				<title>page aggregation debugger</title>
			</head>
			<body>
				<xsl:apply-templates select="page"/>				
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="page">
		<xsl:apply-templates />		
	</xsl:template>
	
	<xsl:template match="cinclude:cached-include">
		<xsl:if test="(@src = 'teaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/teaser='false') or (@src = 'unitteaseraggregation.xhtml' and //ctmpl:module[@name='showteaser']/unitteaser='false')">
			<div><![CDATA[Die ]]><xsl:value-of select="@src"/><![CDATA[ wurde entfernt, weil es die Content xhtml gesagt hat]]></div>
		</xsl:if>	
		<div>
			<a href="{substring-after(@src,'cocoon:')}"><xsl:value-of select="@src"/></a>				
		</div>
	</xsl:template>
	
	<xsl:template match="modules">
		<div>Modules:<xsl:apply-templates /></div>
		<br/>
	</xsl:template>
	
	<xsl:template match="ctmpl:module">
		<div style="padding-left:70px;">Module:<xsl:value-of select="@name"/></div>
	</xsl:template>

	
	</xsl:stylesheet>

