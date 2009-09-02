<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:include href="common.xsl"/>

    <!-- eigene Styles fuer dieses Template -->
    <xsl:template match="styles" mode="template" priority="1">
        
        #contentcontainer {
            min-height:430px;
        }
        #show {
            position: relative;
        }
        #p-top-r {
            position: absolute;
            top:0px;
            right:-44px;
            z-index: 255;
            background-image:url('/httpd/img/slideshow/panorama-top-r.png');
            background-repeat:no-repeat;
            width:50px;
            height:50px;
        }
        #p-top-l {
            position: absolute;
            top:0px;
            left:0px;
            z-index: 255;
            background-image:url('/httpd/img/slideshow/panorama-top-l.png');
            background-repeat:no-repeat;
            width:50px;
            height:50px;
        }
        #p-bottom-r {
            position: absolute;
            bottom:0px;
            right:0px;
            z-index: 255;
        }
        #p-bottom-l {
            position: absolute;
            bottom:0px;
            left:0px;
            z-index: 255;
        }
        <!--Slideshow--> 
        .slideshow {
            display: block;
            position: relative;
            z-index: 0;
        }
        .slideshow-images {
            display: block;
            overflow: hidden;
            position: relative;
        }		
        .slideshow-images img {
            display: block;
            position: absolute;
            z-index: 1;
        }		
        .slideshow-thumbnails {
            overflow: hidden;
        }
        .slideshow-images {
            height: 300px;
            width: 400px;
        }		
        .slideshow-images-visible { 
            opacity: 1;
        }	
        .slideshow-images-prev { 
            opacity: 0; 
        }
        .slideshow-images-next { 
            opacity: 0; 
        }
        .slideshow-images img {
            float: left;
            left: 0;
            top: 0;
        }	
        .slideshow {
            height: 300px;
            <!--margin: 0 auto;-->
            width: 440px;
        }
        .slideshow a img {
            border: 0;
        }
        .slideshow-captions {
            background: #000;
            bottom: 0;
            color: #FFF;
            font: normal 12px/22px Arial, sans-serif;
            left: 0;
            overflow: hidden;
            position: absolute;
            text-indent: 10px;
            width: 100%;
            z-index: 10000;
        }
        .slideshow-captions-hidden {
            height: 0;
            opacity: 0;
        }
        .slideshow-captions-visible {
            height: 22px;
            opacity: .7;
        }
        .slideshow-controller {
            background: url(/httpd/img/slideshow/controller.png) no-repeat;
            height: 42px;
            left: 60%;
            margin: -21px 0 0 -119px;
            overflow: hidden;
            position: absolute;
            top: 60%;
            width: 238px;
            z-index: 10000;
        }
        .slideshow-controller * {
            margin: 0;
            padding: 0;
        }
        .slideshow-controller-hidden { 
            opacity: 0;
        }
        .slideshow-controller-visible {
            opacity: 1;
        }
        .slideshow-controller a {
            cursor: pointer;
            display: block;
            height: 18px;
            overflow: hidden;
            position: absolute;
            top: 12px;
        }
        .slideshow-controller a.active {
            background-position: 0 18px;
        }
        .slideshow-controller li {
            list-style: none;
        }
        .slideshow-controller li.first a {
            background-image: url(/httpd/img/slideshow/controller-first.gif);
            left: 33px;
            width: 19px;
        }
        .slideshow-controller li.last a {
            background-image: url(/httpd/img/slideshow/controller-last.gif);
            left: 186px;
            width: 19px;
        }
        .slideshow-controller li.next a {
            background-image: url(/httpd/img/slideshow/controller-next.gif);
            left: 145px;
            width: 28px;
        }
        .slideshow-controller li.pause a {
            background-image: url(/httpd/img/slideshow/controller-pause.gif);
            left: 109px;
            width: 20px;
        }
        .slideshow-controller li.play a {
            background-position: 20px 0;
        }
        .slideshow-controller li.play a.active {
            background-position: 20px 18px;
        }
        .slideshow-controller li.prev a {
            background-image: url(/httpd/img/slideshow/controller-prev.gif);
            left: 65px;
            width: 28px;
        }
        .slideshow-loader {
            height: 28px;
            right: 0;
            position: absolute;
            top: 0;
            width: 28px;
            z-index: 10001;
        }
        .slideshow-loader-hidden {
            opacity: 0;
        }
        .slideshow-loader-visible {
            opacity: 1;
        }
        .slideshow-thumbnails {
            bottom: -65px;
            height: 65px;
            left: 0;
            position: absolute;
            width: 100%;
        }
        .slideshow-thumbnails * {
            margin: 0;
            padding: 0;
        }
        .slideshow-thumbnails ul {
            height: 65px;
            left: 0;
            position: absolute;
            top: 0;
            width: 10000px;
        }
        .slideshow-thumbnails li {
            float: left;
            list-style: none;
            margin: 5px 5px 5px 0;
            position: relative;
        }
        .slideshow-thumbnails a {
            display: block;
            float: left;
            padding: 5px;
            position: relative; 
        }
        .slideshow-thumbnails a:hover {
            background-color: #FF9 !important;
            opacity: 1 !important;
        }
        .slideshow-thumbnails img {
            display: block;
        }
        .slideshow-thumbnails-active {
            background-color: #9FF;
            opacity: 1;
        }
        .slideshow-thumbnails-inactive {
            background-color: #FFF;
            opacity: .5;
        }
        
    </xsl:template>
    
</xsl:stylesheet>