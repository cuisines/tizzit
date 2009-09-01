<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ft="http://apache.org/cocoon/forms/1.0#template"
    xmlns:fi="http://apache.org/cocoon/forms/1.0#instance">

    <xsl:template match="source">
        <!--<ft:form-template action="" method="post" accept-charset="utf-8" location="getAttribute($cocoon/session, 'form')">-->
        <xsl:variable name="enctype">
            <xsl:choose>
                <xsl:when test="count(/source/all/content/iteration/item[@description='attachment']) &gt; 0">
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
            <div style="position:relative; padding-top:20px; padding-bottom:20px">
                <div style="display:inline;">
                    <input type="submit" value="Abschicken"/>
                </div>
            </div>
            <div>
                <xsl:apply-templates select="all/content/aftertext" mode="format"/>
            </div>
        </ft:form-template>
    </xsl:template>

    <!-- prev und post text matchen -->
    <xsl:template match="prevtext | aftertext" mode="format">
        <xsl:apply-templates mode="format"/>
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
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="elena"/>
            <br/>
            <ft:widget id="elena">
                <fi:styling list-type="radio"/>
            </ft:widget>
        </div>
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="nordsee"/>
            <br/>
            <ft:widget id="nordsee">
                <fi:styling list-type="radio"/>
            </ft:widget>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='input1'] | item[form-element-list/value='input2'] | item[form-element-list/value='input3'] | item[form-element-list/value='input4'] | item[form-element-list/value='input5']">     
        <xsl:variable name="id-name" select="form-element-list/value"/>
        <div class="widget" style="padding-top:10px">
            <xsl:if test="label!=''">
                <ft:widget-label id="{$id-name}"/>
                <br/>
            </xsl:if>
            <ft:widget id="{$id-name}">
                <fi:styling class="widget-input"/>
            </ft:widget>
        </div>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='name']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="name"/>
            <br/>
            <ft:widget id="name">
                <fi:styling class="widget-name"/>
            </ft:widget>
        </div>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='krankenhausname']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="krankenhausname"/>
            <br/>
            <ft:widget id="krankenhausname">
                <fi:styling class="widget-name"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='fachgebiet']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="fachgebiet"/>
            <br/>
            <ft:widget id="fachgebiet">
                <fi:styling class="widget-name"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='land']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="land"/>
            <br/>
            <ft:widget id="land">
                <fi:styling class="widget-name"/>
            </ft:widget>
        </div>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='funktion']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="funktion"/>
            <br/>
            <ft:widget id="funktion">
                <fi:styling class="widget-funktion"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='firma']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="firma"/>
            <br/>
            <ft:widget id="firma">
                <fi:styling class="widget-firma"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='vorname']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="vorname"/>
            <br/>
            <ft:widget id="vorname">
                <fi:styling class="widget-vorname"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anrede']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="anrede"/>
            <br/>
            <ft:widget id="anrede"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='tel']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="tel"/>
            <br/>
            <ft:widget id="tel"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='titel']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="titel"/>
            <br/>
            <ft:widget id="titel"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='fax']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="fax"/>
            <br/>
            <ft:widget id="fax"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='email']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="email"/>
            <br/>
            <ft:widget id="email">
                <fi:styling class="widget-email"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='email2']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="email2"/>
            <br/>
            <ft:widget id="email2"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='infotext']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="infotext"/>
            <br/>
            <ft:widget id="infotext"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='strasse']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="strasse"/>
            <br/>
            <ft:widget id="strasse"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='plz']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="plz"/>
            <br/>
            <ft:widget id="plz"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='ort']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="ort"/>
            <br/>
            <ft:widget id="ort"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='attachment']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="attachment_1"/>
            <br/>
            <ft:widget id="attachment_1"> </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='bday']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="bday"/>
            <br/>
            <ft:widget id="bday"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='blutgruppe']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="blutgruppe"/>
            <br/>
            <ft:widget id="blutgruppe"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='gtel']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="gtel"/>
            <br/>
            <ft:widget id="gtel"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='tel2']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="tel2"/>
            <br/>
            <ft:widget id="tel2"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='handy']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="handy"/>
            <br/>
            <ft:widget id="handy"/>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='geschlecht']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="geschlecht"/>
            <br/>
            <div class="radio">
                <ft:widget id="geschlecht">
                    <fi:styling list-type="radio" list-orientation="horizontal" size="20"/>
                </ft:widget>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anfragetext']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="anfragetext"/>
            <br/>
            <ft:widget id="anfragetext">
                <fi:styling type="textarea" class="widget-anfragetext"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='url']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="url"/>
            <br/>
            <ft:widget id="url">
                <fi:styling type="textarea" class="widget-url"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='unit']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="unit"/>
            <br/>
            <ft:widget id="unit">
                <fi:styling type="textarea" class="widget-unit"/>
            </ft:widget>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='anzahl']">
        <div class="widget" style="padding-top:10px">
            <ft:widget-label id="anzahl"/>
            <br/>
            <ft:widget id="anzahl">
                <fi:styling type="textarea" class="widget-anzahl"/>
            </ft:widget>
        </div>
    </xsl:template>

    <!-- feedback-blocks -->
    <xsl:template match="item[form-element-list/value='feedback-user']">
        <div class="widget" style="padding-top:10px">
            <fieldset>
                <legend title="">Ihre Daten</legend>
                <div style="float:left;width:75px;padding-top:10px;">
                    <ft:widget-label id="feedback_name"/>
                </div>
                <div style="float:left;padding-top:10px;">
                    <ft:widget id="feedback_name">
                        <fi:styling/>
                    </ft:widget>
                </div>
                <br/>
                <div style="clear:both;float:left;width:75px;padding-top:10px;">
                    <ft:widget-label id="feedback_email"/>
                </div>
                <div style="float:left;padding-top:10px;">
                    <ft:widget id="feedback_email">
                        <fi:styling/>
                    </ft:widget>
                </div>
            </fieldset>
        </div>
    </xsl:template>
    <xsl:template match="item[form-element-list/value='feedback-block']">
        <div class="widgetbox" style="padding-top:10px">
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
                <br/>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_firstimpression">
                        <fi:styling list-type="radio"/>
                    </ft:widget>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_design">
                        <fi:styling list-type="radio"/>
                    </ft:widget>
                </div>
                <br/>

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
                <br/>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_usability">
                        <fi:styling list-type="radio"/>
                    </ft:widget>
                </div>
                <div style="float:left;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_information">
                        <fi:styling list-type="radio"/>
                    </ft:widget>
                </div>
                <br/>

                <div style="float:left;width:225px;padding-top:10px;">
                    <span class="feedback-label">
                        <ft:widget-label id="feedback_comment"/>
                    </span>
                </div>
                <br/>
                <div style="float:none;clear:both;width:225px;padding-top:10px;">
                    <ft:widget id="feedback_comment">
                        <fi:styling type="textarea"/>
                    </ft:widget>
                </div>

            </fieldset>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ffwoher']">
        <div class="widget" style="padding-top:10px; padding-bottom:10px;">
            <ft:widget-label id="ffwoher"/>
            <br/>
            <ft:widget id="ffwoher"/>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ginsenginfo']">
        <div style="padding-top:10px">
            <table>
                <tr>
                    <td>
                        <ft:widget id="ginsenginfo">
                            <fi:styling list-type="checkbox"/>
                        </ft:widget>
                    </td>
                    <td>
                        <ft:widget-label id="ginsenginfo"/>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='ginsenginfo2']">
        <div style="padding-top:10px">
            <table>
                <tr>
                    <td>
                        <ft:widget id="ginsenginfo2">
                            <fi:styling list-type="checkbox"/>
                        </ft:widget>
                    </td>
                    <td>
                        <ft:widget-label id="ginsenginfo2"/>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

</xsl:stylesheet>