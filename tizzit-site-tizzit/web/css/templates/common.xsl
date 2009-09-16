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
		}
		html, body {
			width: 100%;
			height: 100%;
			color: #193140;
			font-family: Arial;
			font-size: 12px;
		}
		
		a {
			color: #193140;
			text-decoration:none;
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
		
		h1 {
			font-size:24px;
			padding-bottom:20px;
		}
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
		#contentSeperator {
			background-image:url('/httpd/img/contentSeperator.gif');
			background-repeat: repeat-x;
			background-position: 0 193px;
		}
		#information_1 {
			<!--height: 320px;-->
		}
		#information_2 {
			background-image:url('/httpd/img/informations_repeat.jpg');
			background-repeat: repeat-x;
			background-position: 0 top;
			background-color:#aeb9bf;
			<!--height: 410px;-->
		}
		#footer {
			background-image:url('/httpd/img/footer_repeat.jpg');
			background-repeat: repeat-x;
			background-position: 0 bottom;
			height:60px;
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
			margin-top: 37px!important;
			margin-top: 10px;
			padding:30px 0px 30px 25px;
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
		#f_content {
			color: #aeb9bf;
			font-size:11px;
			margin:0 auto;
			font-family:Tahoma;
			width:955px;
			padding:17px 15px 0 0;
		}
		#leftmenue {
			width:255px;
			padding-right:25px;
		}
		#leftmenue_top {
			background-image:url('/httpd/img/navi/leftnavi_top.gif');
			background-repeat:no-repeat;
			height:9px;
			line-height:0px;
		}
		#leftmenue_middle {
			background-image:url('/httpd/img/navi/leftnavi_middle.gif');
			background-repeat:repeat-y;
			padding:20px 13px;
		}
		#leftmenue_bottom {
			background-image:url('/httpd/img/navi/leftnavi_bottom.gif');
			background-repeat:no-repeat;
			height:10px;
			line-height:0px;
		}
		#right {
			width:220px;
			padding-top:17px;
		}
		#middle {
			width:455px;
			padding-top:11px;
		}
		#tagCloud {
			position:absolute;
			left:30px;
		}
		.twitter {
			float:right;
		}
		.content {
			line-height:18px;
			font-size:13px;
		}
		.secondlevel, #search_area, #search_button, #search_container, .copyright,
		.firstlevel2 .firstlink, .secondlevel .firstlink, .secondlevel .first-of-firstlink,
		.fl_bg_l, .fl_bg, .flm_bg, #leftmenue, #right, #middle, .footer_item, .first-item, .breadcrumb-item,
		.languageFlags {
			float:left;
		}
		.footer_item {
			padding-left:25px;
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
			padding:5px 8px 13px 8px;
		}
		.firstlevel2 .firstlink .fl_bg_l {
			background-image:url('/httpd/img/navi/nav_bg_left.gif');
			background-repeat:no-repeat;
			width:4px;
		}
		.firstlevel2 .firstlink .fl_bg {
			background-image:url('/httpd/img/navi/nav_bg_right.gif');
			background-repeat:no-repeat;
			width:4px;
		}
		.firstlevel2 .firstlink .flm_bg {
			background-image:url('/httpd/img/navi/nav_bg_middle.gif');
			background-repeat:repeat-x;
		}
		.firstlevel2 .firstlink .actualClicked a, .firstlevel2 .firstlink .clicked a {
			background-image:url('/httpd/img/navi/nav_bg_bottom.gif');
			background-repeat:no-repeat;
			background-position:center 29px;
			color:#fff;
		}
		.firstlevel2 .firstlink a:hover {
			color:#96cf48;
		}
		.leftmenue .firstlink a {
			font-size:13px;
			text-decoration:none;
			font-weight:bold;
			background-image:url('/httpd/img/navi/leftnavi_seperator.gif');
			background-repeat:no-repeat;
			background-position:0 bottom;
			display:block;
			padding:3px 0 7px 17px;
		}
		.leftmenue .firstlink a:hover, .leftmenue .firstlink .clicked a {
			font-weight:bold;
		}
		.leftmenue .thirdlinks a {
			padding:3px 0 3px 32px;
			background-image:url('/httpd/img/navi/leftnavi_bg.gif');
			background-repeat:no-repeat;
			background-position:17px 8px;
			font-weight:normal;
		}
		#right .teaseraggregation {
			padding-left:40px;
		}
		#right .teaseritem {
			<!--border:7px solid #eceef0;-->
			margin-bottom:10px;
		}
		#right .teaserheadline {
			font-size:16px;
			color:#254559;
			font-weight:bold;
		}
		#right .teaser_content {
			font-size:13px;
		}
		#information_2 .teaseraggregation {
			width:955px;
			padding:40px 15px 40px 0;
			margin:0 auto;
		}
		#information_2 .teaseritem {
			width:460px;
			float:left;
			padding-right:10px;
		}
		#information_2 h1.teaserheadline {
			font-size:24px;
			color:#fff;
		}
		#information_2 h2.subline {
			font-size:18px;
			color:#254559;
		}
		#information_2 .teaser_content {
			font-size:13px;
			color:#474747;
			padding-top:10px;
		}
		#information_2 .teaserimage {
			float:left;
			padding:0 10px 10px 0;
		}
		#breadcrumbs {
			position:absolute;
			top:199px;
			left:306px;
		}
		#breadcrumbs, #breadcrumbs a {
			color:#fff;
			font-size:11px;
		}
		#breadcrumbs a:hover {
			text-decoration:underline;
		}
		.breadcrumb-item {
			padding-left:23px;
			background-image:url('/httpd/img/navi/breadcrumb_bg.gif');
			background-repeat:no-repeat;
			background-position:8px 5px;
		}
		.languageFlags {
			padding:1px 0 0 10px;
			border-left:1px solid #A0A0A0;
			margin:5px 0 0 10px;
			height:13px;
		}
		.languageFlags a {
		}

		<xsl:if test="$media = 'print'"></xsl:if>

	</xsl:template>
	
</xsl:stylesheet>