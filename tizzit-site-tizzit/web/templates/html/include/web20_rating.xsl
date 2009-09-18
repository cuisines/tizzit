<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ctmpl="http://www.conquest-cms.net/template"
    xmlns:web20="http://web20.conquest-cms.net/1.0">

    <xsl:variable name="rating" select="source/all/web20/rating"/>
    
    <xsl:variable name="userRating" select="//page/userRating"/>
    
    <xsl:variable name="ratesum" select="//page/ratingSum"/>
    <xsl:variable name="ratecount" select="//page/ratingCount"/>
    <xsl:variable name="rate_average" select="$ratesum div $ratecount"/>


    <!-- RATING -->

    <xsl:template match="web20:rating">
        <xsl:choose>
            <xsl:when test="@scope='page' and $rating = 'true'">
                <ctmpl:module name="pagerating">
                    <xsl:choose>
                        <xsl:when test="$ratesum!=0">
                            <div class="pagerating">
                                <xsl:call-template name="rated_stars">
                                    <xsl:with-param name="curRateAsNo" select="$rate_average"/>
                                    <!--<xsl:with-param name="replace" select="'false'"/>-->
                                </xsl:call-template>
                                <div class="rating_digits">
                                    <xsl:text>(</xsl:text>
                                    <xsl:value-of select="round(10*$rate_average) div 10"/>
                                    <xsl:text>)</xsl:text>
                                </div>
                            </div>
                        </xsl:when>
                    </xsl:choose>
                </ctmpl:module>
                <ctmpl:module name="rate">
                    <div class="rating_box">
                        <div class="rating_text">
                            <xsl:text>Diese Seite ist für mich &#160;(nicht empfehlenswert)</xsl:text>
                        </div>
                        <div class="rating_part">
                            <form action="" method="post" accept-charset="utf-8" name="rating"
                                onsubmit="javascript:submitRating(this,'CQRATINGPAGE');">
                                <div class="sterne">
                                    <div class="stern1" id="star_1">
                                        <xsl:call-template name="starAttrib">
                                            <xsl:with-param name="curItem" select="'1'"/>
                                        </xsl:call-template> &#160; </div>
                                    <div class="stern2" id="star_2">
                                        <xsl:call-template name="starAttrib">
                                            <xsl:with-param name="curItem" select="'2'"/>
                                        </xsl:call-template> &#160; </div>
                                    <div class="stern3" id="star_3">
                                        <xsl:call-template name="starAttrib">
                                            <xsl:with-param name="curItem" select="'3'"/>
                                        </xsl:call-template> &#160; </div>
                                    <div class="stern4" id="star_4">
                                        <xsl:call-template name="starAttrib">
                                            <xsl:with-param name="curItem" select="'4'"/>
                                        </xsl:call-template> &#160; </div>
                                    <div class="stern5" id="star_5">
                                        <xsl:call-template name="starAttrib">
                                            <xsl:with-param name="curItem" select="'5'"/>
                                        </xsl:call-template> &#160; </div>
                                </div>
                                <!--
                                <div class="sterne">
                                    <div class="stern1" id="star_1" onmouseover="javascript:buttonHover(this);" onmouseout="javascript:buttonNormal(this);" onclick="javascript:setRating(this);">
                                        &#160;
                                    </div>
                                    <div class="stern2" id="star_2" onmouseover="javascript:buttonHover(this);" onmouseout="javascript:buttonNormal(this);" onclick="javascript:setRating(this);">
                                        &#160;
                                    </div>
                                    <div class="stern3" id="star_3" onmouseover="javascript:buttonHover(this);" onmouseout="javascript:buttonNormal(this);" onclick="javascript:setRating(this);">
                                        &#160;
                                    </div>
                                    <div class="stern4" id="star_4" onmouseover="javascript:buttonHover(this);" onmouseout="javascript:buttonNormal(this);" onclick="javascript:setRating(this);">
                                        &#160;
                                    </div>
                                    <div class="stern5" id="star_5" onmouseover="javascript:buttonHover(this);" onmouseout="javascript:buttonNormal(this);" onclick="javascript:setRating(this);">
                                        &#160;
                                    </div>
                                </div>-->
                                <div class="rating_text2">
                                    <xsl:text>&#160;(sehr empfehlenswert)</xsl:text>
                                </div>
                                <div id="ratebtn">
                                    <!--<input class="submit" type="submit" value="Bewertung abgeben" />-->
                                    <!-- example 
                                    -->
                                    <input class="submit" type="submit" value=">">
                                        <xsl:if test="$userRating != ''">
                                            <xsl:attribute name="style">
                                                <xsl:text>display:none;</xsl:text>
                                            </xsl:attribute>
                                        </xsl:if>
                                    </input>
                                </div>
                                
                                <input type="hidden" id="CQRATINGPAGE" name="CQRATINGPAGE"
                                    class="searchinput"/>
                            </form>
                        </div>
                        <br class="clear"/>
                        <xsl:if test="$userRating!=''">
                            <div class="rating_finished">
                                <xsl:text>Vielen Dank für Ihre Bewertung!</xsl:text>
                            </div>
                        </xsl:if>
                    </div>
                </ctmpl:module>
            </xsl:when>
        </xsl:choose>

        <!-- Template pageranking stellt nur die bestbewertetsten Seiten dar, 
             deshalb scope 'site' abhängig vom Template-->

        <xsl:choose>
            <xsl:when test="@scope='site' and $template='pageranking'">
                <ctmpl:module name="pageranking">
                    <xsl:if test="highestRatings!=''">
                        <div class="pageranking">
                            <xsl:apply-templates select="highestRatings"/>
                        </div>
                    </xsl:if>
                </ctmpl:module>
            </xsl:when>
            <!--<xsl:otherwise>&#160;</xsl:otherwise>-->
        </xsl:choose>
    </xsl:template>

    <xsl:template match="rating">
        <xsl:variable name="CurScore">
            <xsl:value-of select="@score"/>
        </xsl:variable>
        <div class="pageUrl">
            <xsl:call-template name="rated_stars">
                <xsl:with-param name="curRateAsNo" select="number(replace(@score, ',', '.'))"/>
                <!--<xsl:with-param name="replace" select="'true'"/>-->
            </xsl:call-template>
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="page/pageUrl"/>
                    <xsl:text>/</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="page/pageName"/>
            </a>
        </div>
    </xsl:template>

    <xsl:template name="starAttrib">
        <xsl:param name="curItem" select="''"/>
        <xsl:if test="not($userRating)">
            <xsl:attribute name="onmouseover">
                <xsl:text>javascript:buttonHover(this);</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="onmouseout">
                <xsl:text>javascript:buttonNormal(this);</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="onclick">
                <xsl:text>javascript:setRating(this);</xsl:text>
            </xsl:attribute>
        </xsl:if>
        <xsl:if test="$curItem &lt;= $userRating">
            <xsl:attribute name="style">
                <xsl:text>background-image: url(/httpd/img/star_akt.gif);</xsl:text>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="rated_stars">
        <xsl:param name="curRateAsNo" select="''"/>
        <!--<xsl:param name="replace" select="'false'"/>-->
        
        <!-- MORGEN: Schön machen! replace beim übergeben, nich hier -->
        
        <!--<xsl:variable name="curRateAsNo">
            <xsl:choose>
                <xsl:when test="$replace='true'">
                    <xsl:value-of select="number(replace($curRate, ',', '.'))"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$curRate"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>-->
        
        <div class="rating">
            <div>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="$curRateAsNo &gt;= 0.6">
                            <xsl:text>stern_oben1_h</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>stern_oben1</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute> &#160; 
            </div>
            <div>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="$curRateAsNo &gt;= 1.6">
                            <xsl:text>stern_oben2_h</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>stern_oben2</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute> &#160; 
            </div>
            <div>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="$curRateAsNo &gt;= 2.6">
                            <xsl:text>stern_oben3_h</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>stern_oben3</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute> &#160; 
            </div>
            <div>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="$curRateAsNo &gt;= 3.6">
                            <xsl:text>stern_oben4_h</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>stern_oben4</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute> &#160; 
            </div>
            <div>
                <xsl:attribute name="class">
                    <xsl:choose>
                        <xsl:when test="$curRateAsNo &gt;= 4.6">
                            <xsl:text>stern_oben5_h</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>stern_oben5</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute> &#160; 
            </div>
        </div>
    </xsl:template>
    

</xsl:stylesheet>
