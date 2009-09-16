<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ctmpl="http://www.conquest-cms.net/template"
    xmlns:web20="http://web20.conquest-cms.net/1.0">

    <xsl:variable name="tagging" select="source/all/web20/tagging"/>
    <xsl:variable name="tagcloud" select="source/all/tagcloud/tagcloud"/>
    
    <xsl:param name="tag"/>
    
    <!-- TAGGING -->
    <xsl:template match="web20:tagging">
        <xsl:choose>
            <xsl:when test="@scope='page' and $tagging = 'true'">
                <xsl:if test="$liveserver='false'">
                    <ctmpl:module name="tagging">
                        <div class="tagging_content">
                            <xsl:if test="page!=''">
                                <br/><hr/><br/>
                                <div class="tags" style="font-weight:bold;">
                                    <xsl:text>Bisherige Tags für diese Seite:</xsl:text>                            
                                </div>
                                <xsl:apply-templates select="page" mode="tagging"/>
                            </xsl:if>
                            <br/>
                            <div class="tagging_box">
                                <div class="tagging_text" style="font-weight:bold;">
                                    <xsl:text>Schlagworte für diese Seite:</xsl:text>
                                </div>
                                <form action="" method="post" accept-charset="utf-8" name="tagging" onsubmit="return checkTaggingfield(this);">
                                    <div id="tagging">
                                        <input type="text" name="CQTAGGING" class="tagginginput"/>
                                        <input name="tagbtn" class="submit" type="submit" value="Absenden"/>
                                    </div>
                                    <br class="clear"/>	
                                </form>
                            </div>
                            <hr/>
                        </div>
                    </ctmpl:module>
                </xsl:if>
            </xsl:when>
        </xsl:choose>   
        <xsl:choose>
            <xsl:when test="@scope='site' and $tagcloud = 'true'">
                <ctmpl:module name="tagcloud">
                    <xsl:choose>
                        <xsl:when test="$tag != ''">
                            <div id="cloudlist">
                                <xsl:apply-templates select="tagCloud/tag" mode="taglist">
                                    <xsl:with-param name="tagname" select="$tag"/>
                                </xsl:apply-templates>
                            </div>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:if test="tagCloud/tag/tagName!=''">
                        <div id="cloud">
                            <xsl:apply-templates select="tagCloud/tag/tagName" mode="style">  
                                <xsl:sort select="." data-type="text" case-order="lower-first" order="ascending"/>
                            </xsl:apply-templates>&#160;
                        </div>
                    </xsl:if>&#160;
                    <div class="clear">&#160;</div>
                </ctmpl:module>
                <div class="clear">&#160;</div>
            </xsl:when>
        </xsl:choose>     
    </xsl:template>
    
    <xsl:template match="tag" mode="taglist">
        <xsl:param name="tagname" select="''"/>
            <xsl:choose>
                <xsl:when test="$tagname = tagName">
                    <a name="tagging" class="nameA">&#160;</a>
                    <div id="divlayer_tagging">
                        <div class="tagging_top">&#160;</div>
                        <div class="tagging_middle">
                            <div class="taglist">
                                <div class="tag_headline">
                                    <div class="tag_clicked">
                                        <xsl:apply-templates select="tagName"/>
                                    </div>
                                    <xsl:text>verlinkt auf:</xsl:text><br/>
                                </div>
                                <ul>
                                    <xsl:apply-templates select="page" mode="format"/>
                                </ul>
                            </div>
                        </div>
                        <div class="tagging_bottom">&#160;</div>
                    </div>
                </xsl:when>                
            </xsl:choose>
    </xsl:template>
    
    <xsl:template match="page" mode="format">    
        <li>
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="pageUrl"/>
                    <xsl:text>/page.html</xsl:text>
                </xsl:attribute>
                <xsl:apply-templates select="pageName"/>
            </a>
        </li>
    </xsl:template>
        
    <xsl:template match="tagName" mode="style">
        <xsl:param name="tag_min">
            <xsl:value-of select="../../tag[position()=last()]/@count"/>
        </xsl:param>
        <xsl:param name="tag_max">
            <xsl:value-of select="../../tag[position()=1]/@count"/>
        </xsl:param>
        <xsl:param name="cur_anzahl">
            <xsl:value-of select="../@count"/>
        </xsl:param>
        <xsl:param name="fontsize">
            <xsl:value-of select="26 * ($cur_anzahl - $tag_min) div ($tag_max - $tag_min)"/>
        </xsl:param>
            <a>
                <xsl:attribute name="href">
                    <xsl:text>?tag=</xsl:text>
                    <xsl:value-of select="."/> 
                </xsl:attribute> 
                <xsl:attribute name="style">
                    <xsl:text>font-size:</xsl:text>
                    <xsl:choose>
                        <xsl:when test="$fontsize=26">
                            <xsl:value-of select="17"/>
                        </xsl:when>
                        <xsl:when test="$fontsize>0">
                            <xsl:value-of select="$fontsize"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="10"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:text>px;</xsl:text>
                </xsl:attribute>
                <xsl:apply-templates select="."/>
            </a>&#160;
    </xsl:template>

    <xsl:template match="page" mode="tagging">
        <div class="all_tags">
            <xsl:apply-templates select="tag"/>
        </div>
    </xsl:template>

    <xsl:template match="tag">
        <xsl:value-of select="."/>
        <xsl:if test="position()!=last()">
            <xsl:text>, </xsl:text>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>