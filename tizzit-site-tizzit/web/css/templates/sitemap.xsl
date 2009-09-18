<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        .layer {  
            padding: 10px 2px 2px 0px ;
            margin-left:0px;
            width : 370px;
        }
        .layer a:hover{
        
        }
        .layer a{
            padding-top:2px;
        }
        .layerRoot {
            width : 370px;
        }
        .layerSub {
            padding: 12px 2px 2px 22px ; 
            background-position:21px 15px; 
            background-image:url(/httpd/img/sitemap/sitemap_linienkachel.gif); 
            background-repeat:repeat-y; 
            width : 370px;        
        }
        .layerSubLast {        
            padding: 12px 2px 2px 22px ; 
            background-position:21px 0px; 
            background-image:url(/httpd/img/sitemap/sitemap_eckstueck.gif); 
            background-repeat:no-repeat;
            width : 370px;
        }
        .layer img {
            float:left;
            vertical-align: top;
            padding-right:10px;
        }
        .linkSitemap {
            display:block;
        } 
        
    </xsl:template>
    
</xsl:stylesheet>