<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="master.xsl"/>

<xsl:template match="unitinfo">
    <xsl:value-of select="unitInformation/@unitName"/>
</xsl:template>

</xsl:stylesheet>