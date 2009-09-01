<?xml version='1.0'?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:session="http://apache.org/cocoon/session/1.0"
    xmlns:juwimm-registry="http://www.juwimm.net/cocoon/registry"
    >
    
    
    
    
    <xsl:param name="loginname" select="''"/>
    <xsl:param name="pin" select="''"/>
    <xsl:param name="id" select="''"/>
    <xsl:param name="doccheckid" select="''"/>
    <xsl:param name="color-session" select="''"/>
    
    <xsl:template match="ID">
        <ID>         
             
            <session:createcontext name="lms"/>
            <session:setxml context="lms" path="/">
                <lms>
                    <authentication>                        
                        <juwimm-registry:user >
                            <xsl:choose>
                                <xsl:when test="$loginname!=''">
                                    <xsl:attribute name="loginname" >
                                        <xsl:value-of select="$loginname"/>
                                    </xsl:attribute>                                    
                                </xsl:when>
                                <xsl:when test="$pin!=''">
                                    <xsl:attribute name="pin" select="$pin"/>                                    
                                </xsl:when>
                                <xsl:when test="$id!=''">
                                    <xsl:attribute name="id" select="$id"/>                                    
                                </xsl:when>
                                <xsl:when test="$doccheckid!=''">
                                    <xsl:attribute name="doccheckid" select="$doccheckid"/>                                    
                                </xsl:when>
                                <xsl:otherwise/>
                            </xsl:choose>                           
                        </juwimm-registry:user>                        
                    </authentication>
                    
                </lms>
            </session:setxml>
            <xsl:apply-templates/>
        </ID>
    </xsl:template>
    
  
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>	
    </xsl:template>

</xsl:stylesheet>