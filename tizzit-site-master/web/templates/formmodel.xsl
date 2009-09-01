<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
  
<xsl:template match="source">
    <fd:form xmlns:fd="http://apache.org/cocoon/forms/1.0#definition" xmlns:i18n="http://apache.org/cocoon/i18n/2.1">
        <fd:widgets>
          <xsl:apply-templates select="all/content/iteration"/>        
        </fd:widgets>
    </fd:form>
</xsl:template>

	<!-- Captcha - RobotProtection -->
	<xsl:template match="captcha/protect[hide='true']" >
		<fd:captcha id="captcha" required="true">
			<fd:label>
				<xsl:choose>
					<xsl:when test="labelCaptcha=''">Schreiben Sie bitte die Ziffern von dem Bild ab.</xsl:when>
					<xsl:otherwise><xsl:value-of select="../labelCaptcha"/></xsl:otherwise>
				</xsl:choose>  
			</fd:label>
			<fd:datatype base="string"/>
			<fd:validation>
				<fd:javascript><![CDATA[
                    var logname = "mail";
	                var log = Packages.org.apache.log4j.Logger.getLogger(logname);
	                var success = true;]]>
	                <xsl:variable name="validateCaptcha">
                        <xsl:choose>
                        	<xsl:when test="validateCaptcha=''"><xsl:text>Der eingegebene Wert war nicht korrekt.</xsl:text></xsl:when>
                        	<xsl:otherwise><xsl:value-of select="../validateCaptcha"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
				<![CDATA[
                try {
                    var captcha_code = widget.getParent().lookupWidget("captcha").getValue();
                    var captcha_generated = cocoon.session.getAttribute("captcha");
                } catch(e) {
                   log.error(e);
                   success = false;
                   widget.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError("]]><xsl:value-of select="$validateCaptcha"/><![CDATA[", false));
                }
                //var parameters = {"supplied": captcha_code.value, "expected":captcha_generated};
                
                if(captcha_code != null && captcha_generated!= null) {
                   if(!captcha_code.equalsIgnoreCase(captcha_generated)) {
                       success = false;
                        var session = cocoon.session;
                        var count = cocoon.session.getAttribute("count");

  						session.setAttribute("captcha", passcode(count));
                       widget.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError("]]><xsl:value-of select="$validateCaptcha"/><![CDATA[", false));
                   } 
                }
                if(log.isDebugEnabled() && success) {
                    log.debug("The validation was successfully!");   
                }
                return success;
       	 ]]></fd:javascript>
			</fd:validation>
		</fd:captcha>
	</xsl:template>
    
    <xsl:template match="item[form-element-list/value='newsletter']">
        <fd:booleanfield id="newsletter" required="false">
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Ich möchte den Newsletter bestellen.</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>
            </fd:label>
         </fd:booleanfield>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='kassel-helgoland']">
        <fd:field id="elena">
            <fd:label>Paracelsus-Elena-Klinik</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list>
                <fd:item value="ambulant">
                    <fd:label>ambulant</fd:label>
                </fd:item>
                <fd:item value="stationaer">
                    <fd:label>stationär</fd:label>
                </fd:item> 
            </fd:selection-list>   
        </fd:field>        
        <fd:field id="nordsee">
            <fd:label>Paracelsus-Nordseeklinik Helgoland</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list>
                <fd:item value="ambulant">
                    <fd:label>ambulant</fd:label>
                </fd:item>
                <fd:item value="stationaer">
                    <fd:label>stationär</fd:label>
                </fd:item> 
            </fd:selection-list>   
        </fd:field>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='input1'] | item[form-element-list/value='input2'] | item[form-element-list/value='input3'] | item[form-element-list/value='input4'] | item[form-element-list/value='input5']">
        <xsl:variable name="id-name" select="form-element-list/value"/>
        <fd:field id="{$id-name}" required="{requiredChoose/value}">                          
            <fd:label><xsl:value-of select="label"/></fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte füllen Sie dieses Feld aus.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
    
   <xsl:template match="item[form-element-list/value='name']">
         <fd:field id="name" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Nachname</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie einen Namen ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>
    
    <xsl:template match="item[form-element-list/value='fachgebiet']">
        <fd:field id="fachgebiet" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Fachgebiet</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihr Fachgebiet ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='land']">
        <fd:field id="land" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Land</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie das Land ein, in dem Sie wohnen.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='krankenhausname']">
        <fd:field id="krankenhausname" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Krankenhausname</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie den Namen des Krankenhauses ein, falls Sie in einem Krankenhaus arbeiten.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
<xsl:template match="item[form-element-list/value='vorname']">
         <fd:field id="vorname" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Vorname</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie einen Vornamen ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='anrede']">
         <fd:field id="anrede" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Anrede</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>            
            <fd:datatype base="string"/>            
           <fd:selection-list>
              <fd:item value="Herr">
                  <fd:label>Herr</fd:label>
              </fd:item>
              <fd:item value="Frau">
                  <fd:label>Frau</fd:label>
              </fd:item> 
            </fd:selection-list>          
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='tel']">
         <fd:field id="tel" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Tel.</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Telefonnummer ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='titel']">
         <fd:field id="titel" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Titel</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='fax']">
         <fd:field id="fax" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Fax.</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Faxnummer ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='email']">
         <fd:field id="email" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">E-Mail</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre E-Mail Adresse ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='email2']">
         <fd:field id="email2" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">E-Mail Kontrolleingabe</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre E-Mail Adresse ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='infotext']">
         <fd:field id="infotext" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Infotext</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Infotext ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='strasse']">
         <fd:field id="strasse" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Strasse, Nr.</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Strasse ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='plz']">
         <fd:field id="plz" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Postleitzahl</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Postleitzahl ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='ort']">
         <fd:field id="ort" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Ort</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre E-Mail Adresse ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
         </fd:field>
</xsl:template>
    
<xsl:template match="item[form-element-list/value='funktion']">
    <fd:field id="funktion" required="{requiredChoose/value}">                          
        <fd:label>
            <xsl:choose>
                <xsl:when test="label=''">Funktion</xsl:when>
                <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
            </xsl:choose>            
        </fd:label>
        <fd:datatype base="string"/>
        <fd:validation>
            <fd:length min="2">
                <fd:failmessage>                    
                    <xsl:choose>
                        <xsl:when test="validate=''">Bitte tragen Sie Ihre Funktion ein.</xsl:when>
                        <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                    </xsl:choose>       
                </fd:failmessage>
            </fd:length>
        </fd:validation>
    </fd:field>
</xsl:template>
    
<xsl:template match="item[form-element-list/value='firma']">
    <fd:field id="firma" required="{requiredChoose/value}">                          
        <fd:label>
            <xsl:choose>
                <xsl:when test="label=''">Firma</xsl:when>
                <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
            </xsl:choose>            
        </fd:label>
        <fd:datatype base="string"/>
        <fd:validation>
            <fd:length min="2">
                <fd:failmessage>                    
                    <xsl:choose>
                        <xsl:when test="validate=''">Bitte tragen Sie Ihre Firma ein.</xsl:when>
                        <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                    </xsl:choose>       
                </fd:failmessage>
            </fd:length>
        </fd:validation>
    </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='attachment']">
         <fd:upload id="attachment_1" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Anhang</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>
            </fd:label>
            <!--
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte geben Sie einen Anhang an.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
            -->
        </fd:upload>
</xsl:template>

<xsl:template match="item[form-element-list/value='bday']">
         <fd:field id="bday" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Geburtsdatum</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihr Geburtsdatum ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='blutgruppe']">
         <fd:field id="blutgruppe" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Blutgruppe</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="1">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Blutgruppe ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='gtel']">
         <fd:field id="gtel" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Telefon (geschäflich)</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Telefonnr. ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

    <xsl:template match="item[form-element-list/value='tel2']">
        <fd:field id="tel2" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Telefon (geschäflich)</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Telefonnr. ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>

<xsl:template match="item[form-element-list/value='handy']">
         <fd:field id="handy" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Handy</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte tragen Sie Ihre Handynr ein.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='geschlecht']">
         <fd:field id="geschlecht" required="{requiredChoose/value}">                           
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Geschlecht</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>            
            <fd:datatype base="string"/>                      
           <fd:selection-list>    
              <fd:item value="maennlich">
                  <fd:label>männlich</fd:label>
              </fd:item>
              <fd:item value="weiblich">
                  <fd:label>weiblich</fd:label>
              </fd:item>                
            </fd:selection-list>                   
        </fd:field>
</xsl:template>

<xsl:template match="item[form-element-list/value='anfragetext']">
         <fd:field id="anfragetext" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Anfrage</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="3">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte stellen Sie hier Ihre Frage.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
</xsl:template>

    <xsl:template match="item[form-element-list/value='url']">
        <fd:field id="url" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">URL</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="3">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte geben Sie Ihre URL an.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='unit']">
        <fd:field id="unit" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Einrichtung</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="3">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte geben Sie Ihre Einrichtung an.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='anzahl']">
        <fd:field id="anzahl" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Anzahl</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="1">
                    <fd:failmessage>                    
                        <xsl:choose>
                            <xsl:when test="validate=''">Bitte geben Sie eine Anzahl an.</xsl:when>
                            <xsl:otherwise><xsl:value-of select="validate"/></xsl:otherwise>
                        </xsl:choose>       
                    </fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='feedback-user']">
        <fd:field id="feedback_name" required="true">                          
            <fd:label>Name</fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>Bitte tragen Sie Ihren Namen ein.</fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>        
        <fd:field id="feedback_email" required="true">                          
            <fd:label>E-Mail</fd:label>
            <fd:datatype base="string"/>
            <fd:validation>
                <fd:length min="2">
                    <fd:failmessage>Bitte tragen Sie Ihre E-Mail Adresse ein.</fd:failmessage>
                </fd:length>
            </fd:validation>
        </fd:field>
    </xsl:template>

    <xsl:template match="item[form-element-list/value='feedback-block']">
        <fd:field id="feedback_firstimpression" required="true">
            <fd:label>Erster Eindruck</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list src="cocoon:/forms/feedback/feedback-selection-items.xml"/> 
        </fd:field>
        <fd:field id="feedback_design" required="true">
            <fd:label>Design</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list src="cocoon:/forms/feedback/feedback-selection-items.xml"/> 
        </fd:field>
        <fd:field id="feedback_usability" required="true">
            <fd:label>Benutzerführung</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list src="cocoon:/forms/feedback/feedback-selection-items.xml"/> 
        </fd:field>
        <fd:field id="feedback_information" required="true">
            <fd:label>Informationsgehalt</fd:label>    
            <fd:datatype base="string"/>
            <fd:selection-list src="cocoon:/forms/feedback/feedback-selection-items.xml"/> 
        </fd:field>
        <fd:field id="feedback_comment" required="false">
            <fd:label>Ihr Kommentar</fd:label>    
            <fd:datatype base="string"/>
        </fd:field>                                 
    </xsl:template>
     
    
    <xsl:template match="item[form-element-list/value='ginsenginfo']">
        <fd:booleanfield id="ginsenginfo">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">ginsenginfo</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
        </fd:booleanfield>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='ginsenginfo2']">
        <fd:booleanfield id="ginsenginfo2">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">ginsenginfo2</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>
        </fd:booleanfield>
    </xsl:template>
    
    <xsl:template match="item[form-element-list/value='ffwoher']">
        <fd:field id="ffwoher" required="{requiredChoose/value}">                          
            <fd:label>
                <xsl:choose>
                    <xsl:when test="label=''">Woher kennen Sie Uns</xsl:when>
                    <xsl:otherwise><xsl:value-of select="label"/></xsl:otherwise>
                </xsl:choose>            
            </fd:label>            
            <fd:datatype base="string"/>            
            <fd:selection-list>
                <fd:item value="">
                    <fd:label>Bitte auswählen</fd:label>
                </fd:item>
                <fd:item value="Mein Arzt/Aerztin">
                    <fd:label>Mein/e Arzt/Ärztin</fd:label>
                </fd:item>
                <fd:item value="Mein Tierarzt/Tieraerztin">
                    <fd:label>Mein/e Tierarzt/Tierärztin</fd:label>
                </fd:item>
                <fd:item value="Nebenstelle Gran Canaria">
                    <fd:label>Nebenstelle Gran Canaria</fd:label>
                </fd:item>
                <fd:item value="Nebenstelle Mallorca">
                    <fd:label>Nebenstelle Mallorca</fd:label>
                </fd:item>
                <fd:item value="Aussendienst Herr Harbarth">
                    <fd:label>Außendienst Herr Harbarth</fd:label>
                </fd:item>
                <fd:item value="Provista">
                    <fd:label>Provista</fd:label>
                </fd:item>
                <fd:item value="Besuch auf der FloraFarm">
                    <fd:label>Besuch auf der FloraFarm</fd:label>
                </fd:item>
                <fd:item value="Verwandte/Bekannte">
                    <fd:label>Verwandte/Bekannte</fd:label>
                </fd:item>
                <fd:item value="Messebesuch">
                    <fd:label>Messebesuch</fd:label>
                </fd:item>
                <fd:item value="Zeitungsartikel">
                    <fd:label>Zeitungsartikel</fd:label>
                </fd:item>
                <fd:item value="Fernsehbericht">
                    <fd:label>Fernsehbericht</fd:label>
                </fd:item>
                <fd:item value="Anzeige">
                    <fd:label>Anzeige</fd:label>
                </fd:item>
                <fd:item value="Suchmaschine">
                    <fd:label>Suchmaschine</fd:label>
                </fd:item>
                <fd:item value="Sonstiges">
                    <fd:label>Sonstiges</fd:label>
                </fd:item>
            </fd:selection-list>      
        </fd:field>
    </xsl:template>
        
</xsl:stylesheet>