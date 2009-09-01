<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
    xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

    <!-- ACHTUNG: XSLT 2 Template!!!

Sollte die identischen Felder wie formtemplate.xsl unterstützen. Nur mit anderer content und css struktur.
 -->

    <xsl:template match="source" priority="1.1">
        <!--<ft:form-template action="" method="post" accept-charset="utf-8" location="getAttribute($cocoon/session, 'form')">-->
        <xsl:variable name="enctype">
            <xsl:choose>
                <xsl:when
                    test="count(/source/all/content/iteration/item[@description='attachment']) &gt; 0">
                    <xsl:text>multipart/form-data</xsl:text>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:variable>

        <ft:form-template action="" method="post" accept-charset="utf-8">
            <xsl:if test="$enctype != ''">
                <xsl:attribute name="enctype">
                    <xsl:value-of select="$enctype"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="all/content/prevtext != '' or all/content/prevtext/p != ''">
                <div>
                    <xsl:apply-templates select="all/content/prevtext" mode="format"/>
                </div>
            </xsl:if>
            <xsl:apply-templates select="all/content/iteration" mode="item"/>
            <ft:continuation-id/>
            <div class="widget" id="widget-submit">
                <input type="submit" class="widget-submit" value="Abschicken"/>
            </div>
            <div>
                <xsl:apply-templates select="all/content/aftertext" mode="format"/>
            </div>
        </ft:form-template>
    </xsl:template>
    
    <!-- Captcha - RobotProtection -->
	<xsl:template match="captcha/protect[hide='true']" priority="2">
		<div class="widget" id="widget-captcha">
			<img class="capt_img" src="simple.jpeg" alt="captcha"/>
			<div class="widget-label">
				<ft:widget-label id="captcha"/>
			</div>
			<div class="widget-item">
				<ft:widget id="captcha">
					<fi:styling class="widget-captcha" />
				</ft:widget> 
			</div>
		</div>
	</xsl:template>

    <!-- prev und post text matchen -->
    <xsl:template match="prevtext | aftertext" mode="format">
        <xsl:apply-templates mode="format"/>
    </xsl:template>

    <!--Textfeld in items matchen -->
    <xsl:template match="item" mode="item">
        <xsl:if test="text!='' and text/p!=''">
            <div class="text">
                <xsl:apply-templates select="text" mode="format"/>
            </div>
        </xsl:if>
        <xsl:apply-templates select="."/>
    </xsl:template>


    <xsl:template match="item[form-element-list/value='kassel-helgoland']">
        <div class="widget" id="widget-elena">
            <div class="widget-label">
                <ft:widget-label id="elena"/>
            </div>
            <div class="widget-item">
                <ft:widget id="elena">
                    <fi:styling list-type="radio" class="widget-elena"/>
                </ft:widget>
            </div>
        </div>
        <div class="widget" id="widget-nordsee">
            <div class="widget-label">
                <ft:widget-label id="nordsee"/>
            </div>
            <div class="widget-item">
                <ft:widget id="nordsee">
                    <fi:styling list-type="radio" class="widget-nordsee"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template
        match="item[form-element-list/value='input1'] | item[form-element-list/value='input2'] | item[form-element-list/value='input3'] | item[form-element-list/value='input4'] | item[form-element-list/value='input5']">
        <xsl:variable name="id-name" select="form-element-list/value"/>
        <div class="widget" id="widget-{$id-name}">
            <xsl:if test="label!=''">
                <div class="widget-label">
                    <ft:widget-label id="{$id-name}"/>
                </div>
            </xsl:if>
            <div class="widget-item">
                <ft:widget id="{$id-name}">
                    <fi:styling class="widget-input"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='name']">
        <div class="widget" id="widget-name">
            <div class="widget-label">
                <ft:widget-label id="name"/>
            </div>
            <div class="widget-item">
                <ft:widget id="name">
                    <fi:styling class="widget-name"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='krankenhausname']">
        <div class="widget" id="widget-krankenhausname">
            <div class="widget-label">
                <ft:widget-label id="krankenhausname"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="krankenhausname">
                    <fi:styling class="widget-krankenhausname"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='fachgebiet']">
        <div class="widget" id="widget-fachgebiet">
            <div class="widget-label">
                <ft:widget-label id="fachgebiet"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="fachgebiet">
                    <fi:styling class="widget-fachgebiet"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='land']">
        <div class="widget" id="widget-land">
            <div class="widget-label">
                <ft:widget-label id="land"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="land">
                    <fi:styling class="widget-land"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='funktion']">
        <div class="widget" id="widget-funktion">
            <div class="widget-label">
                <ft:widget-label id="funktion"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="funktion">
                    <fi:styling class="widget-funktion"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='firma']">
        <div class="widget" id="widget-firma">
            <div class="widget-label">
                <ft:widget-label id="firma"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="firma">
                    <fi:styling class="widget-firma"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='vorname']">
        <div class="widget" id="widget-vorname">
            <div class="widget-label">
                <ft:widget-label id="vorname"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="vorname">
                    <fi:styling class="widget-vorname"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anrede']">
        <div class="widget" id="widget-anrede">
            <div class="widget-label">
                <ft:widget-label id="anrede"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="anrede">
                    <fi:styling class="widget-anrede"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='tel']">
        <div class="widget" id="widget-tel">
            <div class="widget-label">
                <ft:widget-label id="tel"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="tel">
                    <fi:styling class="widget-tel"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='titel']">
        <div class="widget" id="widget-titel">
            <div class="widget-label">
                <ft:widget-label id="titel"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="titel">
                    <fi:styling class="widget-titel"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='fax']">
        <div class="widget" id="widget-fax">
            <div class="widget-label">
                <ft:widget-label id="fax"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="fax">
                    <fi:styling class="widget-fax"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='email']">
        <div class="widget" id="widget-email">
            <div class="widget-label">
                <ft:widget-label id="email"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="email">
                    <fi:styling class="widget-email"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='email2']">
        <div class="widget" id="widget-email2">
            <div class="widget-label">
                <ft:widget-label id="email2"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="email2">
                    <fi:styling class="widget-email2"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='infotext']">
        <div class="widget" id="widget-infotext">
            <div class="widget-label">
                <ft:widget-label id="infotext"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="infotext">
                    <fi:styling class="widget-infotext"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='strasse']">
        <div class="widget" id="widget-strasse">
            <div class="widget-label">
                <ft:widget-label id="strasse"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="strasse">
                    <fi:styling class="widget-strasse"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='plz']">
        <div class="widget" id="widget-plz">
            <div class="widget-label">
                <ft:widget-label id="plz"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="plz">
                    <fi:styling class="widget-plz"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='ort']">
        <div class="widget" id="widget-ort">
            <div class="widget-label">
                <ft:widget-label id="ort"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="ort">
                    <fi:styling class="widget-ort"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='attachment']">
        <div class="widget" id="widget-attachment_1">
            <div class="widget-label_1">
                <ft:widget-label id="attachment_1"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="attachment_1">
                    <fi:styling class="widget-attachment_1"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='bday']">
        <div class="widget" id="widget-bday">
            <div class="widget-label">
                <ft:widget-label id="bday"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="bday">
                    <fi:styling class="widget-bday"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='blutgruppe']">
        <div class="widget" id="widget-blutgruppe">
            <div class="widget-label">
                <ft:widget-label id="blutgruppe"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="blutgruppe">
                    <fi:styling class="widget-blutgruppe"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='gtel']">
        <div class="widget" id="widget-gtel">
            <div class="widget-label">
                <ft:widget-label id="gtel"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="gtel">
                    <fi:styling class="widget-gtel"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='handy']">
        <div class="widget" id="widget-handy">
            <div class="widget-label">
                <ft:widget-label id="handy"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="handy">
                    <fi:styling class="widget-handy"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='geschlecht']">
        <div class="widget" id="widget-geschlecht">
            <div class="widget-label">
                <ft:widget-label id="geschlecht"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="geschlecht">
                    <fi:styling list-type="radio" list-orientation="horizontal" size="20" class="widget-geschlecht"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='newsletter']">
        <div class="widget" id="widget-newsletter">
            <div class="widget-label">
                <ft:widget-label id="newsletter"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="newsletter">
                    <fi:styling list-type="checkbox" class="widget-newsletter"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='adressGroup']">       
        <ft:widget id="adressGroup">
            <fi:styling list-type="radio" list-orientation="advanced_1" size="20" class="widget-adressGroup"/>
        </ft:widget>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anfragetext']">
        <div class="widget" id="widget-anfragetext">
            <div class="widget-label">
                <ft:widget-label id="anfragetext"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="anfragetext">
                    <fi:styling type="textarea" class="widget-anfragetext"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='url']">
        <div class="widget" id="widget-url">
            <div class="widget-label">
                <ft:widget-label id="url"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="url">
                    <fi:styling type="textarea" class="widget-url"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='unit']">
        <div class="widget" id="widget-unit">
            <div class="widget-label">
                <ft:widget-label id="unit"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="unit">
                    <fi:styling type="textarea" class="widget-unit"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anzahl']">
        <div class="widget" id="widget-anzahl">
            <div class="widget-label">
                <ft:widget-label id="anzahl"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="anzahl">
                    <fi:styling type="textarea" class="widget-anzahl"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <!-- feedback-blocks -->
    <xsl:template match="item[form-element-list/value='feedback-user']">
        <div class="widget" id="widget-feedback">
            <fieldset>
                <legend title="">Ihre Daten</legend>
                <div style="float:left;width:75px;padding-top:10px;">
                    <ft:widget-label id="feedback_name"/>
                </div>
                <div style="float:left;padding-top:10px;">
                    <ft:widget id="feedback_name">
                        <fi:styling class="widget-feedback_name"/>
                    </ft:widget>
                </div>

                <div style="clear:both;float:left;width:75px;padding-top:10px;">
                    <ft:widget-label id="feedback_email"/>
                </div>
                <div style="float:left;padding-top:10px;">
                    <ft:widget id="feedback_email">
                        <fi:styling class="widget-feedback_email"/>
                    </ft:widget>
                </div>
            </fieldset>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='feedback-block']">
        <div class="widgetbox">
            <fieldset>
                <legend title="">Ihre Bewertung</legend>
                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_firstimpression"/>
                    </span>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_design"/>
                    </span>
                </div>

                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_firstimpression">
                        <fi:styling list-type="radio" class="widget-feedback_firstimpression"/>
                    </ft:widget>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_design">
                        <fi:styling list-type="radio" class="widget-feedback_design"/>
                    </ft:widget>
                </div>


                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_usability"/>
                    </span>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_information"/>
                    </span>
                </div>

                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_usability">
                        <fi:styling list-type="radio" class="widget-feedback_usibility"/>
                    </ft:widget>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_information">
                        <fi:styling list-type="radio" class="widget-feedback_information"/>
                    </ft:widget>
                </div>


                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_comment"/>
                    </span>
                </div>

                <div style="float:none;clear:both;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_comment">
                        <fi:styling type="textarea" class="widget-feedback_comment"/>
                    </ft:widget>
                </div>

            </fieldset>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ffwoher']">
        <div class="widget" style="padding-top:10px; padding-bottom:10px;">
            <div class="widget-label">
                <ft:widget-label id="ffwoher"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="ffwoher">
                    <fi:styling class="widget-ffwoher"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ginsenginfo']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="ginsenginfo">
                                <fi:styling list-type="checkbox" class="widget-ginsenginfo"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="ginsenginfo"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ginsenginfo2']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="ginsenginfo2">
                                <fi:styling list-type="checkbox" class="widget-ginsenginfo2"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="ginsenginfo2"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <!-- Raptiva -->

    <xsl:template match="item[form-element-list/value='rpbestell']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="rpbestell">
                                <fi:styling list-type="checkbox" class="widget-rpbestell"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="rpbestell"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpbestell2']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="rpbestell2">
                                <fi:styling list-type="checkbox" class="widget-rpbestell2"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="rpbestell2"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpbestell3']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="rpbestell3">
                                <fi:styling list-type="checkbox" class="widget-rpbestell3"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="rpbestell3"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpbestell4']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="rpbestell4">
                                <fi:styling list-type="checkbox" class="widget-rpbestell4"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="rpbestell4"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpbestellall']">
        <div>
            <table>
                <tr>
                    <td>
                        <div class="widget-item">
                            <ft:widget id="rpbestellall">
                                <fi:styling list-type="checkbox" class="widget-rpbestellall"/>
                            </ft:widget>
                        </div>
                    </td>
                    <td>
                        <div class="widget-label">
                            <ft:widget-label id="rpbestellall"/>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpquest1']">
        <div class="widget" id="widget-rpquest1">
            <div class="widget-label">
                <ft:widget-label id="rpquest1"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="rpquest1">
                    <fi:styling type="textarea" class="widget-rpquest1"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpquest2']">
        <div class="widget" id="widget-rpquest2">
            <div class="widget-label">
                <ft:widget-label id="rpquest2"/>
            </div>
            
            <div class="widget-item">
                <ft:widget id="rpquest2">
                    <fi:styling type="textarea" class="widget-rpquest2"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='rpquest3']">
        <div class="widget" id="widget-rpquest3">
            <div class="widget-label">
                <ft:widget-label id="rpquest3"/>
            </div>
            <div class="widget-item">
                <ft:widget id="rpquest3">
                    <fi:styling type="textarea" class="widget-rpquest3"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
