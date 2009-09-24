<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        #middle {
            width:779px;
        }
        .headline {
            padding-left:280px;
        }
        .sitemap a {
            font-size:16px;
            font-family:Trebuchet MS;
            font-weight:bold;
            background-image:url('/httpd/img/sitemap/link_bg.gif');
            background-repeat:no-repeat;
            background-position:0 6px;
            padding:0 0 15px 9px; 
            display:block;
            text-decoration:underline;
        }
        sitemap a:hover {
            color:#96CF48;
        }
        .s_firstlevel, .s_secondlevel, .sitemap_left, .root, .s_links {
            float:left;
        }
        .root {
            font-size:16px;
            font-family:Trebuchet MS;
            font-weight:bold;
            background-image:url('/httpd/img/sitemap/root_bg.jpg');
            background-repeat:no-repeat;
            background-position:0 0;
            padding-left:15px;
            width:80px;
        }
        .sitemap_left {
            background-image:url('/httpd/img/sitemap/sitemap_bg.jpg');
            background-repeat:no-repeat;
            background-position:0 0;
            width:280px;
            height:300px;
        }
        .s_links {
            width:400px;
        }
        .s_firstlevel, .s_secondlevel {
            padding-right:15px;
        }
        
    </xsl:template>
    
</xsl:stylesheet>