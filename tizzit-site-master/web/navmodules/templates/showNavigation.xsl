<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Bildet einen Navigationsbaum ab
     Damit dies geschieht muss man in der jeweiligen Sitemap die Navmodulresource und den matcher um folgendnen Parameter erweitern
    <map:parameter name="myTemplate" value="{request-param:myTemplate}"/>	
    
    
    Beispiel: UKW, UKW Intra
 -->


    <xsl:param name="myTemplate"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Navigationsübersicht</title>
            </head>
            <body style="font-size:12px; font-family:Arial;">
                <xsl:apply-templates select="showNavigation"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="showNavigation" priority="0.1">
        <div>
            <h1>Navigationsübersicht</h1>
            <br/>
            <xsl:text>Bitte hier das template eintragen:</xsl:text>
            <br/>
        </div>
        <div>
            <xsl:call-template name="searchmask"/>
           <xsl:choose>
               <xsl:when test="$myTemplate != ''">
                   <h2>Es wurde nach dem Tempalte <xsl:value-of select="$myTemplate"/> gesucht</h2>  
               </xsl:when>
               <xsl:otherwise>
                   <h2>Hier wird die gesamte Navigation abgebildet:</h2>
               </xsl:otherwise>                            
           </xsl:choose>
            <br/> 
            
            <xsl:apply-templates select="navigation/viewcomponent"/>  
        </div>
    </xsl:template>

    <xsl:template match="viewcomponent">
        <div style="padding-left:10px;">
            <xsl:choose>
                <xsl:when test="@hasChild='true' or  @parent='1'">
                    <b>
                        <xsl:apply-templates select="linkName"/>
                    </b>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="linkName"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text> ---- </xsl:text>
            <xsl:apply-templates select="url"/>
            <xsl:text> ---- </xsl:text>
            <xsl:choose>
                <xsl:when test="template">
                    <xsl:apply-templates select="template"/>
                </xsl:when>
                <xsl:otherwise> Externer/Interner/Sym-LINK </xsl:otherwise>
            </xsl:choose>
        </div>
        <xsl:if test="viewcomponent">
            <div style="padding-left:10px;">
                <xsl:choose>
                    <xsl:when test="$myTemplate=''">
                        <xsl:apply-templates select="viewcomponent"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select=".//viewcomponent[template=$myTemplate]"/>
                    </xsl:otherwise>
                </xsl:choose>               
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="searchmask">
        <div>           
            <form method="get" action="">
                <input id="myTemplate" name="myTemplate"/>
                <input type="submit"/>
            </form>
        </div>
    </xsl:template>

</xsl:stylesheet>
