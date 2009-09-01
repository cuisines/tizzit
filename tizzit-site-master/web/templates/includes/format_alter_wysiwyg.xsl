<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- hier werden die formatanweiseungen innerhalb des Fliesstextes ausgefuehrhrt-->


	<xsl:template match="notice"></xsl:template>

	<xsl:template match="notice" mode="format"></xsl:template>

	<xsl:template match="@*|node()" priority="-1" mode="format">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" mode="format"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="headLine" mode="format">
		<font class="headline">
			<xsl:value-of select="."/>
			<br/>
		</font>
	</xsl:template>

	<!--Pop up Fenster öffnen für Literatur/ Zitat-->
	<xsl:template match="literaturangabe" mode="format">
		<xsl:apply-templates select="literatur-zitat" mode="format"/>
		(<a>
			<xsl:attribute name="href">javascript:MM_openBrWindowForInternalLink('/cms/popup/<xsl:value-of select="$language"/>/<xsl:call-template name="url-encode"><xsl:with-param name="str" select="$url"/></xsl:call-template>/content.html?itemName=literaturangabe&amp;itemNr=<xsl:value-of select="count(preceding::literaturangabe)+1"/>','literaturangabe','top=200, left=200, height=150, width=300')</xsl:attribute>
			<xsl:apply-templates select="." mode="link"/>
		</a>)
	</xsl:template>
	<!--START Linkname zum öffnen des Pop up Fensters Literatur / Zitat-->
	<xsl:template match="literaturangabe" mode="link">
		<xsl:if test="child::author-year or child::literatur-year"><xsl:apply-templates select="literatur-author" mode="format"/><xsl:apply-templates select="literatur-year" mode="format"/></xsl:if>
	</xsl:template>
	<xsl:template match="literatur-author" mode="format">
		<xsl:if test="not(boolean(.=''))">
			<xsl:apply-templates mode="format"/>,</xsl:if>
	</xsl:template>
	<xsl:template match="literatur-year" mode="format">
		<xsl:if test="not(boolean(.=''))">
			<xsl:apply-templates mode="format"/>
		</xsl:if>
	</xsl:template>
	<!--ENDE Linkname zum öffnen des Pop up Fensters Literatur / Zitat-->
	<!--Den Zitat Text entweder mit oder ohne Anführungszeichen darstellen-->
	<xsl:template match="literatur-zitat" mode="format">
		<xsl:if test="not(boolean(.=''))">
			<font class="literaturangabe">
				<xsl:choose>
					<xsl:when test="@quotEnabled='true'">"<xsl:apply-templates select="."/>"&#160;</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="."/>&#160;
					</xsl:otherwise>
				</xsl:choose>
			</font>
		</xsl:if>
	</xsl:template>


	<xsl:template match="table" mode="format">
		<table cellspacing="0" cellpadding="2" border="0" class="contenttable">
			<!--mit oder ohne Rand -->
<!--			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="@viewborder='true'">contenttableborder</xsl:when>
					<xsl:otherwise>contenttable</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>-->
			<xsl:apply-templates mode="format"/>
		</table>
	</xsl:template>

	<xsl:template match="tr" mode="format">
		<tr>
			<xsl:apply-templates mode="format"/>
		</tr>
	</xsl:template>

	<xsl:template match="td" mode="format">
		<td class="tdata">
			<xsl:if test="not(../../@viewborder) or @viewborder='false'">
				<xsl:attribute name="style">border-style:none;</xsl:attribute>
			</xsl:if>
			<xsl:if test="@rowspan">
				<xsl:attribute name="rowspan">
					<xsl:value-of select="@rowspan"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@colspan">
				<xsl:attribute name="colspan">
					<xsl:value-of select="@colspan"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@align">
				<xsl:attribute name="align">
					<xsl:value-of select="@align"/>
				</xsl:attribute>
			</xsl:if>
				<xsl:apply-templates mode="format"/>&#160;&#160;</td>
	</xsl:template>


	<xsl:template match="th" mode="format">
		<th class="thead">
			<xsl:if test="not(../../@viewborder) or @viewborder='false'">
				<xsl:attribute name="style">border-style:none;</xsl:attribute>
			</xsl:if>
			<xsl:if test="@rowspan">
				<xsl:attribute name="rowspan">
					<xsl:value-of select="@rowspan"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@colspan">
				<xsl:attribute name="colspan">
					<xsl:value-of select="@colspan"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@align">
				<xsl:attribute name="align">
					<xsl:value-of select="@align"/>
				</xsl:attribute>
			</xsl:if>
				<xsl:apply-templates mode="format"/>&#160;&#160;</th>
	</xsl:template>


	<xsl:template match="name" mode="format">
		<b>
			<xsl:value-of select="."/>
		</b>
	</xsl:template>


	<xsl:template match="titelbild" mode="format">
		<xsl:apply-templates select="." mode="titelbild"/>
	</xsl:template>

	<xsl:template match="image" mode="format">
		<xsl:apply-templates select="." mode="type"/>
	</xsl:template>

	<xsl:template match="text" mode="format">
		<xsl:apply-templates mode="format"/>
	</xsl:template>

	<xsl:template match="line" mode="format">
		<xsl:apply-templates select="." mode="line"/>
	</xsl:template>

	<xsl:template match="vita" mode="format">
		<xsl:apply-templates select="." mode="vita"/>
	</xsl:template>

	<xsl:template match="subline" mode="format">
		<font class="subline">
			<xsl:value-of select="."/>
		</font>
		<br/>
	</xsl:template>

	<xsl:template match="subtextline" mode="format">
		<font class="text">
			<b>
				<xsl:value-of select="."/>
			</b>
		</font>
	</xsl:template>

	<xsl:template match="textline" mode="format">
		<font class="text">
			<xsl:apply-templates mode="format"/>
		</font>
	</xsl:template>

	<xsl:template match="link" mode="format">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="@url"/>
			</xsl:attribute>
			<xsl:attribute name="id">
				<xsl:value-of select="@id"/>
			</xsl:attribute>
			<xsl:attribute name="target">
				<xsl:value-of select="@target"/>
			</xsl:attribute>
			<xsl:attribute name="alt">
				<xsl:value-of select="@alt"/>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:apply-templates mode="format"/>
		</a>
	</xsl:template>


	<xsl:template match="email" mode="format">
		<a>
			<xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
			<xsl:apply-templates mode="format"/>
		</a>
	</xsl:template>



	<xsl:template match="zitat" mode="format">
		<font class="zitat">"<xsl:apply-templates mode="format"/>"</font>
	</xsl:template>

	<xsl:template match="marginalie" mode="format">
		<font class="marginalie">
			<xsl:apply-templates mode="format"/>
		</font>
		<br/>
	</xsl:template>

	<xsl:template match="exclamation" mode="format">
		<xsl:choose>
			<xsl:when test="$liveserver='false'">
				<b>
					<font class="aufforderung">
						<xsl:apply-templates mode="format"/>
					</font>
				</b>
				<br/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!--Einen Link, der das Attribut header=true hat einem neuen Fenster mit header oeffnen lassen-->
	<xsl:template match="datum" mode="format">
		<xsl:apply-templates select="tag"/>.<xsl:apply-templates select="monat"/>.<xsl:apply-templates select="jahr"/>
</xsl:template>

	<xsl:template match="day" mode="format">
		<xsl:apply-templates/>.</xsl:template>
	<xsl:template match="month" mode="format">
		<xsl:apply-templates/>.</xsl:template>
	<xsl:template match="year" mode="format">
		<xsl:apply-templates/>&#160;
	</xsl:template>
	<!--Ausschluss von Elementen! Diese Elemente werden nicht in den HTML Output übernommen! -->
	<!-- testarea simon ende -->

	<xsl:template match="subItem" mode="format">
	</xsl:template>
	<xsl:template match="tablehead" mode="format">
	</xsl:template>
	<xsl:template match="comments" mode="format">
	</xsl:template>
	<!-- Template fuer Aufzaehlung-->
	<xsl:template match="aufzaehlung" mode="format">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td valign="top">
					<img src="aufzaehlung.gif"/>
				</td>
				<td valign="top">
					<xsl:value-of select="."/>
				</td>
			</tr>
		</table>
	</xsl:template>


	<!--Symbols of the character Map-->

<xsl:template match="symbol" mode="format">
	<xsl:apply-templates select="property" mode="format"/>
</xsl:template>
	<xsl:template match="property[@enable]" mode="format">
	<xsl:choose>
			<xsl:when test="string(@bold)='true'">
				<b>
					<xsl:apply-templates select="." mode="format2"/>
				</b>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="format2"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="property" mode="format2">
		<xsl:choose>
			<xsl:when test="@italics='true'">
				<i>
					<xsl:apply-templates select="." mode="format3"/>
				</i>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="format3"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="property" mode="format3">
		<xsl:choose>
			<xsl:when test="@enable='true'">
				<xsl:apply-templates select="." mode="format4"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="property" mode="format4">
		<xsl:choose>
			<xsl:when test="@superscript='true'">
				<sup>
						<xsl:value-of select="." disable-output-escaping="yes" />
				</sup>
			</xsl:when>
			<xsl:when test="@subscript='true'">
				<sub>
						<xsl:value-of select="." disable-output-escaping="yes" />
				</sub>
			</xsl:when>
			<xsl:otherwise>
					<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--END Symbols of the character Map-->

<xsl:template match="stickyPad" mode="format">	
</xsl:template>

<xsl:template match="metaInformation" mode="format"></xsl:template>

<xsl:template match="authentication" priority="-1" mode="format">
	<xsl:comment>Eingeloggt als <xsl:value-of select="id"/></xsl:comment>
</xsl:template>

<!--fuer marker color-->
<xsl:template match="font[@class='mark']">
	<font class="mark"><xsl:apply-templates /></font>	
</xsl:template>



<xsl:template match="content" mode="format">
	<xsl:apply-templates mode="format"/>
</xsl:template>

<xsl:template match="subhead" mode="format">
	<xsl:apply-templates mode="format"/>
</xsl:template>



<xsl:template match="anchor" mode="format">
	<xsl:apply-templates mode="format"/>
</xsl:template>

<xsl:template match="a[@type='anchor']" mode="format">
	<a name="{@name}"/>
</xsl:template>

</xsl:stylesheet>





<!-- Stylus Studio meta-information - (c)1998-2004. Sonic Software Corporation. All rights reserved.
<metaInformation>
<scenarios/><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
</metaInformation>
--><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
