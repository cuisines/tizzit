<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
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
	
</xsl:stylesheet>