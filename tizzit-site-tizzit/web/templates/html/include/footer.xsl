<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
	
	<xsl:template match="sn_footer">
	    <ctmpl:module name="sn_footer">
	    	<ul id="sn_footer">
	    		<xsl:apply-templates select="delicious" mode="sn"/>
	    		<xsl:apply-templates select="facebook" mode="sn"/>
	    		<xsl:apply-templates select="twitter" mode="sn"/>
	    		<xsl:apply-templates select="digg" mode="sn"/>
	    		<xsl:apply-templates select="stumble" mode="sn"/>
	    		<xsl:apply-templates select="rrs" mode="sn"/>
	    	</ul>
	    </ctmpl:module>	
	</xsl:template>
	
	<xsl:template match="delicious" mode="sn">
		<li class="footer_item">
			<a href="http://del.icio.us/submit?url=http://{$serverName}/{$language}/{$url}/page.html&amp;title=Tizzit.org" target="_blank">
				<img src="/httpd/img/footer/delicious.gif" alt="SAVE TO DELICIOUS!" border="0"/>
				<span>SAVE TO DELICIOUS!</span>
			</a>
		</li>
	</xsl:template>
	
	<xsl:template match="facebook" mode="sn">
		<li class="footer_item">
			<a href="http://www.facebook.com/sharer.php?u=http://{$serverName}/{$language}/{$url}/page.html&amp;t=Tizzit.org" target="_blank">
				<img src="/httpd/img/footer/facebook.gif" alt="SHARE ON FACEBOOK!" border="0"/>
				<span>SHARE ON FACEBOOK!</span>
			</a>
		</li>
	</xsl:template>
		
	<xsl:template match="twitter" mode="sn">
		<li class="footer_item">
			<a href="http://twitthis.com/twit?url=http://{$serverName}/{$language}/{$url}/page.html" target="_blank">
				<img src="/httpd/img/footer/twitter.gif" alt="TWEET THIS!" border="0"/>
				<span>TWEET THIS!</span>
			</a>
		</li>
	</xsl:template>
	            
	<xsl:template match="digg" mode="sn">
		<li class="footer_item">
			<a href="http://www.digg.com/submit?phase=2&amp;url=http://{$serverName}/{$language}/{$url}/page.html" target="_blank">
				<img src="/httpd/img/footer/digg.gif" alt="DIGG IT!" borer="0"/>
				<span>DIGG IT!</span>
			</a>
		</li>
	</xsl:template>
	        	
	<xsl:template match="stumble" mode="sn">
		<li class="footer_item">
			<a href="http://www.stumbleupon.com/submit?url=http://{$serverName}/{$language}/{$url}/page.html&amp;title=Tizzit.org" target="_blank">
				<img src="/httpd/img/footer/stumbleUpon.gif" alt="STUMBLE THIS!" border="0"/>
				<span>STUMBLE THIS!</span>
			</a>
		</li>
	</xsl:template>
	        	
	<xsl:template match="rrs" mode="sn">
		<xsl:if test="not(contains($userAgentString,'MSIE 6.0'))">
			<li class="footer_item">
				<a href="content.rss" target="_blank">
					<img src="/httpd/img/footer/rss.gif" alt="RSS Feed" border="0"/>
					<span>RSS Feed!</span>
				</a>
			</li>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>