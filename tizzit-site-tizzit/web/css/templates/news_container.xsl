<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        table, td {
            border:1px solid black;
            border-collapse:collapse;
        }
        .metadata {
            font-weight:bold;
            font-size:11px;
        }
        #news_location{
            font-weight:bold;
        }
        
    </xsl:template>
    
</xsl:stylesheet>