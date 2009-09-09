<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="template" select="''"/>

    <xsl:template match="all" priority="1.1">
        <xsl:element name="all">
            <xsl:choose>
                <xsl:when test="$template = 'pageranking'">
                    <web20:rating xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" quantity="10"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="$template = 'tagcloud'">
                            <web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" form="cloud" quantity="50"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <!--<web20:rating xmlns:web20="http://web20.conquest-cms.net/1.0" scope="page"/>
                            <web20:comment xmlns:web20="http://web20.conquest-cms.net/1.0" quantity="10"/>-->
                            <web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" form="cloud" quantity="20"/>
                            <web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="page" quantity="10"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
                        
            <!-- rating für aktuelle seite -->
            <!--<web20:rating xmlns:web20="http://web20.conquest-cms.net/1.0" scope="page"/>-->
            <!-- muss nur ins Template Seitenbewertung -->
            <!-- rating für die ganze seite, anzahl der höchstbewerteten seiten wird zurückgegeben -->
            <!--<web20:rating xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" quantity="10"/>-->
            <!-- Kommentare, liefert aber alle zurück, nicht nur quantity -->
            <!--<web20:comment xmlns:web20="http://web20.conquest-cms.net/1.0" quantity="10"/>-->
            <!-- tagging pro seite, anzahl der angezeigten tags -->
            <!--<web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="page" quantity="10"/>-->
            <!-- tagging-cloud -->
            <!--<web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" form="cloud" quantity="10"/>-->
            <!-- 10 meist getaggten seiten -->
            <!--<web20:tagging xmlns:web20="http://web20.conquest-cms.net/1.0" scope="site" form="ranking" quantity="10"/>-->
            
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="@*|node()" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
