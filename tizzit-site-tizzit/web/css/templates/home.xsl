<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        #contentcontainer {
            margin-top: 37px!important;
            margin-top: 10px;
            padding:0 30px 0 0;
        }
        .kwicks {  
            list-style: none;  
            position: relative;  
            margin: 0;  
            padding: 0;  
            float:right;
        }  
        .kwicks a{  
            display: block;
            height: 310px;
            text-decoration: none;  
        } 
        .kwicks li{  
            display: block;  
            overflow: hidden;  
            padding: 0;  
            cursor: pointer;  
        }  
        .kwicks li{  
            float: left;  
            width: 220px;  
            height: 310px;  
            margin-right: 20px;  
        } 
        .kwicks li.kwick_last{  
            margin-right: 0px;  
            float: left;  
            width: 220px;  
            height: 310px;  
        } 
        <!--#kwick1 {    
            background-image: url('/httpd/img/home/01.png');  
            background-repeat: no-repeat;
        }  
        #kwick2 {    
            background-image: url('/httpd/img/home/02.png');
            background-repeat: no-repeat;
        }  
        #kwick3 {   
            background-image: url('/httpd/img/home/03.png');
            background-repeat: no-repeat;
        }  -->
        .kwick_bg_01 {
            background-image: url('/httpd/img/home/round_decisionmaker.png');
            background-repeat: no-repeat;
            background-position:right 0px;
        }
        .kwick_bg_02 {
            background-image: url('/httpd/img/home/round_user.png');
            background-repeat: no-repeat;
            background-position:right 0px;
        }
        .kwick_bg_03 {
            background-image: url('/httpd/img/home/round_developer.png');
            background-repeat: no-repeat;
            background-position:right 0px;
        }
        .content {
            padding: 15px 0px 30px 300px;
        }
        
    </xsl:template>
        
</xsl:stylesheet>