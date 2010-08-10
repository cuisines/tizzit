<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
	<title><tizzit:content node="//title" omitFirst="true"/></title>
</head>
<body>
<h2>Standard Content Template</h2>
<tizzit:content node="/root/content/source/all/content" omitFirst="true" moduleTemplates="[picture:'picture']"/>

</body>
</html>
