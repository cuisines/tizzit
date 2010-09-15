<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><g:layoutTitle default="Tizzit Content Page"/></title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
		<meta name="keywords" content=""/>
		<meta name="description" content=""/>
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
		<g:layoutHead/>
		<g:javascript library="application"/>
	</head>
	<body>
		<div class="page_center">
			<div class="page">
				<div class="header_wrapper">
			        <div class="header">
						<div class="banner_wrapper"> <img src="${resource(dir: 'images', file: 'banner.jpg')}" width="884" height="129" alt="tissit content management system" /></div>
						<div class="main_menu_wrapper">
							<tizzit:navigation since="root" depth="1" omitFirst="true" showType="3" template="navigation"/>
						</div>
			        </div>
				</div>
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
								<g:layoutBody/>
							</div>
						</div>
						<!-- End of center colomn -->
						<div class="colomn_center_clear"></div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="footer_wrapper">
		  <div class="footer"> <a href="#"><span class="print">Drucken</span></a> | <a href="#"><span class="mail">Versenden per Email</span></a> | <a href="#top"><span class="favorites">Zu Favoriten hinzufügen</span></a> | <a href="#"><span class="pageup">Zum Seitenanfang</span></a></div>
		</div>
	</body>
</html>
