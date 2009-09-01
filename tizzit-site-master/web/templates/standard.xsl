<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<modules>
			<xsl:apply-templates select="source"/>
		</modules>
	</xsl:template>

	<xsl:template match="source">
		<xsl:apply-templates/>
	</xsl:template>
 
	<xsl:template match="head" priority="-0.1">
		<ctmpl:module name="headline" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<h1 class="headline">
				<xsl:choose>
					<xsl:when test="title = ''">
						&#160;
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>				
			</h1>
		</ctmpl:module>
		<ctmpl:module name="meta" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<xsl:apply-templates select="title" mode="format"/>
			<xsl:apply-templates select="meta" mode="format"/>
		</ctmpl:module>
		<ctmpl:module name="searchresults" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<xsl:apply-templates select="searchresults" mode="start"/>
		</ctmpl:module>
		<xsl:apply-templates select="." mode="include"/>
	</xsl:template>

	<xsl:template match="all" priority="1">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="content">
		<ctmpl:module name="content" xmlns:ctmpl="http://www.conquest-cms.net/template">
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
	</xsl:template>

	<xsl:template match="footer">
		<ctmpl:module name="footer" xmlns:ctmpl="http://www.conquest-cms.net/template"> 
			<div class="footer">
				<xsl:apply-templates select="." mode="footer"/>
			</div>
		</ctmpl:module>
	</xsl:template>

	<xsl:template match="splashimage/image">
		<ctmpl:module name="splashimage" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="splashimage">
				<img>
					<xsl:attribute name="src">/img/ejbimage<xsl:choose>
						<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
						<xsl:otherwise>/dummy.jpg</xsl:otherwise>
					</xsl:choose>?id=<xsl:apply-templates select="@src"/>&amp;typ=s</xsl:attribute>
				</img>
				<div class="bu">
					<xsl:value-of select="."/>
				</div>
			</div>
		</ctmpl:module>
	</xsl:template>

	<xsl:template match="next">
		<ctmpl:module name="next" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="next">
				<xsl:apply-templates select="navigation/viewcomponent" mode="nextsymbol"/>
			</div>
		</ctmpl:module>
	</xsl:template>
	
	<xsl:template match="prev">
		<ctmpl:module name="prev" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<div class="prev">
				<xsl:apply-templates select="navigation/viewcomponent" mode="prevsymbol"/>
			</div>
		</ctmpl:module>
	</xsl:template>
	
	<xsl:template match="flash/flash">
		
		<xsl:param name="width" select="@width"/>
		<xsl:param name="height" select="@height"/>
		<xsl:param name="version" select="@playerVersion"/>
		<xsl:param name="urlFlashViewer">
			<xsl:text>/img/ejbfile/</xsl:text>
			<xsl:value-of select="@flashName"/>
			<xsl:text>?id=</xsl:text>
			<xsl:value-of select="@src"/>
		</xsl:param>
		<xsl:param name="quality" select="@quality"/>
		<xsl:param name="play" select="@autostart"/>
		<xsl:param name="loop" select="@loop"/>
		<xsl:param name="scale" select="@scale"/>
		<xsl:param name="wmode" select="@transparency"/>
		<xsl:param name="flashvars">
			<xsl:apply-templates select="variables/variable" mode="flashvars"/>
		</xsl:param>
		
		<ctmpl:module name="flash_component" xmlns:ctmpl="http://www.conquest-cms.net/template">
			<object id="Flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version={$version}"  width="{$width}" height="{$height}" align="middle">
				<param name="allowScriptAccess" value="sameDomain" />
				<param name="movie" value="{$urlFlashViewer}" />
				<param name="menu" value="false" />
				<param name="loop" value="{$loop}" />
				<param name="quality" value="{$quality}" />
				<param name="bgcolor" value="#ffffff" />                         
				<param name="FlashVars" value="{$flashvars}"/>  
				<param name="play" value="{$play}"/>    
				<param name="scale" value="{$scale}"/>           
				<param name="wmode" value="{$wmode}"/>  
				<embed FlashVars="{$flashvars}" wmode="{$wmode}" scale="{$scale}" src="{$urlFlashViewer}" play="{$play}" loop="{$loop}" name="Flash" menu="false" quality="{$quality}" bgcolor="#ffffff" width="{$width}" height="{$height}" align="middle" allowscriptaccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
			</object>
		</ctmpl:module>
	</xsl:template>
	
	<!-- don't write empty meta tags -->
	<xsl:template match="meta[@name='description' and not(@content)]" mode="format" priority="1.1"/>
	<xsl:template match="meta[@name='keywords' and not(@content)]" mode="format" priority="1.1"/>
	
	<!-- include templates with lowest priority -->
	<xsl:template match="content | iteration | head" mode="include" priority="-1"></xsl:template>	
	<xsl:template match="content | iteration" mode="include-before" priority="-1"></xsl:template>
	<xsl:template match="content | iteration" mode="include-after" priority="-1"></xsl:template>
	
</xsl:stylesheet>

