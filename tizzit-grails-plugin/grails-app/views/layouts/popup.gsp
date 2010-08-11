<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<title><g:layoutTitle default="Tizzit Content Page"/></title>
	<meta name="keywords" content=""/>
	<meta name="description" content=""/>
	<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
	<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
	<g:layoutHead/>
	<g:javascript library="application"/>
</head>
<body>
<div id="spinner" class="spinner" style="display:none;">
	<img src="${resource(dir: 'images', file: 'spinner.gif')}" alt="${message(code: 'spinner.alt', default: 'Loading...')}"/>
</div>
<div id="logo">
	<h1><a href="#">Tizzit Default Page</a></h1>
</div>
<hr/>
<!-- end #logo -->
<div id="header">
	<div id="menu">
		<tizzit:navigation since="root" depth="1" omitFirst="true" showType="1"/>
	</div>
	<!-- end #menu -->
	<div id="search">
		<form method="get" action="">
			<fieldset>
				<input type="text" name="s" id="search-text" size="15" value=""/>
				<input type="submit" id="search-submit" value="GO"/>
			</fieldset>
		</form>
	</div>
	<!-- end #search -->
</div>
<!-- end #header -->
<!-- end #header-wrapper -->
<div id="page">
	<div id="content">
		<div class="post">
			<g:layoutBody/>
		</div>
	</div><!-- end #content -->
	<div id="sidebar">
		<tizzit:navigation since="me" depth="2" omitFirst="true"/>
	</div>
	<!-- end #sidebar -->
	<div style="clear: both;">&nbsp;</div>
</div>
<!-- end #page -->
<div id="footer">
	<tizzit:navigation since="root" depth="1" omitFirst="true" showType="2"/>
</div>
<!-- end #footer -->
</body>
</html>
