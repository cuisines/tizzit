<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        .downloads {
            margin-top:20px;
        }
        .download_left, .download_right, .download_middle {
            display:block;
            float:left;
            height:44px;
            padding-top:25px;
            font-size:24px;
            color:#fff;
        }       
        .download_left {
            background-image:url('/httpd/img/downloads/downloads_bg_left.jpg');
            background-repeat:no-repeat;
            width:58px;
        }
        .download_middle {
            background-image:url('/httpd/img/downloads/downloads_bg_middle.jpg');
            background-repeat:repeat-x;
            padding:24px 2px 0 0;
        }
        .download_right {
            background-image:url('/httpd/img/downloads/downloads_bg_right.jpg');
            background-repeat:no-repeat;
            width:47px;
            padding:24px 0 0 16px;
        }
        .downloadLink {
            float:right;
        }
        .versionNumber {
            font-size:20px;
            font-style:italic;
        }
        .downloadUpload, h2.downloadName {
            float:left;
        }
        .downloadUpload {
            padding-left:10px;
            font-family:Trebuchet MS;
            font-size:12px;
        }
        
    </xsl:template>
    
</xsl:stylesheet>