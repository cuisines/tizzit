<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="webcam" mode="format">
		<div class="webcam">
		    <script><xsl:text disable-output-escaping="yes">&lt;!--</xsl:text>
		        function reloadImage() {
		            ts = new Date();
		            image = document.getElementById('imgwebcam');
    		        image.setAttribute('src','<xsl:value-of select="url"/>?ts='+ts.getTime());
    		        window.setTimeout('reloadImage()',<xsl:value-of select="refreshrate"/>*1000);
		        }
		        
                window.setTimeout('reloadImage()',<xsl:value-of select="refreshrate"/>*1000);
                //<xsl:text disable-output-escaping="yes">--&gt;</xsl:text>
		    </script>
            <table width="100%">
                <tr>
                    <td valign="middle" align="center">
                        <xsl:apply-templates select="." mode="image"/>
                    </td>
                </tr>
                <tr>
                    <td align="center"><a href="javascript:reloadImage();">Bild neu laden</a></td>
                </tr>
            </table>
		</div>
	</xsl:template>
	
    <xsl:template match="webcam" mode="image">
        <img>
            <xsl:attribute name="src">
                <xsl:value-of select="url"/>
            </xsl:attribute>
            <xsl:attribute name="id">imgwebcam</xsl:attribute>
        </img>
    </xsl:template>

</xsl:stylesheet>