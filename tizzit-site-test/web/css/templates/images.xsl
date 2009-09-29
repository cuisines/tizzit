<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1"> 
        
        #middle {
            width:665px;
        }
        
        @media screen, projection { 
            .imageflow {
                overflow:hidden; 
                position:relative; 
                text-align:left;
                visibility:hidden; 
                width:100%;
            } 
            .imageflow img {
                border:none; 
                position:absolute; 
                top:0px;
                visibility:hidden; 
                -ms-interpolation-mode:bicubic
            }
            .imageflow p {
                margin:0 auto;
                text-align:center
            }
            .imageflow .loading {
                border:1px solid white; 
                height:15px; 
                left:50%;
                margin-left:-106px; 
                padding:5px; 
                position:relative; 
                visibility:visible;
                width:200px;
            }
            .imageflow .loading_bar {
                background:#fff; 
                height:15px; 
                visibility:visible;
                width:1%;
            }
            .imageflow .navigation {
                z-index:10000;
            }
            .imageflow .caption {
                font-weight:bold;
                position:relative; 
                text-align:center; 
                z-index:10001;
            }
            .imageflow .scrollbar {
                border-bottom:1px solid #b3b3b3; 
                position:relative; 
                visibility:hidden; 
                z-index:10002; 
                height:1px;
            }
            .imageflow .slider {
                background-image:url(/httpd/img/slideshow/slider.png); 
                background-repeat:no-repeat; 
                height:14px;
                margin:-6px 0 0 -7px; 
                position:absolute; 
                width:14px; 
                z-index:10003;
            }
            .imageflow .images {
                overflow:hidden; 
                white-space:nowrap;
            }
            .imageflow .button {
                cursor:pointer; 
                height:17px;
                position:relative; 
                width:17px;
            }
            .imageflow .previous {
                background:url(/httpd/img/slideshow/button_left.png) top left no-repeat; 
                float:left; 
                margin:-7px 0 0 -30px;
            } 
            .imageflow .next {
                background:url(/httpd/img/slideshow/button_right.png) top left no-repeat; 
                float:right; 
                margin:-7px -30px 0 30px;
            }
        }
    
    </xsl:template>

</xsl:stylesheet>
