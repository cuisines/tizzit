<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:cinclude="http://apache.org/cocoon/include/1.0"
>
    <xsl:include href="variables.xsl"/>
    
    <xsl:template match="/">
        <div>
            <xsl:attribute name="id">
                <xsl:value-of select="name(child::*)"/>
            </xsl:attribute>
            <xsl:attribute name="class">
                <xsl:value-of select="name(child::*)"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template match="navigation">
        <xsl:apply-templates select="viewcomponent/viewcomponent"/>
    </xsl:template>
    
    <xsl:template match="viewcomponent">
        <xsl:copy-of select="document(concat('cocoon:/content-',@id,'.xml'))"/>    
        <xsl:apply-templates select="viewcomponent"/>             
    </xsl:template>
  
</xsl:stylesheet>
