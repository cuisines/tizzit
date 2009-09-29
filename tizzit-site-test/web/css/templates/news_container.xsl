<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        .preText, .newsItem {
            margin-bottom:20px;
        }
        .afterText {
            margin-top:20px;
        }
        .newsDate {
            font-size:11px;
        }
        .moreLink {
            font-weight:bold;
        }
        .metadata {
            font-style:italic;
            color:#8c8c8c;
            font-size:11px;
            font-family:Arial;
        }
        h1.headline {
            padding-bottom:10px;
        }
        
    </xsl:template>
    
</xsl:stylesheet>