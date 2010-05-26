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
			font-family:Trebuchet MS, Arial;
			color:#254559;
		}
		h2 {
			font-size:18px;
			color:#254559;
			padding-bottom:4px;
			font-family:Trebuchet MS, Arial;
		}
		a {
			color: #193140;
			text-decoration:underline;
		}
		a:hover {
			color:#96CF48;
		}
		ul {
			padding-left:15px;
		}
		p {
			margin-bottom:5px;
		}
		img {
			border:none;
		}
		hr {
			margin:20px 0;
			border: 0px;
			background-image:url('/httpd/img/hr_bg.gif');
			background-repeat: repeat-x;
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
			<!--filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/httpd/img/white_wave.png',sizingMethod='crop');
			_background:none;-->
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
			background-color:#193140;
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
			top:0px;
			z-index:255;
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
			padding-right:25px!important;
			padding-right:12px;
		}
		#leftmenue_top {
			background-image:url('/httpd/img/navi/leftnavi_top.gif');
			background-repeat:no-repeat;
			height:9px;
			line-height:0px;
		}
		#leftmenue_headline {
			background-image:url('/httpd/img/navi/leftnavi_seperator.gif');
			background-repeat:no-repeat;
			background-position:0 bottom;
			padding:0 0 7px 25px;
			font-size:14px;
			font-weight:bold;
			font-family:Trebuchet MS, Arial;
		}
		#leftmenue_middle {
			background-image:url('/httpd/img/navi/leftnavi_middle.gif');
			background-repeat:repeat-y;
			padding:10px 13px 10px 3px;
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
		#sn_footer {
			float:right;
		}
		.content {
			line-height:18px;
			font-size:13px;
			font-family:Tahoma;
		}
		.secondlevel, #search_area, #search_button, #search_container, .copyright,
		.firstlevel2 .firstlink, .firstlevel2 .last-of-firstlink, .secondlevel .firstlink, .secondlevel .first-of-firstlink,
		.fl_bg_l, .fl_bg, .flm_bg, #leftmenue, #right, #middle, .footer_item, .first-item, .breadcrumb-item,
		.languageFlags {
			float:left;
		}
		.footer_item {
			padding-left:15px;
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
			margin-right:13px;
		}
		.firstlevel2 .firstlink .link, .firstlevel2 .last-of-firstlink .link,
		.firstlevel2 .firstlink .clicked, .firstlevel2 .last-of-firstlink .clicked {
			color: #494949;
			text-decoration:none;
			font-size:17px;
			display:block;
			padding:5px 8px 16px 8px;
			font-weight:bold;
			cursor:default;
		}
		.firstlevel2 .secondlink a, .firstlevel2 .last-of-secondlink a {
			color: #fff;
			text-decoration:none;
			font-size:14px;
			display:block;
			padding:5px 8px 5px 13px;
			font-weight:bold;
		}
		.firstlevel2 .firstlink .fl_bg_l, .firstlevel2 .last-of-firstlink .fl_bg_l {
			background-image:url('/httpd/img/navi/nav_bg_left.gif');
			background-repeat:no-repeat;
			width:4px;
		}
		.firstlevel2 .firstlink .fl_bg, .firstlevel2 .last-of-firstlink .fl_bg {
			background-image:url('/httpd/img/navi/nav_bg_right.gif');
			background-repeat:no-repeat;
			width:4px;
		}
		.firstlevel2 .firstlink .flm_bg, .firstlevel2 .last-of-firstlink .flm_bg {
			background-image:url('/httpd/img/navi/nav_bg_middle.gif');
			background-repeat:repeat-x;
		}
		.firstlevel2 .firstlink .actualClicked, .firstlevel2 .firstlink .clicked,
		.firstlevel2 .last-of-firstlink .actualClicked, .firstlevel2 .last-of-firstlink .clicked {
			background-image:url('/httpd/img/navi/nav_bg_bottom.gif');
			background-repeat:no-repeat;
			background-position:center 29px;
			color:#fff;
		}
		.firstlevel2 .firstlink a:hover, .firstlevel2 .last-of-firstlink a:hover,
		.firstlevel2 .firstlink .clicked a:hover, .firstlevel2 .last-of-firstlink .clicked a:hover,
		.firstlevel2 .secondlink .actualClicked a, .firstlevel2 .secondlink .clicked a,
		.firstlevel2 .last-of-secondlink .actualClicked a, .firstlevel2 .last-of-secondlink .clicked a {
			color:#96CF48;
		}
		.firstlevel2 .secondlink .actualClicked a, .firstlevel2 .secondlink .clicked a,
		.firstlevel2 .last-of-secondlink .actualClicked a, .firstlevel2 .last-of-secondlink .clicked a {
			background-image:none;
		}
		.leftmenue .firstlink a {
			font-size:13px;
			text-decoration:none;
			font-weight:bold;
			background-image:url('/httpd/img/navi/leftnavi_seperator.gif');
			background-repeat:no-repeat;
			background-position:0 bottom;
			display:block;
			padding:3px 10px 7px 35px;
		}
		.leftmenue .firstlink a:hover, .leftmenue .firstlink .clicked a {
			font-weight:bold;
			color:#96CF48;
		}
		.leftmenue .thirdlinks a {
			padding:3px 10px 3px 48px;
			background-image:url('/httpd/img/navi/leftnavi_bg.gif');
			background-repeat:no-repeat;
			background-position:36px 8px;
			font-weight:normal;
		}
		#right .teaseraggregation {
			padding-left:40px;
		}
		#right .teaseritem {
			border:7px solid #eceef0;
			margin-bottom:15px;
			padding:9px;
		}
		#right h1.teaserheadline {
			font-size:16px;
			color:#254559;
			font-weight:bold;
			padding-bottom:5px;
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
			width:445px;
			float:left;
		}
		#information_2 .right {
			width:430px;
			float:left;
			padding-left:22px;
		}
		#information_2 h1.teaserheadline {
			font-weight:normal;
			color:#fff;
			padding-bottom:15px;
		}
		#information_2 .teaser_content {
			font-size:13px;
			color:#474747;
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
		<!--
			mainmenu
		-->
		.sf-menu, .sf-menu * {
			margin:0;
			padding:0;
			list-style:none;
			z-index:255;
		}
		.sf-menu {
			line-height:1.0;
		}
		.sf-menu ul {
			position:absolute;
			top:-999em;
			width:162px; /* left offset of submenus need to match (see below) */
		}
		.sf-menu ul li {
			width:100%;
		}
		.sf-menu li:hover {
			visibility:	inherit; /* fixes IE7 'sticky bug' */
		}
		.sf-menu li {
			float:left;
			position:relative;
		}
		.sf-menu a {
			display:block;
			position:relative;
		}
		.sf-menu li:hover ul,
		.sf-menu li.sfHover ul {
			left:0;
			top:2.5em; /* match top ul list item height */
			z-index:255;
		}
		ul.sf-menu li:hover li ul,
		ul.sf-menu li.sfHover li ul {
			top:-999em;
		}
		ul.sf-menu li li:hover ul,
		ul.sf-menu li li.sfHover ul {
			left:10em; /* match ul width */
			top:0;
		}
		ul.sf-menu li li:hover li ul,
		ul.sf-menu li li.sfHover li ul {
			top:-999em;
		}
		ul.sf-menu li li li:hover ul,
		ul.sf-menu li li li.sfHover ul {
			left:10em; /* match ul width */
			top:0;
		}
		.sf-menu {
			float:left;
			margin-bottom:1em;
		}
		.sf-menu a {
			<!--padding:.75em 1em;-->
			text-decoration:none;
		}
		.secondlinks {
			background-image:url('/httpd/img/navi/secondlinks_bg_top.gif');
			background-repeat:no-repeat;
			padding:3px 0 0 0;
			background-color:transparent;
		}
		.last-item {
			background-image:url('/httpd/img/navi/secondlinks_bg_bottom.png');
			background-repeat:no-repeat;
			filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/httpd/img/navi/secondlinks_bg_bottom.png',sizingMethod='crop');
			_background:none;
			line-height:0px;
			font-size:0px;
			height:4px;
			clear:both;
		}
		<!--.sf-menu a, .sf-menu a:visited  { /* visited pseudo selector so IE6 applies text colour*/
			color:#13a;
		}
		.sf-menu li {
			background:		#BDD2FF;
		}-->
		.sf-menu li li {
			background:#254559;
		}
		<!--.sf-menu li li li {
			background:		#9AAEDB;
		}-->
		.sf-menu li:hover, .sf-menu li.sfHover,
		.sf-menu a:focus, .sf-menu a:hover, .sf-menu a:active {
			<!--background:		#CFDEFF;-->
			outline:0;
		}
		<!--/*** arrows **/
		.sf-menu a.sf-with-ul {
			padding-right: 	2.25em;
			min-width:		1px; /* trigger IE7 hasLayout so spans position accurately */
		}
		.sf-sub-indicator {
			position:		absolute;
			display:		block;
			right:			.75em;
			top:			1.05em; /* IE6 only */
			width:			10px;
			height:			10px;
			text-indent: 	-999em;
			overflow:		hidden;
			<!-\-background:		url('../images/arrows-ffffff.png') no-repeat -10px -100px; /* 8-bit indexed alpha png. IE6 gets solid image only */-\->
		}
		a > .sf-sub-indicator {  /* give all except IE6 the correct values */
			top:			.8em;
			background-position: 0 -100px; /* use translucent arrow for modern browsers*/
		}-->
		<!--/* apply hovers to modern browsers */
			a:focus > .sf-sub-indicator,
			a:hover > .sf-sub-indicator,
			a:active > .sf-sub-indicator,
			li:hover > a > .sf-sub-indicator,
			li.sfHover > a > .sf-sub-indicator {
				background-position: -10px -100px; /* arrow hovers for modern browsers*/
		}
		/* point right for anchors in subs */
			.sf-menu ul .sf-sub-indicator { background-position:  -10px 0; }
			.sf-menu ul a > .sf-sub-indicator { background-position:  0 0; }
			/* apply hovers to modern browsers */
			.sf-menu ul a:focus > .sf-sub-indicator,
			.sf-menu ul a:hover > .sf-sub-indicator,
			.sf-menu ul a:active > .sf-sub-indicator,
			.sf-menu ul li:hover > a > .sf-sub-indicator,
			.sf-menu ul li.sfHover > a > .sf-sub-indicator {
				background-position: -10px 0; /* arrow hovers for modern browsers*/
		}
		/*** shadows for all but IE6 ***/
			.sf-shadow ul {
			background:	url('/httpd/img/navi/shadow.png') no-repeat bottom right;
			padding: 0 8px 9px 0;
			-moz-border-radius-bottomleft: 17px;
			-moz-border-radius-topright: 17px;
			-webkit-border-top-right-radius: 17px;
			-webkit-border-bottom-left-radius: 17px;
		}
		.sf-shadow ul.sf-shadow-off {
			background: transparent;
			}-->
		
		<!--footer-->
		#sn_footer li	{
			position:relative;
			overflow:hidden;
		}
		#sn_footer a {
			text-decoration: none;
			outline: none;
			color:#fff;
			display: block;
			width: 24px;
			cursor:pointer;
		}
		#sn_footer span {
			width: 120px;
			height: 35px;
			position: absolute;
			display: none;
			color:#fff;
			padding-left: 5px;
		}
		<!--FancyBox-->
		div#fancy_overlay {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background-color: #666;
			display: none;
			z-index: 30;
		}
		* html div#fancy_overlay {
			position: absolute;
			height: expression(document.body.scrollHeight > document.body.offsetHeight ? document.body.scrollHeight : document.body.offsetHeight + 'px');
		}
		div#fancy_wrap {
			text-align: left;
		}
		div#fancy_loading {
			position: absolute;
			height: 40px;
			width: 40px;
			cursor: pointer;
			display: none;
			overflow: hidden;
			background: transparent;
			z-index: 100;
		}
		div#fancy_loading div {
			position: absolute;
			top: 0;
			left: 0;
			width: 40px;
			height: 480px;
			background: transparent url('/httpd/img/fancyBox/fancy_progress.png') no-repeat;
		}
		div#fancy_loading_overlay {
			position: absolute;
			background-color: #FFF;
			z-index: 30;
		}
		div#fancy_loading_icon {
			position: absolute;
			background: url('/httpd/img/fancyBox/fancy_loading.gif') no-repeat;
			z-index: 35;
			width: 16px;
			height: 16px;
		}
		div#fancy_outer {
			position: absolute;
		    top: 0;
		    left: 0;
		    z-index: 90;
		    padding: 18px 18px 33px 18px;
		    margin: 0;
		    overflow: hidden;
		    background: transparent;
		    display: none;
		}
		div#fancy_inner {
			position: relative;
			width:100%;
			height:100%;
			border: 1px solid #BBB;
			background: #FFF;
		}
		div#fancy_content {
			margin: 0;
			z-index: 100;
			position: absolute;
		}
		div#fancy_div {
			background: #000;
			color: #FFF;
			height: 100%;
			width: 100%;
			z-index: 100;
		}
		img#fancy_img {
			position: absolute;
			top: 0;
			left: 0;
			border:0; 
			padding: 0; 
			margin: 0;
			z-index: 100;
			width: 100%;
			height: 100%;
		}
		div#fancy_close {
			position: absolute;
			top: -12px;
			right: -15px;
			height: 30px;
			width: 30px;
			background: url('/httpd/img/fancyBox/fancy_closebox.png') top left no-repeat;
			cursor: pointer;
			z-index: 181;
			display: none;
		}
		#fancy_frame {
			position: relative;
			width: 100%;
			height: 100%;
			display: none;
		}
		#fancy_ajax {
			width: 100%;
			height: 100%;
			overflow: auto;
		}
		a#fancy_left, a#fancy_right {
			position: absolute; 
			bottom: 0px; 
			height: 100%; 
			width: 35%; 
			cursor: pointer;
			z-index: 111; 
			display: none;
			background-image: url(data:image/gif;base64,AAAA);
			outline: none;
		}
		a#fancy_left {
			left: 0px; 
		}
		a#fancy_right {
			right: 0px; 
		}
		span.fancy_ico {
			position: absolute; 
			top: 50%;
			margin-top: -15px;
			width: 30px;
			height: 30px;
			z-index: 112; 
			cursor: pointer;
			display: block;
		}
		span#fancy_left_ico {
			left: -9999px;
			background: transparent url('/httpd/img/fancyBox/fancy_left.png') no-repeat;
		}
		span#fancy_right_ico {
			right: -9999px;
			background: transparent url('/httpd/img/fancyBox/fancy_right.png') no-repeat;
		}
		a#fancy_left:hover {
		  visibility: visible;
		}
		a#fancy_right:hover {
		  visibility: visible;
		}
		a#fancy_left:hover span {
			left: 20px; 
		}
		a#fancy_right:hover span {
			right: 20px; 
		}
		.fancy_bigIframe {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background: transparent;
		}
		div#fancy_bg {
			position: absolute;
			top: 0; left: 0;
			width: 100%;
			height: 100%;
			z-index: 70;
			border: 0;
			padding: 0;
			margin: 0;
		}
		div.fancy_bg {
			position: absolute;
			display: block;
			z-index: 70;
			border: 0;
			padding: 0;
			margin: 0;
		}
		div.fancy_bg_n {
			top: -18px;
			width: 100%;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_n.png') repeat-x;
		}
		div.fancy_bg_ne {
			top: -18px;
			right: -13px;
			width: 13px;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_ne.png') no-repeat;
		}
		div.fancy_bg_e {
			right: -13px;
			height: 100%;
			width: 13px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_e.png') repeat-y;
		}
		div.fancy_bg_se {
			bottom: -18px;
			right: -13px;
			width: 13px;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_se.png') no-repeat;
		}
		div.fancy_bg_s {
			bottom: -18px;
			width: 100%;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_s.png') repeat-x;
		}
		div.fancy_bg_sw {
			bottom: -18px;
			left: -13px;
			width: 13px;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_sw.png') no-repeat;
		}
		div.fancy_bg_w {
			left: -13px;
			height: 100%;
			width: 13px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_w.png') repeat-y;
		}
		div.fancy_bg_nw {
			top: -18px;
			left: -13px;
			width: 13px;
			height: 18px;
			background: transparent url('/httpd/img/fancyBox/fancy_shadow_nw.png') no-repeat;
		}
		div#fancy_title {
			position: absolute;
			bottom: -33px;
			left: 0;
			width: 100%;
			z-index: 100;
			display: none;
		}
		div#fancy_title div {
			color: #FFF;
			font: bold 12px Arial;
			padding-bottom: 3px;
		}
		div#fancy_title table {
			margin: 0 auto;
		}
		div#fancy_title table td {
			padding: 0;
			vertical-align: middle;
		}
		td#fancy_title_left {
			height: 32px;
			width: 15px;
			background: transparent url(/httpd/img/fancyBox/fancy_title_left.png) repeat-x;
		}
		td#fancy_title_main {
			height: 32px;
			background: transparent url(/httpd/img/fancyBox/fancy_title_main.png) repeat-x;
		}
		td#fancy_title_right {
			height: 32px;
			width: 15px;
			background: transparent url(/httpd/img/fancyBox/fancy_title_right.png) repeat-x;
		}

		<xsl:if test="$media = 'print'"></xsl:if>

	</xsl:template>
	
</xsl:stylesheet>