<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
Diese Date wird referenziert von den Mandanten:
- para (

-->


	<xsl:include href="master.xsl"/>


	<xsl:template name="extralayer">		
			<div id="extra" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
			<div id="extra_1" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
			<div id="extra_2" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
			<div id="extra_3" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
			<div id="extra_4" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
			<div id="extra_5" onmouseover="clearSwitchOff();" onmouseout="noShowExtra(this)" class="extraLayer"></div>
	</xsl:template>

	<xsl:template match="navigation" priority="1">
		<div id="0"><xsl:apply-templates select="viewcomponent/viewcomponent[showType='3' or showType='0']"/></div>
		<xsl:call-template name="extralayer"/>
	</xsl:template>

	<xsl:template match="viewcomponent" priority="1">
		<div>
			<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
			<xsl:attribute name="class">main-link</xsl:attribute>
				<a>
					<xsl:call-template name="linkattributes"/>
					<xsl:value-of select="linkName"/>
				</a>
		</div>
<!--	Untermenülayer einfügen, wenn es Untermenüpunkte gibt-->
		<xsl:if test="viewcomponent and (template!='linkpage') and (viewIndex!='2')">
			<div class="sub-links"><xsl:attribute name="id"><xsl:value-of select="@id"/>_sub</xsl:attribute><xsl:apply-templates select="viewcomponent" mode="subLinks"/></div>
		</xsl:if>

	</xsl:template>


	<!--Untermenüpunkte schreiben-->
	<xsl:template match="viewcomponent" mode="subLinks">
		<div class="sub-link">
		<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
<!--		Ab der dritten Ebene keinen Rand mehr im letzten Element anzeigen, da er im Extralayer mit dem Ausseren Rand kollidiert-->
<!--		<xsl:if test="(../../../../viewcomponent) and (not(following-sibling::viewcomponent))">
			<xsl:attribute name="style">border-bottom:1px solid #ffffff;</xsl:attribute>
		</xsl:if>-->
			<a>
				<xsl:call-template name="linkattributes"/>
				<xsl:value-of select="linkName"/>
			</a>
		</div>
		<!--	Untermenülayer einfügen, wenn es Untermenüpunkte gibt-->
		<xsl:if test="viewcomponent and (template!='linkpage')  and (viewIndex!='2')">
			<div class="sub-links"><xsl:attribute name="id"><xsl:value-of select="@id"/>_sub</xsl:attribute><xsl:apply-templates select="viewcomponent" mode="subLinks"/></div>
		</xsl:if>
	</xsl:template>


<xsl:template name="linkattributes">
	<xsl:call-template name="href"/>
	<xsl:attribute name="onclick">highLightLeft(<xsl:value-of select="@id"/>)</xsl:attribute>
	<xsl:attribute name="onmouseover"><xsl:if test="viewcomponent">showExtra(this,event);</xsl:if><!--self.status="<xsl:value-of select="statusInfo"/>"; return true;--></xsl:attribute>
	<xsl:attribute name="onmouseout" ><xsl:if test="viewcomponent">noShowExtra(this);</xsl:if>self.status='';return true;</xsl:attribute>		
</xsl:template>


</xsl:stylesheet>













<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
