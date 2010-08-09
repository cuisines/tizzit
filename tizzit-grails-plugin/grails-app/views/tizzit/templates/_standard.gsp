<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
	<title><tizzit:content node="//title" omitFirst="true"/></title>
</head>
<body>
<tizzit:content node="//content" omitFirst="true" moduleTemplates="${[picture:"picture2"]}"/>

</body>
</html>
