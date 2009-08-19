<html>
<head>
<title>Tizzit Content Management System</title>
<link rel="icon" href="favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<style type="text/css">

body {
	text-align: center;
}
#layout {
	text-align:center;
	position:relative;
	padding-top:50px;
	width: 547px;
	margin: 0 auto;
}

#buttonArea {
	position:absolute;
	top:240px;
	left:235px;
	bottom:0px;
	right:0px;	
}

a.webStartLink:link, a.webStartLink:visited,a.webStartLink:active {
	background-image: url('splash/btn_start_tizzit.gif');	
	background-position: 0px 0px; 
	height: 29px;
	width: 263px;
	color: #fafbff;
	text-decoration: none;
	text-indent:50px;
	line-height:29px;
	display:block;
	font-family: Tahoma;
	font-size: 14px;
	font-weight: bold;
	}

a.webStartLink:hover{
	background-image: url('splash/btn_start_tizzit.gif') ;
	background-position: 0px 29px;
	display:block;
	height: 29px;
	width: 263px;
	color: #fafbff;
	text-decoration: none;
	text-indent:50px;
	line-height:29px;
	font-family: Tahoma;
	font-size: 14px;
	font-weight: bold;
 }
 
</style>
<script type="text/javascript">
var javaVersion = "1.5"
var javawsInstalled = false;
var javaMimeType = "application/x-java-applet;version="+javaVersion;
var javaWsMimeType = 'application/x-java-jnlp-file';
var isWebStart = false;
var isJava = false;
var isIE = false;
var agt=navigator.userAgent.toLowerCase();
var isFirefox = (agt.indexOf("firefox")!=-1);
var isWindows = (agt.indexOf("windows")!=-1);

var is_mac = (agt.indexOf("mac")!=-1);
var isICE = navigator.userAgent.indexOf("ICEBrowser") >= 0;
if (navigator.mimeTypes && navigator.mimeTypes.length) {
  javawsInstalled = navigator.mimeTypes['application/x-java-jnlp-file'] || is_mac;
} else {
  isIE = true;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
</script>
<script type="text/vbscript">
<!--
on error resume next
If isIE Then
  If Not(IsObject(CreateObject("JavaWebStart.IsInstalled"))) Then
    javawsInstalled = false
  Else
    javawsInstalled = true
  End If
End If
//-->
</script>
</head>
<body>
	<script language="JavaScript">
	if (!javawsInstalled) {
		if(isIE && isWindows) {
			location.replace("ws_dl_ie.jsp");
		}else{

			if ((isFirefox && isJava) || (!isFirefox && (sWebStart || isJava))) {
				location.replace("ws_dl_ns.jsp");
			} else {
				if(isFirefox) {
					location.replace("http://www.java.com");
					document.write("No Java found - ");
				    document.write("Click ");
				    location.replace("http://www.java.com");
				    //document.write("http://java.sun.com/PluginBrowserCheck?pass="+site+"&fail=http://www.java.com");
				    //location.replace("http://java.sun.com/PluginBrowserCheck?pass="+site+"&fail=http://www.java.com");
				    document.write("<a http://www.java.com>here</a> ");
				    document.write("to download and install JRE 6.0 and the application.");
				} else {
					location.replace("ws_dl_rest.jsp");
				}
			}
		}
	}

	</script>

	<!-- Aufbau -->
	<div id="layout">
		
		<img src="splash/page_animation_tizzit.gif">
		<div id="buttonArea">
			<a href="juwimm-cms-client.jnlp"  type="application/x-java-jnlp-file" class="webStartLink">Internetseiten bearbeiten				
			</a>
		</div>
	</div>
</body>
</html>