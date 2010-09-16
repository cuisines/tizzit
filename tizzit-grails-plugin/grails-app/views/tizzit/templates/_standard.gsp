<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<title><tizzit:content node="//title" omitFirst="true"/></title>
		<meta name="layout" content="${(params.popup) ? 'popup' : 'main'}"/>
		<meta name="viewComponentId" content="${params.tizzit.viewComponentId}"/>
	</head>
	<body>
		<div class="colomn_left_wrapper">
			<div class="colomn_left">
				<div class="sidenavi_wrapper">
					<div class="sidenavi_header"></div>
					<tizzit:navigation since="lastNavigationRoot" depth="2" omitFirst="true" showType="0" template="navigation"/>
				</div>
			</div>
		</div>
		<div class="colomn_right_wrapper">
			<div class="colomn_right">
				<div class="teaser_box_wrapper">
					<div class="teaser_box">
						<div class="teaser_header"><span>Europa</span></div>
						<div class="teaser_image"><img src="${resource(dir: 'images', file: '1x1.png')}" width="168" height="100" alt="" /></div>
						<div class="teaser_content_wrapper">
							<div class="teaser_subline"><span>Karte</span></div>
							<div class="teaser_text"><span>Duis autem vel eum irure dolor in.</span></div>
							<div class="teaser_link"><a href="#">mehr Infos</a></div>
							<div class="clear"></div>
						</div>
						<div class="teaser_footer"></div>
					</div>
				</div>
				<div class="teaser_box_wrapper">
					<div class="teaser_box">
						<div class="teaser_header"><span>Service</span></div>
						<div class="teaser_content_wrapper">
							<div class="teaser_subline"><span>Hilfe und Tipps</span></div>
							<div class="teaser_text"><span>Duis autem vel eum irure dolor in henderit in vulputate velit.</span></div>
							<div class="teaser_link"><a href="#">mehr Infos</a></div>
							<div class="clear"></div>
						</div>
						<div class="teaser_footer"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="colomn_center_wrapper">
			<div class="colomn_center">
				<div class="breadcrumb_wrapper">
					<div class="breadcrumb"> <a href="#">Startseite</a><a href="#" class="sublink">Chain 1</a><a href="#" class="sublink">Chain 2</a> </div>
				</div>
				<div class="main_box_wrapper">
					<div class="main_box">
						<h1><tizzit:content node="//title" omitFirst="true"/></h1>
						<tizzit:content node="/root/content/source/all/content" omitFirst="true" moduleTemplates="" preparseModules="true"/>
					</div>
				</div>
				<!-- End of center colomn -->
				<div class="colomn_center_clear"></div>
			</div>
		</div>
	</body>
</html>