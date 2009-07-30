<html>
<head>
<title>ConQuest Content Management System</title>
<script type="text/javascript">
var javawsInstalled = false;
var isIE = false;
var agt=navigator.userAgent.toLowerCase();
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
<body onLoad="MM_preloadImages('splash/btn_sel.gif')">
	
<SCRIPT LANGUAGE="JavaScript">
if (!javawsInstalled) {
	if(isIE) {
		location.replace("ws_dl_ie.jsp");
	}else{
		location.replace("ws_dl_ns.jsp");
	}
} 
</SCRIPT>	
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
        <table width="450" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td><img src="cms_450x121.png"></td>
          </tr>
          <tr>
            <td align="center"><div style="font-family: sans-serif; font-size: large"> <a href="webstart/app.jnlp" onMouseOut="MM_swapImgRestore()" type="application/x-java-jnlp-file">Start Adminclient</a></div></td>
          </tr>
        </table>
        </td>
  </tr>
</table>
</body>
</html>