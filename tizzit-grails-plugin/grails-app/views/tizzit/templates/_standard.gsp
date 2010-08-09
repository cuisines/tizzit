<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
	<title>Simple GSP page</title></head>
<body>
Standard Template
Tizzit Navi:
<tizzit:navigation since="root" depth="3"/>

</body>
</html>