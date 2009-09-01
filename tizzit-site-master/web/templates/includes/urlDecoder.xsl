<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:variable name="hex" select="'0123456789ABCDEF'"/>
	<xsl:variable name="ascii"> !"#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~</xsl:variable>
	<xsl:variable name="latin1">&#160;&#xA1;&#xA2;&#xA3;&#xA4;&#xA5;&#xA6;&#xA7;&#xA8;&#xA9;&#xAA;&#xAB;&#xAC;&#xAD;&#xAE;&#xAF;&#xB0;&#xB1;&#xB2;&#xB3;&#xB4;&#xB5;&#xB6;&#xB7;&#xB8;&#xB9;&#xBA;&#xBB;&#xBC;&#xBD;&#xBE;&#xBF;&#xC0;&#xC1;&#xC2;&#xC3;&#xC4;&#xC5;&#xC6;&#xC7;&#xC8;&#xC9;&#xCA;&#xCB;&#xCC;&#xCD;&#xCE;&#xCF;&#xD0;&#xD1;&#xD2;&#xD3;&#xD4;&#xD5;&#xD6;&#xD7;&#xD8;&#xD9;&#xDA;&#xDB;&#xDC;&#xDD;&#xDE;&#xDF;&#xE0;&#xE1;&#xE2;&#xE3;&#xE4;&#xE5;&#xE6;&#xE7;&#xE8;&#xE9;&#xEA;&#xEB;&#xEC;&#xED;&#xEE;&#xEF;&#xF0;&#xF1;&#xF2;&#xF3;&#xF4;&#xF5;&#xF6;&#xF7;&#xF8;&#xF9;&#xFA;&#xFB;&#xFC;&#xFD;&#xFE;&#xFF;</xsl:variable>

<!--
Beispiel:
		<xsl:call-template name="decode">
			<xsl:with-param name="encoded" select="$url"/>
		</xsl:call-template>
-->

	<xsl:template name="decode">
		<xsl:param name="encoded"/>
		<xsl:choose>
			<xsl:when test="contains($encoded,'%')">
				<xsl:value-of select="substring-before($encoded,'%')" disable-output-escaping="yes"/>
				<xsl:variable name="hexpair" select="translate(substring(substring-after($encoded,'%'),1,2),'abcdef','ABCDEF')"/>
				<xsl:variable name="decimal" select="(string-length(substring-before($hex,substring($hexpair,1,1))))*16 + string-length(substring-before($hex,substring($hexpair,2,1)))"/>
				<xsl:choose>
					<xsl:when test="$decimal &lt; 127 and $decimal &gt; 31">
						<xsl:value-of select="substring($ascii,$decimal - 31,1)" disable-output-escaping="yes"/>
					</xsl:when>
					<xsl:when test="$decimal &gt; 159">
						<xsl:value-of select="substring($latin1,$decimal - 159,1)" disable-output-escaping="yes"/>
					</xsl:when>
					<xsl:otherwise>?</xsl:otherwise>
				</xsl:choose>
				<xsl:call-template name="decode">
					<xsl:with-param name="encoded" select="substring(substring-after($encoded,'%'),3)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$encoded" disable-output-escaping="yes"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--  
Example:
<xsl:template match="/">

    <result>
      <string>
        <xsl:value-of select="$iso-string"/>
      </string>
      <hex>
        <xsl:call-template name="url-encode">
          <xsl:with-param name="str" select="$iso-string"/>
        </xsl:call-template>
      </hex>
    </result>

  </xsl:template>-->
</xsl:stylesheet><!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
