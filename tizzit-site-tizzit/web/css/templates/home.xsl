<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        #contentcontainer {
            margin-top: 37px!important;
            margin-top: 10px;
            padding:0 30px 0 0;
            background-image:url('/httpd/img/underwater.jpg');
            background-repeat:no-repeat;
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
            padding: 30px 0px 30px 300px;
            background-image:url('/httpd/img/underwater.gif');
            background-repeat:no-repeat;
        }
        .container { 
            <!--width:500px;--> 
            height:315px; 
            overflow:hidden;
            position:relative; 
            cursor:pointer; 
        }
        .slides { 
            position:absolute; 
            top:0; 
            left:0; 
        }
        .slides div { 
            position:absolute; 
            top:0; 
            <!--width:1000px;--> 
            <!--display:none; -->
        }
        a.previous { 
            position:absolute; 
            top:370px; 
            left:278px; 
        }
        a.next { 
            position:absolute; 
            top:370px; 
            right:0px; 
        }
        ul.pagination { 
            list-style-type:none; 
            margin:0; 
            padding:0; 
            margin:9px auto; 
            width:85px; 
        }
        ul.pagination li { 
            float:left; 
            margin:0 5px; 
        }	
        ul.pagination a { 
            display:block; 
            width:16px; 
            padding-top:16px; 
            height:0; 
            overflow:hidden; 
            background-image:url('/httpd/img/home/pagination.png'); 
            background-position:-18px 0; 
            background-repeat:no-repeat;
        }
        ul.pagination li.active a {
            background-position:0px 0;
        }
        #video_container {
            margin:30px 0 0 93px;
        }
        #cloud {
            position:absolute;
            width:170px;
            top:350px;
            padding:0 0 0 40px;
        }
        #cloud a{
            color:#78a7c5;
            text-decoration:none;
        }
        #cloud a:hover{
            text-decoration:underline;
        }
        
        
    </xsl:template>
        
</xsl:stylesheet>