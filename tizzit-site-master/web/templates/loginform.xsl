<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="login" select="''"/>
    <xsl:param name="resource"/>
    <xsl:template match="iteration | content | all" mode="include" priority="1">
        <div class="form">
            <!--Error Message ausgeben, falls das Login fehlgeschlagen ist -->
            <xsl:if test="$login='failed'">
                <div class="error">
                    <xsl:value-of select="//errorMsg"/>
                </div>
                <br/>
            </xsl:if>
            <!--Formulaer ausgeben -->
            <form method="post" action="do-login">
                <div> Benutzername:<br/>
                    <input type="text" name="username" value="" title="Benutzername"/>
                </div>
                <div> Passwort:<br/>
                    <input type="password" name="password" value="" title="Passwort"/>
                </div>
                <br/>
                <div>
                    <input type="submit" value="Login"/>
                </div>
                <!--Die Resource, an die nach dem Login weitergeleitet wird, als parameter uebergeben -->
                <input type="hidden" name="resource" value="{$resource}"/>
            </form>
        </div>
    </xsl:template>
</xsl:stylesheet>
