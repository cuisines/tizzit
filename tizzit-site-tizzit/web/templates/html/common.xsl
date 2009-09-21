<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
  
	<xsl:include href="variables.xsl"/>
       
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/line.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/format.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/db-components.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/urlEncoder.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/contentmenue.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/minipage.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/format_xhtml.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/standard.xsl"/>
	<xsl:include href="../../../../tizzit-site-master/web/templates/includes/date.lib.xsl"/>	
	  
	<xsl:include href="include/footer.xsl"/>
	<xsl:include href="include/teaser.xsl"/>
	<xsl:include href="include/image_div.xsl"/> 
	 
	<!-- includes Web 2.0 -->  
	<!--<xsl:include href="include/web20_rating.xsl"/>-->
	<xsl:include href="include/web20_tagging.xsl"/>
	<!--<xsl:include href="include/web20_comment.xsl"/>--> 
	 
	<!-- Configuration parameters so activate additional features -->
	
	<!-- Name of the template without teaser -->
	<xsl:param name="cfg-templateNameWithoutTeaser" select="'standard-without-teaser'"/>
	
	<!-- Enable content table scaling -->
	<xsl:param name="cfg-enableMaxContentTableWidth" select="'true'"/>
	<xsl:param name="cfg-maxContentTableWidth" select="'500'"/>
	<xsl:param name="cfg-maxContentTableWidthWithoutTeaser" select="'700'"/>
 
	<!-- Enable content image scaling -->
	<xsl:param name="cfg-enableMaxContentImageWidth" select="'true'"/>
	<xsl:param name="cfg-maxContentImageWidth" select="'500'"/>
	<xsl:param name="cfg-maxContentImageWidthWithoutTeaser" select="'700'"/>
	
	<!-- Shadowbox for content image thumbnails - only for center images -->
	<xsl:param name="cfg-enableShadowboxContentThumbnails" select="'true'"/>
	<xsl:param name="cfg-shadowboxContentThumbnailsImageWidth" select="'90'"/>
	
	<!-- Overwrite standard content tables for better usability, please use our config parameters in variables.xsl -->
	<xsl:template match="table[not(@viewborder)] | th | td" mode="format" priority="2">
		<!-- Show error messages on work server if table has too much width -->
		<xsl:if test="$cfg-enableMaxContentTableWidth = 'true'">
			<xsl:if test="not(contains(@width, '%'))">
				<xsl:choose>
					<xsl:when test="name()='table' and @width &gt; $cfg-maxContentTableWidthWithoutTeaser and $liveserver = 'false' and $template = $cfg-templateNameWithoutTeaser">
						<div class="box-red">
							Die Tabelle überschreitet die maximale Breite von <xsl:value-of select="$cfg-maxContentTableWidthWithoutTeaser"/> Pixeln und wurde automatisch auf die maximale Seitenbreite skaliert.<br/><br/>
							Bitte verringern Sie die Breite der Tabelle über die Tabelleneigenschaften im CMS. 
						</div>
					</xsl:when>
					<xsl:when test="name()='table' and @width &gt; $cfg-maxContentTableWidth and $liveserver = 'false' and $template != $cfg-templateNameWithoutTeaser">
						<div class="box-red">
							Die Tabelle überschreitet die maximale Breite von <xsl:value-of select="$cfg-maxContentTableWidth"/> Pixeln und wurde automatisch auf die maximale Seitenbreite skaliert.<br/><br/>
							Bitte verringern Sie die Breite der Tabelle über die Tabelleneigenschaften im CMS. 
						</div>
					</xsl:when>
				</xsl:choose>
			</xsl:if>
		</xsl:if>
		<xsl:element name="{name()}">	    
			<xsl:choose>
				<xsl:when test="name()='table'">	    
					<xsl:attribute name="cellpadding">
						<xsl:choose>
							<xsl:when test="not(@cellpadding)">2</xsl:when>
							<xsl:otherwise><xsl:value-of select="@cellpadding"/></xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<xsl:attribute name="cellspacing">
						<xsl:choose>
							<xsl:when test="not(@cellspacing)">0</xsl:when>
							<xsl:otherwise><xsl:value-of select="@cellspacing"/></xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="name()!='table'">								    			    			    
					<xsl:attribute name="style">border-width: <xsl:value-of select="../../@border"/>
						<xsl:text>px;border-style: solid;</xsl:text>					
						<!--vertical align definieren-->
						<xsl:if test="@valign">
							<xsl:text>vertical-align:</xsl:text><xsl:value-of select="@valign"/>
						</xsl:if>					
					</xsl:attribute>
				</xsl:when>
				<xsl:when test="@border!='0'">			    
					<xsl:attribute name="style">
						<xsl:text>border-width:</xsl:text><xsl:value-of select="@border"/><xsl:text>px;border-style: solid;</xsl:text>
					</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="@bgcolor">
					<xsl:attribute name="class">tablebg</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
			<xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
			<!-- Rescale the table if the content table scaling feature is activated -->
			<xsl:if test="@width">
				<xsl:attribute name="width">
					<xsl:choose>
						<xsl:when test="not(contains(@width, '%')) and $cfg-enableMaxContentTableWidth = 'true'">
							<xsl:choose>
								<xsl:when test="$template = $cfg-templateNameWithoutTeaser and @width &gt; $cfg-maxContentTableWidthWithoutTeaser"><xsl:value-of select="$cfg-maxContentTableWidthWithoutTeaser"/></xsl:when>
								<xsl:when test="$template != $cfg-templateNameWithoutTeaser and @width &gt; $cfg-maxContentTableWidth"><xsl:value-of select="$cfg-maxContentTableWidth"/></xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@width"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="contains(@width, '%') and $cfg-enableMaxContentTableWidth = 'true'">
							<xsl:choose>
								<xsl:when test="$template = $cfg-templateNameWithoutTeaser and @width = '100%'"><xsl:value-of select="$cfg-maxContentTableWidthWithoutTeaser"/></xsl:when>
								<xsl:when test="$template != $cfg-templateNameWithoutTeaser and @width = '100%'"><xsl:value-of select="$cfg-maxContentTableWidth"/></xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@width"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="@width"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="@height"><xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute></xsl:if>
			<xsl:apply-templates mode="format"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="content" priority="2.3">
		<ctmpl:module name="content"> 
			<div class="content">
				<xsl:apply-templates select="." mode="include-before"/>
				<xsl:apply-templates select="." mode="format"/>
				<xsl:apply-templates select="." mode="include"/>
				<xsl:apply-templates select="." mode="include-after"/>
				<xsl:choose> 
					<xsl:when test="normalize-space(.) = '' and count(child::*) = 0">
						<xsl:text>&#160;</xsl:text>
					</xsl:when>
				</xsl:choose>
			</div>           
		</ctmpl:module> 
		<ctmpl:module name="search">
			<xsl:call-template name="search"/>
		</ctmpl:module>	
	</xsl:template> 
	
	<xsl:template name="search">
		<div id="search_area">    
			<div id="search_button"> 
				<input type="image" id="search_arrow" src="/httpd/img/search_btn.gif" onclick="javascript:fastSearch2('searchresult/page.html');"/>                                 
			</div>	
			<div id="search_container"> 
				<form name="fastsearch" method="get" onsubmit="javascript:fastSearch2('searchresult/page.html');" action="javascript:fastSearch2('searchresult/page.html');">
					<input class="hidden" type="hidden" name="format" value="long"/>
					<input type="hidden" name="cqSearchPageSize" value="10"/>
					<input type="hidden" name="cqSearchPageNumber" value="0"/>
					<input id="searchValue" type="text" name="cqWebSearchQuery" value="Search" class="searchinput" onclick="if (this.value=='Search')value=''" onblur="if (this.value=='')value='Search'"/>
				</form>
			</div>  
		</div>
	</xsl:template>
	
</xsl:stylesheet>