<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="page">
   <cform>
     <xsl:apply-templates/>
   </cform>
  </xsl:template>

  <xsl:template match="resources"/>
  <xsl:template match="preFromText|postFromText"/>

  <xsl:template match="title">
   <div class="headline">
       <xsl:choose>
           <xsl:when test="//inctext">
               <xsl:value-of select="//inctext/inctitle"/>
           </xsl:when>
           <xsl:otherwise>
             <xsl:apply-templates/>      
           </xsl:otherwise>
       </xsl:choose>     
   </div>
   <br/><br/>
  </xsl:template>
  
  <xsl:template match="content">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="inctext">
   <div>
    <xsl:apply-templates select="div"/>
   </div>    
  </xsl:template>

  <xsl:template match="para">
   <p>
     <xsl:apply-templates/>
   </p>
  </xsl:template>

  <xsl:template match="link">
   <a href="{@href}">
     <xsl:apply-templates/>
   </a>
  </xsl:template>

  <xsl:template match="error">
    <span class="error">
      <xsl:apply-templates/>
    </span>
  </xsl:template>

   <xsl:template match="comment">
    <xsl:comment> 
      <xsl:apply-templates/>
    </xsl:comment>
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>
</xsl:stylesheet>
