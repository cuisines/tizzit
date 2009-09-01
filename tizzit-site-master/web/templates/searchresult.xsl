<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="format" select="'long'"/>

	<xsl:template match="content" mode="include" priority="2">
		<xsl:apply-templates select="../../head/searchresults" mode="start"/>
	</xsl:template>

	<xsl:template match="searchresults" mode="start">
		<xsl:choose>
			<xsl:when test="@count &gt; 0">
				<!--Suche liefert Ergebnis(se)-->
				<xsl:if test="@count = 1"><div class="subline">Es wurde <xsl:value-of select="@count"/> Dokument gefunden!</div></xsl:if>
				<xsl:if test="@count &gt; 1"><div class="subline">Es wurden <xsl:value-of select="@count"/> Dokumente gefunden!</div></xsl:if>
				<br/>
				<!--<xsl:apply-templates mode="start"/>-->
				<table border="0" cellpadding="2" cellspacing="2" width="100%">
<!--					<tr>
						<th>Nr.</th>
						<th>Quote</th>
						<th>Seitentitel / Ausschnitt</th>
					</tr>-->
					<xsl:apply-templates mode="start"/>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<!--Suche ohne Ergebnis-->
				<div class="subline"><xsl:value-of select="'Leider konnten zu Ihrer Anfrage keine passenden Dokumente gefunden werden!'"/></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="result" mode="start">
		<xsl:variable name="tmp"><xsl:value-of select="url/text()"/></xsl:variable>
		<xsl:variable name="length"><xsl:value-of select="string-length($tmp)"/></xsl:variable>
		<xsl:variable name="new">
			<xsl:choose>
				<xsl:when test="substring($tmp, $length, 1) != '/'">
					<xsl:variable name="front"><xsl:value-of select="substring-before($tmp,'/content.html')"/></xsl:variable>
					<xsl:value-of select="concat($front, '/page.html')"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$tmp"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="score">
			<xsl:value-of select="substring-before(score/text(), '%')"/>
		</xsl:variable>
		<xsl:if test="string-length(title) &gt; 0 or string-length(documentName) &gt; 0">
			<dl>
				<dt>
					<xsl:choose>
						<xsl:when test="@resultType='document'">
							<img src="/httpd/img/data.gif" border="0"/><strong><a><xsl:attribute name="href">/img/ejbfile/<xsl:value-of select="documentName/text()"/>?id=<xsl:value-of select="documentId/text()"/></xsl:attribute><xsl:value-of select="documentName/text()"/></a>&#160;</strong>
						</xsl:when>
						<xsl:otherwise>
							<strong><a><xsl:attribute name="href"><xsl:value-of select="$new"/></xsl:attribute><xsl:value-of select="title/text()"/></a>&#160;</strong>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="$score &gt; 75"><img src="/httpd/img/star.gif"/></xsl:if>
					<xsl:if test="$score &gt; 50"><img src="/httpd/img/star.gif"/></xsl:if>
					<xsl:if test="$score &gt; 25"><img src="/httpd/img/star.gif"/></xsl:if>
					<xsl:if test="$score &gt;= 0"><img src="/httpd/img/star.gif"/></xsl:if>
				</dt>
				<dd>
					<div>
						<xsl:if test="$format = 'long'"><xsl:value-of select="summary/text()"/><b><tt> ...</tt></b><br/></xsl:if>
						<!--<i><a target="_blank"><xsl:attribute name="href"><xsl:value-of select="$new"/></xsl:attribute><xsl:value-of select="translate($new,'%20','&#32;')"/></a></i>-->
					</div>
				</dd>
			</dl>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>