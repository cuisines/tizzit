<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../../tizzit-site-master/web/jscript/jsmastersheet.xsl"/>

    <xsl:param name="language" select="''"/>
    <xsl:param name="siteId" select="''"/>
    <xsl:param name="viewComponentId"/>

    <xsl:template match="search3.0" priority="3">
        <![CDATA[
        
        function fastSearch2 (path) {
            var siteId = ]]><xsl:choose><xsl:when test="$siteId!=''"><xsl:value-of select="$siteId"/></xsl:when><xsl:otherwise>null</xsl:otherwise></xsl:choose><![CDATA[;
            var searchItem = document.fastsearch.cqWebSearchQuery.value;
        
            //if wenn kein Suchbegriff, dann keine Suche
            if (document.fastsearch.cqWebSearchQuery.value=='' || document.fastsearch.cqWebSearchQuery.value=='Suche' || document.fastsearch.cqWebSearchQuery.value=='Search') {
               return;
            }
            else {
               document.fastsearch.action = "/]]><xsl:value-of select="$language"/><![CDATA[/"+path+"?cqWebSearchQuery=metadata:(" + searchItem + ")^4 url:(" + searchItem + ")^3 title:(" + searchItem + ")^2 contents:(" + searchItem + ")) OR (documentName:(" + searchItem + ")^3 title:(" + searchItem + ")^2 contents:(" + searchItem + "))";
               document.fastsearch.submit();
            }
        }
        
        ]]>
    </xsl:template>

</xsl:stylesheet>