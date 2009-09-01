<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="publikation-komp" mode="format">
		<xsl:apply-templates select="."/>
	</xsl:template>

<xsl:template match="publikation">
<a>
<xsl:attribute name="href">javascript:MM_openBrowserWindow('/coc/cms/maca/<xsl:value-of select="$language"/>/<xsl:value-of select="$url"/>/content.html?itemName=publikation&amp;itemNr=<xsl:value-of select="position()"/>','pub','width=200 height= 200 top=200 left=200')</xsl:attribute>
(<xsl:value-of select="position()"/>)
</a>
</xsl:template>

<!-- publikationen -->
<xsl:template match="publikation" mode="popup">
	<xsl:apply-templates select="pubhead" mode="pubhead"/>
	<xsl:apply-templates select="pubname" mode="pubname"/>
<!-- bitte ersetzen durch: -->
	<xsl:apply-templates select="autor" mode="autor"/>
	<xsl:apply-templates select="pubinhalt" mode="pubinhalt"/>
<!-- bitte ersetzen durch: -->
	<xsl:apply-templates select="inhalt" mode="inhalt"/>
	<xsl:apply-templates select="pubveroeffentlich" mode="pubveroeffentlich"/>
<!-- bitte ersetzen durch: -->
	<xsl:apply-templates select="veroeffentlicht-in" mode="veroeffentlicht-in"/>
	<xsl:apply-templates select="line" mode="format"/>
	<xsl:apply-templates select="pubtel" mode="pubtel"/>
	<xsl:apply-templates select="pubemail" mode="pubemail"/>
	<xsl:apply-templates select="pubfax" mode="pubfax"/>
	<xsl:apply-templates select="pubhomepage" mode="pubhomepage"/>
	<xsl:apply-templates select="pubsonstiges" mode="pubsonstiges"/>

	<xsl:apply-templates select="pubsubhead" mode="pubsubhead"/>
	<xsl:apply-templates select="pubtext" mode="pubtext"/>
<br/>
</xsl:template>





<xsl:template match="pubhead" mode="pubhead">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<font class="headline"><xsl:value-of select="."/></font><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubsonstiges" mode="pubsonstiges">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<br/><xsl:value-of select="."/><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubhomepage" mode="pubhomepage">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<a>
 <xsl:attribute name="href"><xsl:value-of select="@url"/></xsl:attribute>
 <xsl:attribute name="target"><xsl:value-of select="@target"/></xsl:attribute> 
 <xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute> 
 <xsl:value-of select="."/></a><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubtel" mode="pubtel">
<xsl:choose>
<xsl:when test="not(boolean(.=''))"><b>Tel:&#160;
<xsl:value-of select="."/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubemail" mode="pubemail">
<xsl:choose>
<xsl:when test="not(boolean(.=''))"><b>E-Mail:&#160;
<a>
<xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
<xsl:value-of select="."/>
</a><br/></b>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubfax" mode="pubfax">
<xsl:choose>
<xsl:when test="not(boolean(.=''))"><b>Fax:&#160;
<xsl:value-of select="."/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubname" mode="pubname">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<b><xsl:value-of select="."/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="autor" mode="autor">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<b><xsl:value-of select="."/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubveroeffentlich" mode="pubveroeffentlich">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<xsl:value-of select="."/><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="veroeffentlicht-in" mode="veroeffentlicht-in">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
veröffentlicht in:
<xsl:apply-templates/>,&#160;<xsl:value-of select="../datum"/><br/>
</xsl:when>
</xsl:choose>
</xsl:template>
<!--
<xsl:template match="datum" mode="datum">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<xsl:value-of select="."/>
</xsl:when>
</xsl:choose>
</xsl:template>
-->
<xsl:template match="pubinhalt" mode="pubinhalt">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<b><xsl:apply-templates mode="format"/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="inhalt" mode="inhalt">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<b><xsl:apply-templates mode="format"/></b><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubsubhead" mode="pubsubhead">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<font class="subline"><xsl:value-of select="."/></font><br/><br/>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="pubtext" mode="pubtext">
<xsl:choose>
<xsl:when test="not(boolean(.=''))">
<xsl:value-of select="."/><br/>
</xsl:when>
</xsl:choose>
</xsl:template>




</xsl:stylesheet>





<!--
	<xsl:apply-templates select="line" mode="format"/>
-->








<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" url="..\..\master2.xml" htmlbaseurl="" processortype="internal" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
