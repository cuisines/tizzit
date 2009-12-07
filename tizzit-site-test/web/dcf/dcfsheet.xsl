<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wiki="http://plugins.tizzit.org/ConfluenceContentPlugin" >

    <xsl:include href="../../../tizzit-site-master/web/dcf/dcfsheet-master.xsl"/>

    <xsl:template match="textfield" mode="allowall" priority="1">
    	<xsl:call-template name="subline"/>
    	<xsl:call-template name="line"/>
    	<xsl:call-template name="picture"/>
    	<xsl:call-template name="documents"/>
    	<xsl:call-template name="anchor"/>
    	<xsl:call-template name="top"/>
    	<xsl:call-template name="internalLink"/>
    	<xsl:call-template name="externalLink"/>
    	<xsl:call-template name="aggregation"/>
        <xsl:call-template name="flash_dialog"/>
    </xsl:template>

	<xsl:template match="form-element-list">
		<form-element-list dcfname="form-element-list" label="Feld Element">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>false</mandatory>
				<property name="dropdownValues">
					<name>Name</name>
					<value>standard_name</value>
				</property>
				<property name="dropdownValues">
					<name>Vorname</name>
					<value>standard_vorname</value>
				</property>
				<property name="dropdownValues">
					<name>E-Mail</name>
					<value>standard_email</value>
				</property>
				<property name="dropdownValues">
					<name>Strasse</name>
					<value>standard_strasse</value>
				</property>
				<property name="dropdownValues">
					<name>PLZ</name>
					<value>standard_plz</value>
				</property>
				<property name="dropdownValues">
					<name>Ort</name>
					<value>standard_ort</value>
				</property>
				<property name="dropdownValues">
					<name>Textfeld</name>
					<value>standard_textarea</value>
				</property>
				<xsl:apply-templates select="." mode="include"/>
			</dcfConfig>
			<dcfInitial/>
		</form-element-list>
	</xsl:template>

	<xsl:template match="wikiURLs">
		<wikiURLs dcfname="wikiURL" label="Wiki Urls">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>false</mandatory>
				<property name="dropdownValues">
					<name>http://wiki.tizzit.org</name>
					<value>http://wiki.tizzit.org</value>
				</property>
				<xsl:apply-templates select="." mode="include"/>
			</dcfConfig>
			<dcfInitial/>
		</wikiURLs>
	</xsl:template>

	<xsl:template match="web20">
		<web20 label="WEB 2.0 Settings" dcfname="web20">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<property name="checkboxes">
					<label>Tagging</label>
					<elementName>tagging</elementName>
					<properties>true</properties>
				</property>
			</dcfConfig>
		</web20>
	</xsl:template>

	<xsl:template match="tagCloud">
		<tagcloud label="WEB 2.0 Tagcloud" dcfname="tagcloud">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<property name="checkboxes">
					<label>Tagcloud anzeigen?</label>
					<elementName>tagcloud</elementName>
					<properties>true</properties>
				</property>
			</dcfConfig>
		</tagcloud>
	</xsl:template>

	<xsl:template match="requiredChoose" priority="2">
		<requiredChoose dcfname="requiredChoose" label="Feld ist ein Pflichtfeld?">
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
				<mandatory>true</mandatory>
				<property name="dropdownValues">
					<name>Ja</name>
					<value>false</value>
				</property>
				<property name="dropdownValues">
					<name>Nein</name>
					<value>true</value>
				</property>
			</dcfConfig>
			<dcfInitial/>
		</requiredChoose>
	</xsl:template>

	<xsl:template match="sn_footer" priority="2">
		<sn_footer>
			<display label="Social Network" dcfname="display">
				<dcfConfig>
					<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
					<property name="checkboxes">
						<label>Display Social Network Icons?</label>
						<elementName>display</elementName>
						<properties>true</properties>
					</property>
				</dcfConfig>
				<dcfInitial>true</dcfInitial>
			</display>
			<xsl:apply-templates/>
		</sn_footer>
	</xsl:template>

	<!-- CONFLUENCE PLUGIN -->
	<xsl:template match="wiki:simpletextfield" priority="2.0">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name" />
				</xsl:when>
				<xsl:otherwise>simpletextfield</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}" namespace="http://plugins.tizzit.org/ConfluenceContentPlugin">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label" />
					</xsl:when>
					<xsl:otherwise>Text</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.SimpleTextfield</classname>
				<mandatory>
					<xsl:choose>
						<xsl:when test="@mandatory">
							<xsl:value-of select="@mandatory" />
						</xsl:when>
						<xsl:otherwise>false</xsl:otherwise>
					</xsl:choose>
				</mandatory>
			</dcfConfig>
			<dcfInitial>
				<xsl:value-of select="." />
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="wiki:checkBox" priority="2">
		<xsl:variable name="elementname">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name" />
				</xsl:when>
				<xsl:otherwise>checkBox</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{$elementname}" namespace="http://plugins.tizzit.org/ConfluenceContentPlugin">
			<xsl:attribute name="label">
				<xsl:choose>
					<xsl:when test="@label">
						<xsl:value-of select="@label" />
					</xsl:when>
					<xsl:otherwise>Auswahl</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="dcfname">
				<xsl:choose>
					<xsl:when test="@dcfname">
						<xsl:value-of select="@dcfname" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$elementname" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<dcfConfig>
				<classname>de.juwimm.cms.content.modules.CheckBoxModule</classname>
				<xsl:apply-templates mode="checkBox" select="wiki:box" />
			</dcfConfig>
			<dcfInitial>
				<xsl:for-each select="wiki:box[@selected='true']">
					<xsl:variable name="boxname">
						<xsl:call-template name="getBoxElementName" />
					</xsl:variable>
					<xsl:element name="{$boxname}" namespace="http://plugins.tizzit.org/ConfluenceContentPlugin">
						<xsl:apply-templates />
					</xsl:element>
				</xsl:for-each>
			</dcfInitial>
		</xsl:element>
	</xsl:template>

	<xsl:template match="wiki:box" mode="checkBox" priority="2">
		<xsl:variable name="elementname">
			<xsl:call-template name="getBoxElementName" />
		</xsl:variable>
		<property name="checkboxes">
			<label>
				<xsl:value-of select="@label" />
			</label>
			<elementName>
				<xsl:value-of select="$elementname" />
			</elementName>
			<properties>
				<xsl:apply-templates />
			</properties>
		</property>
	</xsl:template>
</xsl:stylesheet>