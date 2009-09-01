<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fb="http://apache.org/cocoon/forms/1.0#binding"
    xmlns:fd="http://apache.org/cocoon/forms/1.0#definition">
    
    
    <xsl:template match="/">
        <fb:context path="/">
            <xsl:apply-templates select="//fd:field | //fd:booleanfield"/>
        </fb:context>
    </xsl:template>    
    
    <xsl:template match="fd:field | fd:booleanfield">
        <fb:value id="{@id}" path="{@id}"/>
    </xsl:template>
        
</xsl:stylesheet>
