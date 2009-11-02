<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="serverName" select="'unknown'" />

    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>
                    Site is undergoing maintenance
                </title>
            </head>
            <body>
                <p>We are sorry that this site is unavailable at the moment but we are carrying out important maintenance work. The site will be available again shortly.</p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
