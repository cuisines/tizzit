<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	
    <xsl:template match="content" mode="format" priority="1.1">
        <xsl:apply-templates mode="format"/>
        <xsl:if test="../downloadlist/item/downloadname!=''">
            <hr/>
            <div class="downloads">
                <xsl:apply-templates select="../downloadlist/item"/>
            </div>
            <div class="clear">&#160;</div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="item">
        <div class="downloadItem">
            <h2 class="downloadName">
                <xsl:value-of select="downloadname"/>
            </h2>
            <xsl:if test="downloadUpload">
                <div class="downloadUpload">
                    <xsl:text>uploaded </xsl:text>
                    <xsl:value-of select="downloadUpload/year"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="downloadUpload/month"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="downloadUpload/day"/>
                </div>
            </xsl:if>
            <div class="clear">&#160;</div>
            <xsl:if test="text!=''">
                <div class="downloadDescription">
                    <xsl:apply-templates select="text" mode="format"/>
                </div>
            </xsl:if>
            <div class="downloadLink">
                <a>
                    <xsl:attribute name="href">
                        <xsl:value-of select="downloadURL/a/@href"/>
                    </xsl:attribute>
                    <xsl:attribute name="targer">
                        <xsl:value-of select="downloadURL/a/@target"/>
                    </xsl:attribute>
                    <span class="download_left">&#160;</span>
                    <span class="download_middle">
                        <xsl:value-of select="downloadname"/>
                    </span>
                    <span class="download_right">
                        <xsl:choose>
                            <xsl:when test="downloadVersion">
                                <span class="versionNumber"><xsl:value-of select="downloadVersion"/></span>
                            </xsl:when>
                            <xsl:otherwise>
                                &#160;
                            </xsl:otherwise>
                        </xsl:choose>
                    </span>
                </a>
            </div>
        </div>
        <div class="clear">&#160;</div>
        <xsl:if test="position()!=last()">
            <hr/>
        </xsl:if>
    </xsl:template>
	
</xsl:stylesheet>