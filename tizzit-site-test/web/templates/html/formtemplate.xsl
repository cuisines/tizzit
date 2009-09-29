<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ft="http://apache.org/cocoon/forms/1.0#template" xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">
    
    <xsl:import href="common.xsl"/>
    <xsl:include href="../../../../tizzit-site-master/web/templates/formtemplate.xsl"/>
    
    <!-- Neues Formularkonzept von Sven, muss noch getestet werden! -->
    <!-- * Validierungen alle via Javascript -->
    <!-- * Alle Templates unten ausgelagert -->
    <!-- * Funktionierende Error Messages auch wenn man nichts ausfüllt -->
    <!-- * Error Box am Anfang der Seite -->
    <!-- * Einheitliches Formulardesign -->
    <!-- * Einfaches einfügen neuer Widgets -->
    
    <xsl:template match="source" priority="2">
        <ft:form-template action="" method="post" accept-charset="utf-8">
            <xsl:if test="all/content/prevtext != '' or all/content/prevtext/p != ''">
                <div class="form-prevtext">
                    <xsl:apply-templates select="all/content/prevtext" mode="format"/>
                </div>
            </xsl:if>
            <!-- Error Frame kommt an den Anfang des Formulars -->
            <ft:widget id="standard_errorframe">
                <fi:styling class="standard_errorframe"/>
            </ft:widget>
            <div class="clear">&#160;</div>
            <xsl:apply-templates select="all/content/iteration" mode="item"/>
            <ft:continuation-id/>
            <div class="form-submit-button">
                <input type="submit" value="Abschicken"/>
            </div>
            <xsl:if test="all/content/aftertext != '' or all/content/aftertext/p != ''">
                <div class="form-aftertext">
                    <xsl:apply-templates select="all/content/aftertext" mode="format"/>
                </div>
            </xsl:if>
        </ft:form-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_name']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_name'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_vorname']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_vorname'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_email']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_email'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_strasse']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_strasse'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_plz']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_plz'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_ort']" priority="2">
        <xsl:call-template name="widget_textInput">
            <xsl:with-param name="widget_name" select="'standard_ort'"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='standard_textarea']" priority="2">
        <xsl:call-template name="widget_textAreaInput">
            <xsl:with-param name="widget_name" select="'standard_textarea'"/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- Ausgelagerte Templates -->
    <xsl:template name="widget_textInput">
        <xsl:param name="widget_name" select="''"/>
        <div class="widget">
            <div class="widget-label">
                <ft:widget-label id="{$widget_name}"/>
            </div>
            <div class="widget-input">
                <ft:widget id="{$widget_name}"/>
            </div>
            <div class="clear">&#160;</div>
        </div>
    </xsl:template>
    
    <xsl:template name="widget_textAreaInput">
        <xsl:param name="widget_name" select="''"/>
        <div class="widget">
            <div class="widget-label">
                <ft:widget-label id="{$widget_name}"/>
            </div>
            <div class="widget-input">
                <ft:widget id="{$widget_name}">
                    <fi:styling type="textarea"/>
                </ft:widget>
            </div>
            <div class="clear">&#160;</div>
        </div>
    </xsl:template>

</xsl:stylesheet>