<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1"> 
        
        .image {
            margin-right:10px;
            margin-bottom:50px;
            float:left;
            border:4px solid #eceef0;
            width:130px;
        }
        .imgTxt {
            background-color:#eceef0;
            font-size:11px;
            padding:3px 0 0 5px;
        }
    
    </xsl:template>

</xsl:stylesheet>
