<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
    
    <xsl:param name="hostname" select="'default'"/>
    
    <xsl:include href="../../../../tizzit-site-master/web/templates/loginform.xsl"/>
    <xsl:include href="common.xsl"/>
    
    <xsl:template match="iteration | content | all" mode="include" priority="1.1">
        <!--Error Message ausgeben, falls das Login fehlgeschlagen ist -->
        <xsl:if test="$login='-1'">
            <div class="error">
                <xsl:value-of select="//errorMsg"/>
            </div>
            <br/>
        </xsl:if>
        
        <div class="form">
            <!--Formulaer ausgeben -->
            <form method="post" action="{$resource}page.html">
                <div class="loginform"> Benutzername:<br/>
                    <input tabindex="1" width="100" id="cqusername" type="text" name="cqusername" value="Benutzername" title="Benutzername" onfocus="if(this.value=='Benutzername') this.value=''" onBlur="if(this.value=='') this.value='Benutzername'"/>
                </div>
                <div class="loginform"> Passwort:<br/>
                    <input tabindex="2" width="100" type="password" name="cqpassword" id="cqpassword" value="Passwort" title="Passwort" onfocus="if(this.value=='Passwort') this.value=''" onBlur="if(this.value=='') this.value='Passwort'"/>
                </div>
                <br/>
                <div class="submitbutton">
                    <input type="submit" value="Login"/>
                </div>
                <!--Die Resource, an die nach dem Login weitergeleitet wird, als parameter uebergeben -->
                <input type="hidden" name="resource" value="{$resource}"/>
            </form>
        </div>
        
        
        
    </xsl:template>
    
</xsl:stylesheet>