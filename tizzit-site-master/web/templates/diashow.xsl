<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- Eingebunden z.B. in cmspw -> asw  -->
    
    
    <!--<xsl:include href="includes/image.xsl"/>-->
    <xsl:include href="standard.xsl"/>
    
    <xsl:template match="content | iteration" mode="include" priority="0.1">
        <xsl:apply-templates select="../diashow"/>
    </xsl:template>
    
    <xsl:template match="diashow">
        <!-- slideShow('0'); -->
           <table>
              <tr>
                 <td>
                       <div class="picinfo">
                           <div class="headline_dia">
                               <div class="textcenter2">
                                   <xsl:apply-templates select="../event"/>
                               </div>
                           </div>
                           <div class="pictext" id="diaInfos">
                               <xsl:apply-templates select="item[1]/dia-infos"/>
                               </div>
                               <div class="copyright">
                                   <xsl:apply-templates select="../copyright"/>
                               </div>
                       </div>
                 </td>
                 <td>
                       <div class="pic">
                           <xsl:apply-templates select="item[1]/dia-picture/image" mode="Image"/>
                       </div>
                 </td>
              </tr>
              <tr>
                  <td colspan="3"><div class="piclist"><xsl:apply-templates select="item" mode="smallImage"/></div></td>
              </tr>
          </table>
          <div class="navi">
               <a href="#" onclick="prevpic();"><img src="/httpd/img/prevpic.jpg" border="0"/></a>
              <a href="#" onclick="nextpic();" style="margin-left:10px;"><img border="0" src="/httpd/img/nextpic.jpg"/></a>
          </div>    
            <div class="bildtext">
                <!-- Nur Bu vom ersten Bild - andere mit js -->                   
               <xsl:apply-templates select="item[1]/dia-picture/image" mode="bu"/>                    
            </div> 
                <!-- JS Script einfuegen und Diashow starten. -->
               <script type="text/javascript">
                   //Array mit img IDs
                   var imageArray = new Array(<xsl:apply-templates select="item" mode="array"/>);
                   //hiddenInterval setzen                   
                   var hiddeninterval = <xsl:value-of select="../interval"/>;
                   //slideShow starten
                   slideShow('0');
                   <xsl:apply-templates select="." mode="include"/>
               </script>
    </xsl:template>
   
    <xsl:template match="item" mode="array">        
        <xsl:value-of select="dia-picture/image/@src"/>
        <xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>
   
    <xsl:template match="event">
        <xsl:apply-templates/>        
    </xsl:template>
    
    <xsl:template match="copyright">
        <xsl:apply-templates mode="format"/>        
    </xsl:template>
   
    <xsl:template match="item" mode="smallImage">
        <xsl:variable name="height">
            <xsl:value-of select="dia-picture/image/@height"/>
        </xsl:variable>
        <xsl:variable name="width">
            <xsl:value-of select="dia-picture/image/@width"/>
        </xsl:variable>
        <xsl:variable name="src">
            <xsl:value-of select="dia-picture/image/@src"/>
        </xsl:variable>
        <xsl:variable name="bu">
            <xsl:value-of select="dia-picture/image"/>
        </xsl:variable>
        <!-- Thumbnail aller Bilder zum klicken -->
        <a>
            <xsl:attribute name="href">javascript:showImg('<xsl:value-of select="position()-1"/>')</xsl:attribute>
            <!-- Thumbnail schreiben -->
            <img hspace="0" vspace="0" class="listedpic" border="0" >
            	<xsl:attribute name="src">/img/ejbimage<xsl:choose>
            		<xsl:when test="dia-picture/image/filename != ''">/<xsl:value-of select="dia-picture/image/filename"/></xsl:when>
            		<xsl:otherwise>/dummy.jpg</xsl:otherwise>
            	</xsl:choose>?id=<xsl:apply-templates select="dia-picture/image/@src"/>&amp;typ=t</xsl:attribute>
                <xsl:attribute name="id"><xsl:value-of select="position()-1"/></xsl:attribute>
            </img>
        </a>
        <!-- unsichtbarer Layer mit Text und BU pro Bild -->
        <div style="display:none">
            <!-- Text zu image -->            
            <xsl:attribute name="id">infos_<xsl:value-of select="position()-1"/></xsl:attribute>            
            <xsl:apply-templates select="dia-picture/image" mode="bu"/>
            <xsl:apply-templates  select="dia-infos" mode="format"/>            
        </div>
    </xsl:template>
     
    <!--Bildunterschrift-->
     <xsl:template match="image" mode="bu">
         <xsl:if test=".!=''">
             <div class="subline" id="diaBu">
                 <xsl:value-of select="."/>
             </div>
         </xsl:if> 
    </xsl:template>

    <!--Bild einfügen-->
    <xsl:template match="image" mode="Image">
        <xsl:if test="@src!=''">
            <img hspace="0" vspace="0" id="diaImg">
            	<xsl:attribute name="src">/img/ejbimage<xsl:choose>
            		<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
            		<xsl:otherwise>/dummy.jpg</xsl:otherwise>
            	</xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>             
           </img>
        </xsl:if>
    </xsl:template>
    
 
    <xsl:template match="dia-infos" mode="format">
        <xsl:apply-templates mode="format"/>       
    </xsl:template>
    
    
</xsl:stylesheet>
