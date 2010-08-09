<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
	<title>
		<tizzit:content node="//title" omitFirst="true"/>
	</title>
</head>
<body>
<table>
	<tr>
		<td>
			<div class="navigation1"> 
				<tizzit:navigation since="root" depth="2" omitFirst="true" template="templates/navigationTest"/>
			</div>
		</td>
		<td>
			<tizzit:content node="//content" omitFirst="true" moduleTemplates="${[picture:"templates/picture2"]}"/>
			<g:formatDate date="${new Date()}" format="dd.MM.yyyy" />
		</td>
	</tr>
	<tr>
		<td><tizzit:content nodes="//picture" template="templates/pictureListTest" preparseModules="false"/></td>
	</tr>
</table>
<div class="teaser">
</div>
</body>
</html>
