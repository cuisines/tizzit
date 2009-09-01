<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--	automatisch generierte Teamliste anzeigen-->

	<xsl:template match="content" mode="include" priority="1">
		<xsl:apply-templates select="../unitMembersList | ../membersList" mode="format"/>
	</xsl:template>
	
</xsl:stylesheet>
