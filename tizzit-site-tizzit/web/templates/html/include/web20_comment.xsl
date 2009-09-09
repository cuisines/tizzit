<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ctmpl="http://www.conquest-cms.net/template"
    xmlns:web20="http://web20.conquest-cms.net/1.0">

    <!--<xsl:include href="../variables.xsl"/>
    <xsl:include href="../../../../../juwimm-web-cms/web/templates/variables.xsl"/>
-->
    <xsl:variable name="comment" select="source/all/web20/comment"/>
    
    <!-- COMMENT -->
    
    <xsl:template match="web20:comment">
        <xsl:variable name="toggle_id" select="'toggle_id'"/>
        <xsl:variable name="toggle_bild" select="'toggle_bild'"/>
        <xsl:choose>
            <xsl:when test="$comment = 'true'">
                <ctmpl:module name="comment">
                    <div class="comment_box">
                        <div class="comments_head">
                            <xsl:text>Kommentare</xsl:text>
                        </div>
                        <div class="comment_text">
                            <img src="/httpd/img/comment_arrow.gif" border="0">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="$toggle_bild"/>
                                </xsl:attribute>
                            </img>
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:text>javascript:toggleTextfield('</xsl:text>
                                    <xsl:value-of select="$toggle_id"/>
                                    <xsl:text>','</xsl:text>
                                    <xsl:value-of select="$toggle_bild"/>
                                    <xsl:text>');</xsl:text>
                                </xsl:attribute>
                                <xsl:text>Kommentar schreiben</xsl:text>
                            </a>
                        </div> 
                        <br class="clear"/>
                        <form action="" method="post" accept-charset="utf-8" name="comment" onsubmit="return checkTaggingfield(this);">
                            <div style="display:none;">
                                <xsl:attribute name="id">
                                    <xsl:value-of select="$toggle_id"/>
                                </xsl:attribute>
                                <input type="hidden" name="CQTITLE" value="'-'" class="titleinput"/>
                                <textarea rows="5" cols="98" maxlength="255" name="CQCOMMENT" class="commentinput" onclick="clearValue(this)" onfocus="clearValue(this);" onselect="clearValue(this);">&#160;</textarea>
                                <input name="commentbtn" class="submit" type="submit" value="Absenden"/>
                            </div>
                            <br class="clear"/>	
                        </form>
                        <xsl:if test="page!=''">
                            <div class="all_comments"> 
                                <div class="old_comments">
                                    <xsl:apply-templates select="page/pageComment" mode="format">
                                        <xsl:sort order="descending"></xsl:sort>
                                    </xsl:apply-templates>
                                </div>
                            </div>
                        </xsl:if>
                    </div>
                </ctmpl:module>                
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="pageComment" mode="format">
        <div class="comment_author">
            <xsl:apply-templates select="userName"/>
            <xsl:text> (</xsl:text>
            <xsl:apply-templates select="lastModifiedDate"/>
            <xsl:text>):</xsl:text>
        </div>
        <div class="comment_content">
            <xsl:apply-templates select="commentText"/>
        </div>
        <br class="clear"/>
    </xsl:template>
    
    <xsl:template match="userName">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="commentText">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="lastModifiedDate">
        <xsl:apply-templates/>
    </xsl:template>

</xsl:stylesheet>
