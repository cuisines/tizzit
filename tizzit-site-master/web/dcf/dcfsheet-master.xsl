<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cinclude="http://apache.org/cocoon/include/1.0">
	<!--
=========START How to use the Dcfsheet ================
1. every xml ELement in the DCF is routed to the output if it's not declared in the dcfsheet or dcfsheet-master
2. declared in the dcfsheet-master are:
	- <head/>
	- <textfield/>
	- <stickyPad/>
	- <notice/>
	- footer
	- picture
	- document
	- iframe
	- simpletextfield
	- tmenu
	- paragraph
	- aggregation
	- cinclude
	- checkBox
	- simpleDate
	- deployStartStop
	- internalLink
	- externalLink
	- shape | dropdownbox for the shape of image maps
	- webcam (simple webcam)
	- time
	- selectboxes / dropdown
	- minipage
	(to be expanded)
3. There are declared Elements with options and without
4. The element <textfield/> has the following options:
	a.) default value: <text dcfname="text" label="Textfeld">
	b.) dcfname, Elementname and label can be overwritten by the dcf. For example:
		<textfield name="info" label="Infofeld" dcfname="info1"/> instead of the default values
	c.) all possible allowedElements are included by default
	d.) allowedELements can be overwritten by the dcf
		<textfield name="content">
			<subline/>	
			<HSubline/>	
			<line/>
			<picture/>
			<documents/>
			<anchor/>
			<internalLink/>
			<externalLink/>
			<aggregation/>
			<literaturangabe/>
			<publikation-komp/>
			<top/>
			<markerColor/>
 		</textfield>
		If the textfield has children, the children are the only allowedElements.
5. A linklist to the generated DCF's is availabe as html: /{clientCode}/dcf/dcf.html
6. The dcfsheet-master is included by the dcfsheet of the client
7. The client dcfsheet can declare specific Elements or overwrite the master declarations
8. The checkBox can be called like this: 
<checkBox name="checkBox">
	<box label="Hallo" elementname="halloTrue">true</box>
</checkBox>



=========END How to use ============================
-->

	<xsl:variable name="cqcms-juwimm-plugin-wysiwyg" select="'cqcms-juwimm-plugin-wysiwyg-5.1-all.jar'" />

<xsl:include href="../templates/includes/date.lib.xsl"/>

	<!-- head und title Element must be written in dcf, otherwise title of template is not saved. The DEFAULT value is overwritten by the template always -->
	<xsl:template match="head" priority="-0.9">
		<head>
			<title>DEFAULT</title>
		</head>
	</xsl:template>
	

	<!--route xml through, that is not matched-->
	<xsl:template match="@*|node()" priority="-1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="source">
		<xsl:element name="source">
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	<!-- ============== START Textfeld ==================== -->
	<xsl:template match="textfield" name="textfield">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>content</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Textfeld</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Textfield</classname>
				<!-- For ConQuest 3.0 you need to outcomment this area: -->
				<classpath>
					<jar><xsl:value-of select="$cqcms-juwimm-plugin-wysiwyg"/></jar>
				</classpath> 
				<xsl:choose>
					<xsl:when test="not(child::*)">
						<xsl:apply-templates select="." mode="allowall"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates mode="allow"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="@height"/>
			</dcfConfig>
			<dcfInitial>
			    <xsl:apply-templates select="." mode="initial"/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>
    <!--Paragraph fuer dcfinitial in textfield, kann in mandanten dcfsheets ueberschrieben werden -->
    <xsl:template match="textfield" mode="initial"><p/></xsl:template>
    
	<xsl:template match="@height" name="height">
		<property name="size">
			<height>
				<xsl:value-of select="."/>
			</height>
		</property>
	</xsl:template>

	<xsl:template match="nothingAllowed" mode="allow"/>

	<xsl:template match="textfield" mode="allow"/>

	<xsl:template match="subline" name="subline" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>subline</rootnodeName>
			<name>Zwischenberschrift</name>
			<class>de.juwimm.cms.content.modules.TxtSubline</class>
		</property>
	</xsl:template>
	<xsl:template match="HSubline" name="HSubline" mode="allow">
		<property name="HSubline">
			<number>1</number>
			<name>Überschrift 1. Ordnung</name>
		</property>
		<property name="HSubline">
			<number>2</number>
			<name>Überschrift 2. Ordnung</name>
		</property>
		<property name="HSubline">
			<number>3</number>
			<name>Überschrift 3. Ordnung</name>
		</property>
		<property name="HSubline">
			<number>4</number>
			<name>Überschrift 4. Ordnung</name>
		</property>
		<property name="HSubline">
			<number>5</number>
			<name>Überschrift 5. Ordnung</name>
		</property>
		<property name="HSubline">
			<number>6</number>
			<name>Überschrift 6. Ordnung</name>
		</property>
	</xsl:template>
	<xsl:template match="line" name="line" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>line</rootnodeName>
			<name>Linie</name>
			<class>de.juwimm.cms.content.modules.Line</class>
		</property>
	</xsl:template>
	<xsl:template match="picture" name="picture" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>picture</rootnodeName>
			<name>Bild</name>
			<class>de.juwimm.cms.content.modules.Picture</class>
		</property>
	</xsl:template>
	<xsl:template match="pictureCustomPreview" name="pictureCustomPreview" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>pictureCustom</rootnodeName>
			<name>Bild mit benutzerdefinierter Vorschau</name>
			<class>de.juwimm.cms.content.modules.PictureCustomPreview</class>
		</property>
	</xsl:template>
	<xsl:template match="documents" name="documents" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>documents</rootnodeName>
			<name>Dokumente</name>
			<class>de.juwimm.cms.content.modules.Documents</class>
		</property>
	</xsl:template>
	<xsl:template match="anchor" name="anchor" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>anchor</rootnodeName>
			<name>Anker</name>
			<class>de.juwimm.cms.content.modules.Anchor</class>
		</property>
	</xsl:template>
	<xsl:template match="internalLink" name="internalLink" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>internalLink</rootnodeName>
			<name>Interner Link</name>
			<class>de.juwimm.cms.content.modules.InternalLink</class>
			<properties>
				<property name="DisplayType">
					<editable>true</editable>
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>
	<xsl:template match="externalLinkPopupEdit" name="externalLinkPopupEdit" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>externalLink</rootnodeName>
			<name>Externer Link</name>
			<class>de.juwimm.cms.content.modules.ExternalLink</class>
			<properties>
				<property name="DisplayType">
					<editable>true</editable>
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>
	
	<xsl:template match="internalLinkPopupEdit" name="internalLinkPopupEdit" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>internalLink</rootnodeName>
			<name>Interner Link</name>
			<class>de.juwimm.cms.content.modules.InternalLink</class>
			<properties>
				<property name="DisplayType">
					<editable>true</editable>
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>
	<xsl:template match="externalLink" name="externalLink" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>externalLink</rootnodeName>
			<name>Externer Link</name>
			<class>de.juwimm.cms.content.modules.ExternalLink</class>
			<properties>
				<property name="DisplayType">
					<editable>true</editable>
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>	
	<xsl:template match="internalLinkPopup" name="internalLinkPopup" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>internalLink</rootnodeName>
			<name>Interner Link</name>
			<class>de.juwimm.cms.content.modules.InternalLink</class>
			<properties>
				<property name="DisplayType">
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>	
	<xsl:template match="externalLinkPopup" name="externalLinkPopup" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>externalLink</rootnodeName>
			<name>Externer Link</name>
			<class>de.juwimm.cms.content.modules.ExternalLink</class>
			<properties>
				<property name="DisplayType">
					<popupAvailable>true</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>	
	<xsl:template match="internalLinkNonPopup" name="internalLinkNonPopup" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>internalLink</rootnodeName>
			<name>Interner Link</name>
			<class>de.juwimm.cms.content.modules.InternalLink</class>
			<properties>
				<property name="DisplayType">
					<popupAvailable>false</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>	
	<xsl:template match="externalLinkNonPopup" name="externalLinkNonPopup" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>externalLink</rootnodeName>
			<name>Externer Link</name>
			<class>de.juwimm.cms.content.modules.ExternalLink</class>
			<properties>
				<property name="DisplayType">
					<popupAvailable>false</popupAvailable>
				</property>
			</properties>
		</property>
	</xsl:template>	
	<xsl:template match="aggregation" name="aggregation" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>aggregation</rootnodeName>
			<name>Personendatenbank</name>
			<class>de.juwimm.cms.content.modules.DatabaseComponent</class>
		</property>
	</xsl:template>
	<xsl:template match="literaturangabe" name="literaturangabe" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>literaturangabe</rootnodeName>
			<name>Literaturangabe</name>
			<class>de.juwimm.cms.content.modules.Literature</class>
		</property>
	</xsl:template>
	<xsl:template match="publikation-komp" name="publikation-komp" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>publikation-komp</rootnodeName>
			<name>Publikation</name>
			<class>de.juwimm.cms.content.modules.Publication</class>
		</property>
	</xsl:template>
	<xsl:template match="markerColor" name="markerColor" mode="allow">
		<xsl:param name="color">#FFCCCC</xsl:param>
		<property name="MarkerColor">
			<showMarkerColor>true</showMarkerColor>
			<color>
				<xsl:value-of select="$color"/>
			</color>
		</property>
	</xsl:template>

	<xsl:template match="top" name="top" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>top</rootnodeName>
			<name>Link nach oben</name>
			<class>de.juwimm.cms.content.modules.Line</class>
			<properties>
				<property name="image">
					<iconImage>16_nachoben.gif</iconImage>
					<paneImage>16_nachoben.gif</paneImage>
				</property>
			</properties>
		</property>
	</xsl:template>
	<!--call all possible allowed Elements-->
	<xsl:template match="textfield | paragraph" mode="allowall">
		<xsl:call-template name="subline"/>
		<xsl:call-template name="HSubline"/>
		<xsl:call-template name="line"/>
		<xsl:call-template name="picture"/>
		<xsl:call-template name="documents"/>
		<xsl:call-template name="anchor"/>
		<xsl:call-template name="internalLink"/>
		<xsl:call-template name="externalLink"/>
		<xsl:call-template name="aggregation"/>
		<xsl:call-template name="literaturangabe"/>
		<xsl:call-template name="publikation-komp"/>
	</xsl:template>
	<!--===============END TExtfeld=================-->

	<!--================START components==============-->
	<xsl:template match="stickyPad">
		<stickyPad dcfname="stickypad" label="Notiz">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.StickyPadModule</classname>
			</dcfConfig>
			<dcfInitial/>
		</stickyPad>
	</xsl:template>

	<xsl:template match="notice">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>notice</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Information</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleTextArea</classname>
				<property name="viewOption">
					<editable>false</editable>
				</property>
			</dcfConfig>
			<dcfInitial>
				<xsl:value-of select="."/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<!--============END components================-->

	<xsl:template match="picture">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>picture</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Bild</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Picture</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="pictureCustom">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>pictureCustom</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Bild mit custom thnumbnail</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.PictureCustomPreview</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="documents">
		<xsl:variable name="elementname">documents</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Dokument</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Documents</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>
	<!-- Neue Flash Komponente im WISIWIG-Editor/-->	
	<xsl:template match="flash_dialog" name="flash_dialog" mode="allow">
		<property name="AllowedElements">
			<rootnodeName>flash_dialog</rootnodeName>
			<name>Flash Dialog</name>
			<class>de.juwimm.cms.content.modules.Flash</class>
			<properties>
				<property name="PlayerVersion">
					<!-- semicolon separated, whitespaces are ignored -->
					<!-- the last version number is taken as default -->
					<versions>5,0,0,0; 6,0,0,0; 7,0,0,0; 8,0,0,0; 9,0,0,0</versions>
				</property>
				<property name="Autostart">
					<choosable>true</choosable>
					<default>false</default>
				</property>
				<property name="Loop">
					<choosable>true</choosable>
					<default>false</default>
				</property>
				<property name="FlashVariables">
					<choosable>true</choosable>
					<variables>
						<!-- semicolon separated, whitespaces are ignored --> 
						ersteVariable = ersterWert; zweiteVariable = zweiterWert 
					</variables>
				</property>
				<property name="Quality">
					<choosable>true</choosable>
					<default>best</default>
					<values>
						<!-- semicolon separated, whitespaces are ignored--> 
						low; medium; high; best
					</values>
				</property>
				<property name="Scale">
					<choosable>true</choosable>
					<default>exactfit</default>
					<values>
						<!-- semicolon separated, whitespaces are ignored-->
						showall; noborder; exactfit
					</values>
				</property>
				<property name="Transparency">
					<choosable>true</choosable>
					<default>n/a</default>
					<values>
						<!-- semicolon separated, whitespaces are ignored--> 
						n/a; window; opaque; transparent
					</values>
				</property>
			</properties>
		</property>
	</xsl:template>

	<!--=====ENDE FLASH-Komponente====/-->
	
	
	<!-- Einbinden der Flash-Komponente unabhängig von WYSIWIG Editor -->
	<!-- example: -->
	
	<!-- <flash_component loop="false" autostart="false" version="8,0,0; 7,0,0" elementname="flash" label="Label für Komponente"/> -->
	
	<!-- OPTIONAL PARAMTERS: -->
	<!-- @elementname:		spezifiziert das Tag -->
	<!-- @label:			spezifiziert das Label -->
	<!-- @loop:				allowed values: true, false, choosable -->
	<!-- @autostart: 		allowed values for : true, false, choosable -->
	<!-- @version:			multiple or single values allowed, last value is default value-->
	
	<xsl:template match="flash_component">
		
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@elementname">
					<xsl:value-of select="@elementname"/>
				</xsl:when>
				<xsl:otherwise>flash</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<flash>
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Flash Komponente</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			
			
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Flash</classname>
				
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory=true">
							<xsl:text>true</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>false</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</mandatory>
				
				<property name="PlayerVersion">
					<versions>
						<xsl:choose>
							<xsl:when test="@version">
								<xsl:value-of select="@version"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>5,0,0,0; 6,0,0,0; 8,0,0,0; 9,0,0,0; 7,0,0,0</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
						<!-- semicolon separated, whitespaces are ignored -->
						<!-- the last version number is taken as default -->
					</versions>
				</property>
				<property name="Autostart">
					<choosable>
						<xsl:choose>
							<xsl:when test="@autostart = 'choosable'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>false</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</choosable>
					<default>
						<xsl:choose>
							<xsl:when test="@autostart= 'false'">
								<xsl:text>false</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>true</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</default>
				</property>
				<property name="Loop">
					<choosable>
						<xsl:choose>
							<xsl:when test="@loop = 'choosable'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>false</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</choosable>
					<default>
						<xsl:choose>
							<xsl:when test="@loop = 'true'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>false</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</default>
				</property>
				<property name="FlashVariables">
					<choosable>true</choosable>
					<variables>
						<xsl:if test="@variables"><xsl:value-of select="@variables"/></xsl:if>
					</variables>
				</property>
				<property name="Quality">
					<choosable>
						<xsl:choose>
							<xsl:when test="@quality_choosable = 'true'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</choosable>
					<default>best</default>
					<values><!-- semicolon separated, whitespaces are ignored-->low; medium; high;
						best</values>
				</property>
				<property name="Scale">
					<choosable>
						<xsl:choose>
							<xsl:when test="@scale_choosable = 'true'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</choosable>
					<default>exactfit</default>
					<values><!-- semicolon separated, whitespaces are ignored-->showall; noborder;
						exactfit</values>
				</property>
				<property name="Transparency">
					<choosable>
						<xsl:choose>
							<xsl:when test="@transparency_choosable = 'true'">
								<xsl:text>true</xsl:text>
							</xsl:when>
							<xsl:otherwise>false</xsl:otherwise>
						</xsl:choose>
					</choosable>
					<default>n/a</default>
					<values><!-- semicolon separated, whitespaces are ignored-->n/a; window; opaque;
						transparent</values>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</flash>
	</xsl:template>
	
	
	
	
	<xsl:template match="aggregation">
		<xsl:variable name="elementname">aggregation</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Datenbank Komponente</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.DatabaseComponent</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="footer">
		<footer dcfname="footer" label="Footer">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Footer</classname>
			</dcfConfig>
			<dcfInitial>
				<pdf/>
				<mail-to-a-friend/>
				<favorite/>
				<printView/>
				<zoom/>
			</dcfInitial>
		</footer>
	</xsl:template>

	<xsl:template match="iframe">
		<iframe>
			<iframeSrc dcfname="iframeSrc" label="http Adresse des iframes">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</iframeSrc>
			<iframeName dcfname="iframeName" label="Name des iframes">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</iframeName>
			<iframeWidth dcfname="iframeWidth" label="Breite des iframes">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</iframeWidth>
			<iframeHeight dcfname="iframeHeight" label="Höhe des iframes">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</iframeHeight>
		</iframe>
	</xsl:template>

	<xsl:template match="simpletextfield">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>simpletextfield</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:value-of select="."/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

<!--HTML Area / (= beschreibbare text Area) -->
	<xsl:template match="htmlarea">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>htmlarea</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleHTMLArea</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:value-of select="."/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="tmenu">
		<tmenu dcfname="traverseMenu" label="Links auf 'vorherige' und 'nchste' Seite anzeigen">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<property name="checkboxes">
					<label>anzeigen</label>
					<elementName>show</elementName>
					<properties>
						<navigation since="next" depth="1"/>
						<navigation since="prev" depth="1"/>
					</properties>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</tmenu>
	</xsl:template>


	<xsl:template match="next">
		<next dcfname="next" label="Link auf folgende Seite anzeigen?">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<property name="checkboxes">
					<label>anzeigen</label>
					<elementName>show</elementName>
					<properties>
					    <navigation since="next" depth="1"/>
					</properties>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</next>
	</xsl:template>

	<xsl:template match="date | simpleDate">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="name()"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDate</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
			<xsl:choose>
				<xsl:when test="@actual='true'">
					<day>
						<xsl:call-template name="getDate">
							<!-- hier auf "dd" umstellen, wenn  Datumskomponente 8 stellig wird-->
							<xsl:with-param name="format" select="'d'"/>
						</xsl:call-template>
					</day>
					<month>
						<xsl:call-template name="getDate">
							<!-- hier auf "MM" umstellen, wenn  Datumskomponente 8 stellig wird-->
							<xsl:with-param name="format" select="'M'"/>
						</xsl:call-template>
					</month>
					<year>
						<xsl:call-template name="getDate">
							<xsl:with-param name="format" select="'yyyy'"/>
						</xsl:call-template>						
					</year>
				</xsl:when>
				<xsl:otherwise>					
					<xsl:apply-templates />
				</xsl:otherwise>
			</xsl:choose>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="paragraph">
		<iteration dcfname="iteration" label="Abstze">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Iteration</classname>
				<mandatory>false</mandatory>
				<property name="mincount">
					<value>0</value>
				</property>
				<property name="maxcount">
					<value>30</value>
				</property>
				<property name="descriptionDcfname">
					<value>subhead</value>
				</property>
				<iterationElements>
					<subhead dcfname="subhead" label="(Absatz-)berschrift">
						<dcfConfig>
							<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
							<mandatory>false</mandatory>
						</dcfConfig>
						<dcfInitial/>
					</subhead>
					<picture dcfname="picture" label="(Absatz-)Bild">
						<dcfConfig>
							<classname>de.juwimm.cms.content.modules.Picture</classname>
							<mandatory>false</mandatory>
						</dcfConfig>
						<dcfInitial/>
					</picture>
					<content dcfname="text" label="(Absatz-)Textfeld">
						<dcfConfig>
							<classname>de.juwimm.cms.content.modules.Textfield</classname>
							<!-- For ConQuest 3.0 you need to outcomment this area: -->
							<classpath>
								<jar><xsl:value-of select="$cqcms-juwimm-plugin-wysiwyg"/></jar>
							</classpath> 
							<property name="Size">
								<height>300</height>
							</property>
							<xsl:choose>
								<xsl:when test="not(child::*)">
									<xsl:apply-templates select="." mode="allowall"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates mode="allow"/>
								</xsl:otherwise>
							</xsl:choose>
						</dcfConfig>
						<dcfInitial>
							<br/>
						</dcfInitial>
					</content>
				</iterationElements>
			</dcfConfig>
			<dcfInitial>
				<item id="1">
					<subhead dcfname="subhead">Neuer Absatz</subhead>
					<picture dcfname="picture">
					</picture>
					<content dcfname="text">Hier beginnt ein neuer Absatz</content>
				</item>
			</dcfInitial>
		</iteration>
	</xsl:template>

	<xsl:template match="iteration">
		<!--Elementname bestimmen-->
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>iteration</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--Element schreiben-->
		<xsl:element name="{$elementname}">
			<!--Elementattribute schreiben-->
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<!--dcfconfig fuer iterationem-->
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Iteration</classname>
				<mandatory>false</mandatory>
				<xsl:apply-templates select="@*" mode="property"/>
				<iterationElements>
					<xsl:apply-templates/>
				</iterationElements>
			</dcfConfig>
			<dcfInitial>
				<item id="1">
					<xsl:apply-templates mode="dcfinitial"/>
				</item>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="@name | @dcfname | @label" mode="property">
	</xsl:template>

	<xsl:template match="@*" mode="property">
		<property name="{name()}">
			<value>
				<xsl:value-of select="."/>
			</value>
		</property>
	</xsl:template>

	<xsl:template match="*" mode="dcfinitial">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="name()"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--Element schreiben-->
		<xsl:element name="{$elementname}">
			<!--DCF Name des Elementes schreiben-->
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<!--dcf Iniital Wert setzen-->

			<xsl:apply-templates mode="initial"/>
		</xsl:element>
	</xsl:template>
	<!--Fuer dcfinitial keine dcfConfig und dcfInital Elemente von Kindelementen der Iteration auslesen. -->
	<xsl:template match="dcfConfig | dcfCnitial" mode="initial">
	</xsl:template>


	<xsl:template match="anchor">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="name()"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Ankername</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Anchor</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:value-of select="."/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="internalLink">
		<internalLink>
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Interner Link</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>internalLink</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.InternalLink</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
				<!-- properties -->
				<xsl:choose>
				<xsl:when test="not(@anchor) or @anchor='true'">				
					<property name="anchor">
						<visible>true</visible>
					</property>
				</xsl:when>
					<xsl:otherwise>				
						<property name="anchor">
							<visible>false</visible>
						</property>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="@editable or @popupAvailable">
					<property name="DisplayType">
						<xsl:if test="@editable">						
							<editable>
								<xsl:value-of select="@editable"/>
							</editable>
						</xsl:if>
						<xsl:if test="@popupAvailable">						
							<popupAvailable>
								<xsl:value-of select="@popupAvailable"/>
							</popupAvailable>
						</xsl:if>
					</property>					
				</xsl:if>
			</dcfConfig>
			<dcfInitial/>
		</internalLink>
	</xsl:template>

	<xsl:template match="cinclude">
		<cinclude:includexml xmlns:cinclude="http://apache.org/cocoon/include/1.0" ignoreErrors="true">
			<cinclude:src dcfname="includeUrl" label="URL fr Include">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</cinclude:src>
		</cinclude:includexml>
	</xsl:template>

<!-- START checkboxen -->
	<xsl:template match="checkBox">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>checkBox</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Auswahl</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<xsl:apply-templates select="box" mode="checkBox"/>
			</dcfConfig>
			<dcfInitial>
				<xsl:for-each select="box[@selected='true']">
					<xsl:variable name="boxname">
						<xsl:call-template name="getBoxElementName"/>
					</xsl:variable>
					<xsl:element name="{$boxname}">						
						<xsl:apply-templates />
					</xsl:element>
				</xsl:for-each>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="box" mode="checkBox" priority="-1">
		<xsl:variable name="elementname">
			<xsl:call-template name="getBoxElementName"/>
		</xsl:variable>
		<property name="checkboxes">
			<label>
				<xsl:value-of select="@label"/>
			</label>
			<elementName>
				<xsl:value-of select="$elementname"/>
			</elementName>
			<properties>
                <xsl:apply-templates/>
			</properties>
		</property>
	</xsl:template>

	<xsl:template name="getBoxElementName">
			<xsl:choose>
				<xsl:when test="@elementname">
					<xsl:value-of select="@elementname"/>
				</xsl:when>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>box</xsl:otherwise>
			</xsl:choose>
	</xsl:template>	
<!-- END checkboxen -->

<!-- for servicelists -->
<xsl:template match="services">
	<xsl:variable name="elementname">
		<xsl:choose>
			<xsl:when test="@name">
				<xsl:value-of select="@name"/>
			</xsl:when>
			<xsl:otherwise>checkBox</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:element name="{$elementname}">
		<xsl:attribute name="label">
			<xsl:choose>
				<xsl:when test="@label">
					<xsl:value-of select="@label"/>
				</xsl:when>
				<xsl:otherwise>Auswahl</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="dcfname">
			<xsl:choose>
				<xsl:when test="@dcfname">
					<xsl:value-of select="@dcfname"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$elementname"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<dcfConfig>
			<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
            <xsl:call-template name="serviceitem"/>
		</dcfConfig>
		<dcfInitial/>
	</xsl:element>
	
	<xsl:for-each select="/source/all/subServiceDefs">
	    <xsl:variable name="actName"><xsl:value-of select="@name"/></xsl:variable>
        <xsl:element name="subService">
    		<xsl:attribute name="label">
    			<xsl:choose>
    				<xsl:when test="@label">
    					<xsl:value-of select="@label"/>
    				</xsl:when>
    				<xsl:otherwise>Auswahl <xsl:value-of select="/source/all/serviceDefs/serviceDef[@name=$actName]"/></xsl:otherwise>
    			</xsl:choose>
    		</xsl:attribute>
    		<xsl:attribute name="dcfname">sub<xsl:value-of select="@name"/></xsl:attribute>
    		<dcfConfig>
    			<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
                <xsl:apply-templates select="serviceDef" mode="subServiceDef"/>
    		</dcfConfig>
    		<dcfInitial/>
    	</xsl:element>	
	</xsl:for-each>

</xsl:template>

<xsl:template name="serviceitem">
	<xsl:for-each select="/source/all/serviceDefs/serviceDef">
    	<property name="checkboxes">
    		<label><xsl:value-of select="."/></label>
    		<elementName><xsl:value-of select="@name"/></elementName>
    		<properties><xsl:value-of select="."/></properties>
    	</property>
	</xsl:for-each>
</xsl:template>

<xsl:template match="serviceDef" mode="subServiceDef">
    <property name="checkboxes">
    	<label><xsl:value-of select="."/></label>
    	<elementName><xsl:value-of select="@name"/></elementName>
    	<properties><xsl:value-of select="."/></properties>
    </property>
</xsl:template>

<xsl:template match="deployStartStop">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>deployStartStop</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Deploy Start/Stop</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.DeployStartStop</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
</xsl:template>



	<xsl:template match="externalLink">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>externalLink</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Externer Link</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.ExternalLink</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
				<!-- properties -->
				<xsl:if test="@editable or @popupAvailable">
					<property name="DisplayType">
						<xsl:if test="@editable">						
							<editable>
								<xsl:value-of select="@editable"/>
							</editable>
						</xsl:if>
						<xsl:if test="@popupAvailable">						
							<popupAvailable>
								<xsl:value-of select="@popupAvailable"/>
							</popupAvailable>
						</xsl:if>
					</property>					
				</xsl:if>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>


	<xsl:template match="abstractLink">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>abstractLink</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Link</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.AbstractLink</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory"/>
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
				<!-- properties -->
				<xsl:if test="not(@anchor) or @anchor='true'">				
					<property name="anchor">
						<visible>true</visible>
					</property>
				</xsl:if>
				<xsl:if test="@editable or @popupAvailable">
					<property name="DisplayType">
						<xsl:if test="@editable">						
							<editable>
								<xsl:value-of select="@editable"/>
							</editable>
						</xsl:if>
						<xsl:if test="@popupAvailable">						
							<popupAvailable>
								<xsl:value-of select="@popupAvailable"/>
							</popupAvailable>
						</xsl:if>
					</property>					
				</xsl:if>
			</dcfConfig>
			<dcfInitial>
				<xsl:apply-templates/>
			</dcfInitial>
		</xsl:element>
	</xsl:template>
	



<xsl:template match="time">
	<xsl:variable name="elementname">
		<xsl:choose>
			<xsl:when test="@name">
				<xsl:value-of select="@name"/>
			</xsl:when>
			<xsl:otherwise>time</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:element name="{$elementname}">
		<xsl:attribute name="label">
			<xsl:choose>
				<xsl:when test="@label">
					<xsl:value-of select="@label"/>
				</xsl:when>
				<xsl:otherwise>Zeit</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<xsl:attribute name="dcfname">
			<xsl:choose>
				<xsl:when test="@dcfname">
					<xsl:value-of select="@dcfname"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$elementname"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
		<dcfConfig>
			<classname>de.juwimm.cms.content.modules.TimePicker</classname>
			<xsl:if test="@pattern">
				<property name="customTimeFormat">
					<displayPattern><xsl:value-of select="@pattern"/></displayPattern>
				</property>
			</xsl:if>
		</dcfConfig>
		<dcfInitial>
			<xsl:apply-templates/>
		</dcfInitial>
	</xsl:element>
</xsl:template>

<!-- Dropdownboxes -->
<!-- sample: 
	<select name="targetgroup"><option value="Alle">Alle	(Internet)</option> <option ...
 -->	
	<xsl:template match="select" name="select">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:otherwise>select</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>Auswahl</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>true</mandatory>
				<xsl:apply-templates select="option" mode="select"/>
			</dcfConfig>
			<dcfInitial/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="option" mode="select">
		<property name="dropdownValues">
			<name>
				<xsl:value-of select="."/>
			</name>
			<value>
				<xsl:choose>
					<xsl:when test="@value"><xsl:value-of select="@value"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
				</xsl:choose>
			</value>
		</property>		
	</xsl:template>
	

<xsl:template match="terminType">
	<terminType dcfname="terminType" label="Auswahl der Veranstaltungsart">
		<dcfConfig>
			<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
			<mandatory>true</mandatory>
			<property name="dropdownValues">
				<name>Vorlesung (VL)</name>
				<value>Vorlesung</value>
			</property>
			<property name="dropdownValues">
				<name>Seminar (S)</name>
				<value>Seminar</value>
			</property>
			<property name="dropdownValues">
				<name>Praktikum (P)</name>
				<value>Praktikum</value>
			</property>
			<property name="dropdownValues">
				<name>Praktikum/Seminar (PS)</name>
				<value>Prak./Sem.</value>
			</property>
			<property name="dropdownValues">
				<name>Unterricht am Krankenbett (UAK)</name>
				<value>UAK</value>
			</property>
			<property name="dropdownValues">
				<name>Problemorientiertes Lernen (POL)</name>
				<value>POL</value>
			</property>
			<property name="dropdownValues">
				<name>Keine Angabe</name>
				<value></value>
			</property>
		</dcfConfig>
		<dcfInitial/>
	</terminType>
</xsl:template>

<xsl:template match="weekdayNames">
	<weekdayNames dcfname="weekdayNames" label="Wochentag">
		<dcfConfig>
			<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
			<mandatory>true</mandatory>
			<property name="dropdownValues">
				<name>Montag</name>
				<value>0</value>
			</property>
			<property name="dropdownValues">
				<name>Dienstag</name>
				<value>1</value>
			</property>
			<property name="dropdownValues">
				<name>Mittwoch</name>
				<value>2</value>
			</property>
			<property name="dropdownValues">
				<name>Donnerstag</name>
				<value>3</value>
			</property>
			<property name="dropdownValues">
				<name>Freitag</name>
				<value>4</value>
			</property>
			<property name="dropdownValues">
				<name>Samstag</name>
				<value>5</value>
			</property>
			<property name="dropdownValues">
				<name>Sonntag</name>
				<value>6</value>
			</property>
		</dcfConfig>
		<dcfInitial/>
	</weekdayNames>
</xsl:template>

<xsl:template match="shape">
		<!-- fuer die occupationn gibt es eine definiert Liste mit Moeglichkeiten-->
			<shape dcfname="shape" label="Auswahl der Form (shape)">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
					<mandatory>true</mandatory>
					<property name="dropdownValues">
						<name>rect</name>
						<value>rect</value>
					</property>
					<property name="dropdownValues">
						<name>circle</name>
						<value>circle</value>
					</property>
					<property name="dropdownValues">
						<name>poly</name>
						<value>poly</value>
					</property>
				</dcfConfig>
				<dcfInitial/>
			</shape>
</xsl:template>



<!--dynamische Unitlisten in dcfs -->
<xsl:template match="config" mode="choose">
       <xsl:param name="label">Bitte ein Unit auswählen</xsl:param>
       <unitChoose dcfname="unitChoose" label="{$label}">
           <dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
					<mandatory>false</mandatory>
					<xsl:apply-templates select="unitList/unit" mode="choose">
					    <xsl:sort data-type="number" order="ascending" select="@id"/>
					</xsl:apply-templates>    
			</dcfConfig>
			<dcfInitial/>							
       </unitChoose>   
</xsl:template>

<xsl:template match="unit" mode="choose">
		<property name="dropdownValues">
			<name><xsl:value-of select="."/></name>
			<value><xsl:value-of select="@id"/></value>
		</property>
</xsl:template>

<!-- simple webcam -->
<xsl:template match="webcam">
    <webcam>
	    <url dcfname="webcamImage" label="URL des WebCam Bildes">
        	<dcfConfig>
        		<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
        		<mandatory>false</mandatory>
        	</dcfConfig>
        	<dcfInitial></dcfInitial>
        </url>
        <refreshrate dcfname="webcamImageRefresh" label="Nach wieviel Sekunden soll das Bild erneut geladen werden?">
        	<dcfConfig>
        		<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
        		<mandatory>false</mandatory>
        	</dcfConfig>
        	<dcfInitial></dcfInitial>            
        </refreshrate>
	</webcam>
</xsl:template>

<!-- ====== -->
<xsl:template match="flash">
    <flash>
	    <url dcfname="flashMovie" label="URL des Flash-Films">
        	<dcfConfig>
        		<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
        		<mandatory>false</mandatory>
        	</dcfConfig>
        	<dcfInitial>http://</dcfInitial>
        </url>
	</flash>
</xsl:template>

<!-- Form Elements  -->

<!--Dropdown Form Field Required ? -->
<xsl:template match="requiredChoose">
       <requiredChoose dcfname="requiredChoose" label="Feld ist ein Pflichtfeld?">
           <dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
					<mandatory>false</mandatory>
		<property name="dropdownValues">
			<name>Ja</name>
			<value>true</value>
		</property>
		<property name="dropdownValues">
			<name>Nein</name>
			<value>false</value>
		</property>					
		</dcfConfig>
		<dcfInitial/>							
		</requiredChoose>  
</xsl:template>

	<xsl:template match="form-listelement-list">
		<form-listelement-list dcfname="form-listelement-list" label="Feld Element">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>false</mandatory>
				<property name="dropdownValues">
					<name>Radiobuttons</name>
					<value>radio</value>
				</property>
				<property name="dropdownValues">
					<name>Radiobuttons horizontal</name>
					<value>radioHorizontal</value>
				</property>
				<property name="dropdownValues">
					<name>Checkboxen</name>
					<value>checkbox</value>
				</property>
				<property name="dropdownValues">
					<name>Textfeld</name>
					<value>textfield</value>
				</property>
				<property name="dropdownValues">
					<name>imageViewer</name>
					<value>imageViewer</value>
				</property>
			</dcfConfig>
		</form-listelement-list>
	</xsl:template>

<xsl:template match="form-element-list">
	<form-element-list dcfname="form-element-list" label="Feld Element">
    	<dcfConfig>
			<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
			<mandatory>false</mandatory>
			<property name="dropdownValues">
				<name>Name</name>
				<value>name</value>
			</property>
			<property name="dropdownValues">
				<name>Vorname</name>
				<value>vorname</value>
			</property>			
			<property name="dropdownValues">
				<name>Tel.(Privat)</name>
				<value>tel</value>
			</property>			
			<property name="dropdownValues">
				<name>Fax</name>
				<value>fax</value>
			</property>			
			<property name="dropdownValues">
				<name>E-Mail</name>
				<value>email</value>
			</property>			
			<property name="dropdownValues">
				<name>E-Mail Kontrolleingabe</name>
				<value>email2</value>
			</property>			
			<property name="dropdownValues">
				<name>Infotext</name>
				<value>infotext</value>
			</property>			
			<property name="dropdownValues">
				<name>Strasse</name>
				<value>strasse</value>
			</property>			
			<property name="dropdownValues">
				<name>PLZ</name>
				<value>plz</value>
			</property>			
			<property name="dropdownValues">
				<name>Ort</name>
				<value>ort</value>
			</property>				
			<property name="dropdownValues">
				<name>Attachment</name>
				<value>attachment</value>
			</property>			
			<property name="dropdownValues">
				<name>Geburtsdatum</name>
				<value>bday</value>
			</property>			
			<property name="dropdownValues">
				<name>Anrede</name>
				<value>anrede</value>
			</property>			
			<property name="dropdownValues">
				<name>Titel</name>
				<value>titel</value>
			</property>
			<property name="dropdownValues">
				<name>Blutgruppe</name>
				<value>blutgruppe</value>
			</property>	
			<property name="dropdownValues">
				<name>Tel.(geschäflich)</name>
				<value>gtel</value>
			</property>
			<property name="dropdownValues">
				<name>Handy</name>
				<value>handy</value>
			</property>
			<property name="dropdownValues">
				<name>Anfragetextfeld</name>
				<value>anfragetext</value>
			</property>
		    <property name="dropdownValues">
				<name>Geschlecht</name>
				<value>geschlecht</value>
		    </property>
	        <property name="dropdownValues">
	           <name>Url</name>
	           <value>url</value>
	        </property>
	        <property name="dropdownValues">
	           <name>Einrichtung</name>
	           <value>unit</value>
	        </property>
	       	<property name="dropdownValues">
	        	<name>Feedback-Block</name>
	           	<value>feedback-block</value>
	       	</property>
    		<property name="dropdownValues">
    			<name>Feedback-Nutzerdaten</name>
    			<value>feedback-user</value>
    		</property>	 
    		<property name="dropdownValues">
    			<name>FFWoher</name>
    			<value>ffwoher</value>
    		</property>  
    		<property name="dropdownValues">
    			<name>ginsenginfo</name>
    			<value>ginsenginfo</value>
    		</property>    
    		<property name="dropdownValues">
    			<name>ginsenginfo2</name>
    			<value>ginsenginfo2</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>Krankenhausname</name>
    			<value>krankenhausname</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>Fachgebiet</name>
    			<value>fachgebiet</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>Land</name>
    			<value>land</value>
    		</property> 
    		<property name="dropdownValues">
    			<name>input1</name>
    			<value>input1</value>
    		</property>     
    		<property name="dropdownValues">
    			<name>input2</name>
    			<value>input2</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>input3</name>
    			<value>input3</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>input4</name>
    			<value>input4</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>input5</name>
    			<value>input5</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>order</name>
    			<value>order</value>
    		</property>  
    		<property name="dropdownValues">
    			<name>Anzahl Schilder</name>
    			<value>schilderanzahl</value>
    		</property> 
    		<property name="dropdownValues">
    			<name>Zuweiserpraxis</name>
    			<value>zuweiserpraxis</value>
    		</property>   
    		<property name="dropdownValues">
    			<name>Newsletter bestellen</name>
    			<value>newsletter</value>
    		</property>   
    		<xsl:apply-templates select="." mode="include"/>	
    	</dcfConfig>
		<dcfInitial/>							
	</form-element-list>  
</xsl:template>

	
	<xsl:template match="minipage" priority="-0.5">
		<minipage>
			<minipage-head dcfname="minipage-head" label="Minipage-Überschrift">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</minipage-head>
			<minipage-picture dcfname="minipage-picture" label="Minipage-Bild">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.Picture</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</minipage-picture>
			<internalLink label="Interner Link" dcfname="internalLinkMinipage">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.InternalLink</classname>
					<mandatory>false</mandatory>
				</dcfConfig>
				<dcfInitial/>
			</internalLink>
			<minipage-content dcfname="minipage-content" label="Minipage-Text">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.Textfield</classname>
					<!-- For ConQuest 3.0 you need to outcomment this area: -->
					<classpath>
						<jar><xsl:value-of select="$cqcms-juwimm-plugin-wysiwyg"/></jar>
					</classpath> 
					<property name="Size">
						<height>400</height>
					</property>
					<property name="AllowedElements">
						<rootnodeName>subline</rootnodeName>
						<name>Zwischen?berschrift</name>
						<class>de.juwimm.cms.content.modules.TxtSubline</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>line</rootnodeName>
						<name>Linie</name>
						<class>de.juwimm.cms.content.modules.Line</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>documents</rootnodeName>
						<name>Dokumente</name>
						<class>de.juwimm.cms.content.modules.Documents</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>picture</rootnodeName>
						<name>Bild</name>
						<class>de.juwimm.cms.content.modules.Picture</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>anchor</rootnodeName>
						<name>Anker</name>
						<class>de.juwimm.cms.content.modules.Anchor</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>top</rootnodeName>
						<name>Link nach oben</name>
						<class>de.juwimm.cms.content.modules.Line</class>
						<properties>
							<property name="image">
								<iconImage>16_nachoben.gif</iconImage>
								<paneImage>16_nachoben.gif</paneImage>
							</property>
						</properties>
					</property>
					<property name="AllowedElements">
						<rootnodeName>internalLink</rootnodeName>
						<name>Interner Link</name>
						<class>de.juwimm.cms.content.modules.InternalLink</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>externalLink</rootnodeName>
						<name>Externer Link</name>
						<class>de.juwimm.cms.content.modules.ExternalLink</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>aggregation</rootnodeName>
						<name>Personendatenbank</name>
						<class>de.juwimm.cms.content.modules.DatabaseComponent</class>
					</property>
					<property name="AllowedElements">
						<rootnodeName>literaturangabe</rootnodeName>
						<name>Literaturangabe</name>
						<class>de.juwimm.cms.content.modules.Literature</class>
					</property>
				</dcfConfig>
				<dcfInitial>
					<br/>
				</dcfInitial>
			</minipage-content>
		</minipage>
	</xsl:template>
	
	<xsl:template match="newsArchive">
		<!-- Dropdown-Field für Datum -->
		<newsArchive dcfname="newsArchive" label="Welche News sollen angezeigt werden?">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>true</mandatory>
				<property name="dropdownValues">
					<name>Live</name>
					<value>1</value>
				</property>
				<property name="dropdownValues">
					<name>Archiv</name>
					<value>2</value>
				</property>     
				<property name="dropdownValues">
					<name>Alle anzeigen</name>
					<value>3</value>
				</property>   	
			</dcfConfig>
			<dcfInitial/>
		</newsArchive>
	</xsl:template>
	
	<!-- for Templates with one teaser -->
	<xsl:template match="singleTeaserInclude" priority="-0.5">
		<teaserInclude>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname != ''">
						<xsl:value-of select="@dcfname"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>teaserInclude</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label != ''">
						<xsl:value-of select="@label"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>Teaser include</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Teaser</classname>
				<property name="SearchScope">
					<name>Global Teaser</name>
					<xpathTeaserElement>//teaser</xpathTeaserElement>
					<!-- TODO erst einmal auskommentiert, da nicht klar, ob multiTeaserDefinition jemals wieder aktuell wird
						<xpathTeaserName>./@description</xpathTeaserName>-->
					<unit>root</unit>
					<!-- 
						unit kann sein: root 
						this 
						parent 
						global 
						wenn unit fehlt oder global ist, dann ist dieser scope für die ganze site 
					-->
					<randomizeable>true</randomizeable>
				</property>
				<property name="SearchScope">
					<name>Teaser of this product (parent unit)</name>
					<xpathTeaserElement>//teaser</xpathTeaserElement>
					<!-- TODO erst einmal auskommentiert, da nicht klar, ob multiTeaserDefinition jemals wieder aktuell wird
						<xpathTeaserName>./@description</xpathTeaserName>-->
					<unit>parent</unit>
					<randomizeable>true</randomizeable>
				</property>
				<property name="SearchScope">
					<name>Teaser of this target group (this unit)</name>
					<xpathTeaserElement>//teaser</xpathTeaserElement>
					<!-- TODO erst einmal auskommentiert, da nicht klar, ob multiTeaserDefinition jemals wieder aktuell wird
						<xpathTeaserName>./@description</xpathTeaserName>-->
					<unit>this</unit>
					<randomizeable>true</randomizeable>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</teaserInclude>
	</xsl:template>
	
	<xsl:template match="multiTeaserInclude" priority="-0.5">
		<teaserInclude dcfname="teaserInclude" label="Teaser include">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.Teaser</classname>
				<property name="SearchScope">
					<name>Globale Teaser</name>
					<xpathTeaserElement>//teasers/item</xpathTeaserElement>
					<xpathTeaserName>./@description</xpathTeaserName>
					<xpathTeaserIdentifier>./@id</xpathTeaserIdentifier>
					<unit>root</unit>
					<!-- 
						unit kann sein: root 
						this 
						parent 
						global 
						wenn unit fehlt oder global ist, dann ist dieser scope für die ganze site 
					-->
					<randomizeable>true</randomizeable>
				</property>
				<property name="SearchScope">
					<name>Teaser dieses Produktes</name>
					<xpathTeaserElement>//teasers/item</xpathTeaserElement>
					<xpathTeaserName>./@description</xpathTeaserName>
					<xpathTeaserIdentifier>./@id</xpathTeaserIdentifier>
					<unit>this</unit>
					<randomizeable>true</randomizeable>
				</property>
				<property name="SearchScope">
					<name>Teaser dieses Produktes</name>
					<xpathTeaserElement>//teasers/item</xpathTeaserElement>
					<xpathTeaserName>./@description</xpathTeaserName>
					<unit>parent</unit>
					<randomizeable>true</randomizeable>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</teaserInclude>
	</xsl:template>

<!-- Leerausgabe -->
<xsl:template match="form-element-list" mode="include" priority="-1"/>


</xsl:stylesheet>