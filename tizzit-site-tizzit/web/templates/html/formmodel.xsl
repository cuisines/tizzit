<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">

    <xsl:include href="../../../../tizzit-site-master/web/templates/formmodel.xsl"/>
    
    <!-- Neues Formularkonzept von Sven, muss noch getestet werden! -->
    <!-- * Validierungen alle via Javascript -->
    <!-- * Alle Templates unten ausgelagert -->
    <!-- * Funktionierende Error Messages auch wenn man nichts ausfüllt -->
    <!-- * Error Box am Anfang der Seite -->
    <!-- * Einheitliches Formulardesign -->
    <!-- * Einfaches einfügen neuer Widgets -->
    
    <xsl:template match="source" priority="2">
        <fd:form xmlns:fd="http://apache.org/cocoon/forms/1.0#definition" xmlns:i18n="http://apache.org/cocoon/i18n/2.1">
            <fd:widgets>
                <!-- Error Frame kommt an den Anfang des Formulars -->
                <fd:field id="standard_errorframe" required="false" state="invisible">
                    <fd:datatype base="string"/>
                </fd:field>
                <xsl:apply-templates select="all/content/iteration"/>        
            </fd:widgets>
        </fd:form>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_name']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_name'"/>
            <xsl:with-param name="widget_label" select="'Name'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_vorname']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_vorname'"/>
            <xsl:with-param name="widget_label" select="'Vorname'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_email']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_email'"/>
            <xsl:with-param name="widget_label" select="'E-Mail'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_strasse']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_strasse'"/>
            <xsl:with-param name="widget_label" select="'Strasse'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_plz']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_plz'"/>
            <xsl:with-param name="widget_label" select="'PLZ'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_ort']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_ort'"/>
            <xsl:with-param name="widget_label" select="'Ort'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_textarea']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_textarea'"/>
            <xsl:with-param name="widget_label" select="'Textfeld'"/>
            <xsl:with-param name="widget_required" select="requiredChoose/value"/>
            <xsl:with-param name="widget_errormsg" select="'Bitte füllen Sie dieses Feld aus'"/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- Ausgelagerte Templates -->
    <xsl:template name="widget_textInput">
        <xsl:param name="widget_name" select="''"/>
        <xsl:param name="widget_label" select="''"/>
        <xsl:param name="widget_required" select="''"/>
        <xsl:param name="widget_errormsg" select="''"/>
        <fd:field required="false">   
            <xsl:attribute name="id">
                <xsl:value-of select="$widget_name"/>
            </xsl:attribute>
            <fd:label>
                <xsl:value-of select="$widget_label"/><xsl:if test="$widget_required = 'false'">*</xsl:if>
            </fd:label>
            <fd:datatype base="string"/>
            <xsl:if test="$widget_required = 'false'">
                <xsl:call-template name="standardValidation">
                    <xsl:with-param name="widgetErrorMsg" select="$widget_errormsg"/>
                </xsl:call-template>
            </xsl:if>
        </fd:field>
    </xsl:template>
    
    <xsl:template name="standardValidation">
        <xsl:param name="widgetErrorMsg" select="''"/>
        <fd:validation>
            <fd:javascript>
                <![CDATA[
                    var success = false;
                    if (widget.value == null || widget.value == undefined) {
                        widget.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError("]]><xsl:value-of select="$widgetErrorMsg"/><![CDATA[", false));
                        widget.getParent().lookupWidget("standard_errorframe").setValue("Bitte füllen Sie alle rot markierten Pflichtfelder aus!");
                        widget.getParent().lookupWidget("standard_errorframe").setState(org.apache.cocoon.forms.formmodel.WidgetState.ACTIVE);
                    } else { success = true; }
                    return success;
                ]]>
            </fd:javascript>
        </fd:validation>
    </xsl:template>
    
</xsl:stylesheet>