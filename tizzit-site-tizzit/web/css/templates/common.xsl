<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="text" indent="no"/>

	<xsl:include href="../../../../tizzit-site-master/web/css/templates/common.xsl"/>
	
	<xsl:param name="media" select="'default'"/>		
	
	<xsl:template match="/">		
		<!-- create generic styles for all conQuest Mandants -->				
		<!--<xsl:apply-templates select="styles" mode="reset"/>-->

		<!-- create generic styles for this mandant -->
		<xsl:apply-templates select="styles"/>

		<!-- create template specific -->
		<xsl:apply-templates select="styles" mode="template"/>	
	</xsl:template>
	
	<!-- Templates mit leerausgabe, koennen ggfs. ueberschrieben werden -->
	<xsl:template match="styles" mode="template" priority="-1"/>
	<xsl:template match="styles" mode="reset" priority="-1"/>

	<xsl:template match="styles">
		
		<!-- Globale Matcher -->
		
		* {
			padding: 0;
			margin: 0;
			font-family: Arial;
			font-size: 12px;
		}
		html, body {
			width: 100%;
			height: 100%;
		}
		
		ul {
			padding-left:15px;
		}
		
		<!-- Clear Hack to Remove Floating -->
		
		.clear {
			width: 0px;
			height: 0px;
			padding: 0px 0px 0px 0px;
			margin: 0px 0px 0px 0px;
			font-size: 0px;
			line-height: 0px;
			clear: both;
		}
		
		<!-- info box layouts -->
		
		.box-green {
			padding: 7px 20px;
			margin: 0px 0px 10px 0px;
			background-color: #F4F8F4;
			border-color: #AACCAA -moz-use-text-color;
			border-style: dotted none;
			border-width: 1px 0px;
			color: #335533;
		}
		.box-yellow {
			padding: 7px 20px;
			margin: 0px 0px 10px 0px;
			background-color: #FFFFEE;
			border-color: #888844 -moz-use-text-color;
			border-style: dotted none;
			border-width: 1px 0px;
			color: #444400;
		}
		.box-red {
			padding: 7px 20px 7px 55px;
			margin: 0px 0px 10px 0px;
			background-image: url('/httpd/img/icon_attention.jpg');
			background-repeat: no-repeat;
			background-position: 3px 6px;
			background-color: #FFF5F5;
			border-color: #FFCCCC -moz-use-text-color;
			border-style: dotted none;
			border-width: 1px 0px;
			color: #AA1124;
		}
		
		<!-- Custom CSS below -->
		
		#layout {
			width:1024px;
			margin:0 auto;
			position:relative;
		}
		#cloudsrepeat {
			background-image:url('/httpd/img/sky_repeat.png');
			background-repeat: repeat-x;
		}
		#wavesrepeat {
			background-image:url('/httpd/img/waterwaves.png');
			background-repeat: repeat-x;
			background-position: 0 158px;
		}
		#whitewaverepeat {
			background-image:url('/httpd/img/white_wave.png');
			background-repeat: repeat-x;
			background-position: 0 188px;
		}
		#underwaterrepeat {
			background-image:url('/httpd/img/underwater_repeat.png');
			background-repeat: repeat-x;
			background-position: 0 195px;
		}
		#header {
			background-image:url('/httpd/img/header_logo.jpg');
			background-repeat:no-repeat;
			width:986px;
			height:158px;
			position:relative;
			padding:0 38px 0 0;
		}
		#waterwaves {
			width:1024px;
			height:34px;
		}
		#contentcontainer {
			width:994px;
			height:375px;
			margin-top: 37px!important;
			margin-top: 10px;
			padding:0 30px 0 0;
		}
		#toparea {
			float:right;
		}
		#search_container {
			margin-left: 10px;
		}
		#searchValue {
			background-image: url('/httpd/img/search_lupe.gif');
			background-repeat: no-repeat;
			background-position: 4px 4px;
			height:16px;
			border:1px solid #a0a0a0;
			padding:4px 0 0 27px!important;
			padding:0px 0 4px 27px;
			line-height:20px;
			width:156px;
		}
		#toparea {
			margin-top:27px;
		}
		#search_area {
			padding-left:44px;
		}
		#homelink {
			display:block;
			height:132px;
			left:27px;
			position:absolute;
			width:246px;
			line-height:0px;
			color:#fff;
			font-size:0px;
		}
		.secondlevel, #search_area, #search_button, #search_container {
			float:left;
		}
		.firstlevel2 {
			margin: 49px 0 0 302px;
		}
		.secondlevel {
			margin-top:5px;
		}
		.secondlevel .firstlink a, .secondlevel .first-of-firstlink a {
			color: #494949;
			text-decoration:none;
			padding:0 0 0 10px;
			margin-left:10px;
			border-left:1px solid #a0a0a0;
		}
		.secondlevel .first-of-firstlink a {
			border-left:0px solid #a0a0a0;
		}
		.secondlevel .firstlink a:hover, .secondlevel .first-of-firstlink a:hover {
			text-decoration:underline;
		}
		.firstlevel2 .firstlink, .secondlevel .firstlink, .secondlevel .first-of-firstlink,
		.fl_bg_l, .fl_bg, .flm_bg {
			float:left;
		}
		.fl_bg_l, .fl_bg {
			height:29px;
		}
		.firstlevel2 .firstlink{
			margin-right:14px;
		}
		.firstlevel2 .firstlink a {
			color: #494949;
			text-decoration:none;
			font-size:17px;
			display:block;
			padding:5px 8px 12px 8px;
		}
		.firstlevel2 .firstlink .fl_bg_l {
			background-image:url('/httpd/img/nav_bg_left.gif');
			background-repeat:no-repeat;
		}
		.firstlevel2 .firstlink .fl_bg {
			background-image:url('/httpd/img/nav_bg_right.gif');
			background-repeat:no-repeat;
		}
		.firstlevel2 .firstlink .flm_bg {
			background-image:url('/httpd/img/nav_bg_middle.gif');
			background-repeat:repeat-x;
		}
		.firstlevel2 .firstlink .actualClicked a {
			background-image:url('/httpd/img/nav_bg_bottom.gif');
			background-repeat:no-repeat;
			background-position:center 29px;
			color:#fff;
		}
		.firstlevel2 .firstlink a:hover {
			color:#fff;
		}
		

		<xsl:if test="$media = 'print'"></xsl:if>

	</xsl:template>
	
</xsl:stylesheet>