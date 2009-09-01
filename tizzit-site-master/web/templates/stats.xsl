<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:session="http://apache.org/cocoon/session/1.0"
    xmlns:ctmpl="http://www.conquest-cms.net/template">
    
	<xsl:param name="login" select="''"/>

    <xsl:template match="all" priority="2">
		<ctmpl:module name="content">
			<div class="content">
	            <xsl:apply-templates select="." mode="content"/>	        
            </div>
	    </ctmpl:module>
	</xsl:template>

    <xsl:template match="all" mode="content">
            	<xsl:apply-templates select="../head/authentication" mode="stats"/>
        		<xsl:if test="not(//head/authentication)">
        			<form action="do-login" method="POST">
        				<table cellspacing="2" cellpadding="2" border="0" align="left">
        				    <xsl:apply-templates select="." mode="resource"/>
        					<xsl:call-template name="loginfail"/>
        					<tr>
        						<td>Benutzername</td>
        						<td>
        							<input value="" name="username" type="text"/>
        						</td>
        					</tr>
        					<tr>
        						<td>Passwort</td>
        						<td>
        							<input value="" name="password" type="password"/>
        						</td>
        					</tr>
        					<tr>
        						<td/>
        						<td>
        							<input value="Login" name="" type="submit"/>
        						</td>
        					</tr>
        				</table>
        			</form>
        		</xsl:if>
   	</xsl:template>
	
	<xsl:template match="all" mode="resource">
   		<input value="/deutsch/{$url}/page.html" name="resource" type="hidden"/>
    </xsl:template>
	
	<xsl:template match="authentication" mode="stats">
    	<div class="logout">
    	    Hier können Sie den geschützten Bereich verlassen: <a href="do-logout">Logout</a>
    	</div>
	    <div class="leftContainer">
    		<xsl:choose>
    			<xsl:when test="role='viewWebstats'">
    			    <a name="top"/>
    			    <div class="intro">Sie haben auf die Statistiken der folgenden Seiten und Units Zugriff:</div>
    				<xsl:apply-templates select="siteInformation" mode="stats">
    				    <xsl:sort select="sitename" order="ascending"/>
    				</xsl:apply-templates>
    			</xsl:when>		
    			<xsl:otherwise>
        			 <div class="intro"><b>Sie haben leider keine ausreichenden Berechtigungen mit dem User
    			 <xsl:value-of select="//authentication/id"/> auf diese Statistiken zuzugreifen!</b></div>
    			</xsl:otherwise>
    		</xsl:choose>
		</div>
	</xsl:template>

    <xsl:template match="siteInformation" mode="stats">
        <xsl:variable name="actId" select="@id"/>
        <div class="siteContainer">
            <a>
                <xsl:attribute name="name">
                    <xsl:value-of select="generate-id(.)"/>
                </xsl:attribute>
            </a>
            <div class="siteLinkContainer">
                <div class="siteLink">
                    <a>
                        <xsl:attribute name="href">
                            <xsl:text>/stats/</xsl:text><xsl:value-of select="@id"/><xsl:text>/index.html</xsl:text>
                        </xsl:attribute>
                        <xsl:value-of select="sitename"/> (gesamt)
                    </a>
                </div>
                <div class="topLink">
                    <a href="#top">Top</a>
                </div>
            </div>
            <div class="unitContainer">
                <xsl:apply-templates select="../unitInformation[@siteId=$actId]" mode="stats">            
                    <xsl:sort select="unitname" order="ascending"/>
                </xsl:apply-templates>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="unitInformation" mode="stats">
        <div class="unitLink">
            <a>
                <xsl:attribute name="href">
                    <xsl:text>/stats/</xsl:text><xsl:value-of select="@siteId"/><xsl:text>/</xsl:text><xsl:value-of select="@id"/><xsl:text>/index.html</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="unitname"/>
            </a>
        </div>
    </xsl:template>
	
    <xsl:template match="siteInformation" mode="siteMenu">
        <div class="menuLink">
            <a>
                <xsl:attribute name="href">
                    <xsl:text>#</xsl:text><xsl:value-of select="generate-id(.)"/>
                </xsl:attribute>
                <xsl:value-of select="sitename"/>
            </a>
        </div>
    </xsl:template>
	
	<xsl:template match="role"/>
	<xsl:template match="id"/>
	
    <xsl:template name="loginfail">
    	<xsl:if test="$login='failed'">	
    	<div>Ihr Benutzername oder Passwort ist nicht erkannt worden. Bitte prüfen Sie, ob die Schreibweise korrekt ist und wenden Sie sich gegebenenfalls an Ihren Administrator. Vielen Dank.</div>
    	<br/>
    	</xsl:if>
    </xsl:template>

	<xsl:template match="head/*" mode="format"/>

</xsl:stylesheet>