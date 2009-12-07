<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:wiki="http://plugins.tizzit.org/ConfluenceContentPlugin" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="common.xsl" />

    <xsl:template match="wiki:confluencePage">
        <ctmpl:module name="content" xmlns:ctmpl="http://www.conquest-cms.net/template">
            <div class="content">
                <table>
                    <tr>
                        <th>Key</th>
                        <th>Value</th>
                    </tr>
                    <xsl:for-each select="./@*">
                        <tr>
                            <td>
                                <xsl:value-of select="name()" />
                            </td>
                            <td>
                                <xsl:value-of select="." />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                <xsl:copy-of copy-namespaces="no" select="wiki:renderedContent/*" />
            </div>
        </ctmpl:module>
    </xsl:template>

</xsl:stylesheet>
