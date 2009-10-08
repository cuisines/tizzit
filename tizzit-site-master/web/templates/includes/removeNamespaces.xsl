<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

<xsl:template match="*">
      <!-- remove element prefix (if any) -->
      <xsl:element name="{local-name()}">
        <!-- process attributes -->
        <xsl:for-each select="@*">
          <!-- remove attribute prefix (if any) -->
          <xsl:attribute name="{local-name()}">
            <xsl:value-of select="."/>
          </xsl:attribute>
        </xsl:for-each>
        <xsl:apply-templates/>
      </xsl:element>
  </xsl:template>

  <!--//
    "Conditional Comments" ausgeben

    @author <a href="eduard.siebert@juwimm.com">Eduard Siebert</a>
    //-->
  <xsl:template match="comment()">
    <xsl:if test="contains(., '[if ')">
      <xsl:comment>
        <xsl:copy-of select="normalize-space(.)"/>
      </xsl:comment>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>