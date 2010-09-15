<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<title><tizzit:content node="//title" omitFirst="true"/></title>
		<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
		<meta name="viewComponentId" content="${params.tizzit.viewComponentId}"/>
	</head>
	<body>
		<h1><tizzit:content node="//title" omitFirst="true"/></h1>
		<tizzit:content node="/root/content/source/all/content" omitFirst="true" moduleTemplates="" preparseModules="true"/>
	</body>
</html>

