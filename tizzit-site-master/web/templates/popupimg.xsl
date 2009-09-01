<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="ejbimageid" select="'55'"/>
	<xsl:param name="clientCode" select="'tk'"/>


	<xsl:template match="dummy">
		<html> 
			<head>
				<title>Bild</title>
			<script language="JavaScript">
			<xsl:comment>
				<![CDATA[
					function detectImgsize(){						
						
						var x,y,imgHoehe,imgBreite,diffX,diffY, screenWidth, screenHeight, docWidth, docHeight;
						
						//Fenstergroesse
						screenWidth = screen.availWidth;
						screenHeight = screen.availHeight;
						
						//Imagegroesse
						imgHoehe=document.images[0].height;
						imgBreite=document.images[0].width;
						
						//Fenstergroesse
						if (self.innerWidth) // all except Explorer
						{	
							x = self.innerWidth;
							y = self.innerHeight;
							//wegen Bug in Firefox 1.5 
							if (x > self.outerWidth) x = self.outerWidth-6; 
						}
						else if (document.documentElement && document.documentElement.clientHeight)
						// Explorer 6 Strict Mode
						{
							x = document.documentElement.clientWidth;
							y = document.documentElement.clientHeight;
						}
						else if (document.body) // other Explorers
						{
							x = document.body.clientWidth;
							y = document.body.clientHeight;
						}

						//Differenz 
						if (imgHoehe > screenHeight || imgBreite > screenWidth) {
							moveTo(0,0);
							diffX = (imgBreite > screenWidth) ? screenWidth - x : imgBreite - x + 25;
							diffY = ((imgHoehe+25) > screenHeight) ? screenHeight - y - 25 : imgHoehe - y + 25;
						} else {
							diffX = imgBreite - x;
							diffY = imgHoehe - y;						
						}

						//Fenster resize										
						resizeBy(diffX,diffY);						

						//scrollbars einschalten?
						if (imgHoehe > screenHeight || imgBreite > screenWidth) {
							document.getElementById('img-container').style.width=(diffX+x-5)+'px';
							document.getElementById('img-container').style.height=(diffY+y)+'px';
							document.getElementById('img-container').style.overflow='auto';
						}

					}
			]]>
			</xsl:comment>
			</script>
			</head>
			<body MARGINWIDTH="0" MARGINHEIGHT="0" LEFTMARGIN="0" TOPMARGIN="0" onload="javascript:detectImgsize()">
				<div id="img-container">
					<img hspace="0" vspace="0">
						<xsl:attribute name="src">/img/ejbimage/dummy.jpg?id=<xsl:value-of select="$ejbimageid"/>&amp;typ=s</xsl:attribute>
					</img>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
</xsl:stylesheet>
