<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
	
	<xsl:template match="t_footer">
	    <ctmpl:module name="footer">
	        <div id="t_footer">
	            <!--<div class="footer_item">
	                <a href="#">
	                    <img src="/httpd/img/footer/youtube.gif" alt="Bookmark with delicious" border="0"/>
	                </a>
	            </div>-->
	            <!--<div class="footer_item">
	                <a href="#">
	                    <img src="/httpd/img/footer/apple.gif" alt="Bookmark with delicious" border="0"/>
	                </a>
	            </div>-->
	            <div class="footer_item">
	            	<a href="http://del.icio.us/submit?url={$serverName}{$url}&amp;title=Tizzit.org" target="_blank">
	                	<img src="/httpd/img/footer/delicious.gif" alt="SAVE TO DELICIOUS" border="0"/>
	                </a>
	            </div>
	            <div class="footer_item">
	            	<a href="http://www.facebook.com/sharer.php?u={$serverName}{$url}&amp;t=Tizzit.org" target="_blank">
	            		<img src="/httpd/img/footer/facebook.gif" alt="SHARE ON FACEBOOK" border="0"/>
	                </a>
	            </div>
	            <!--<div class="footer_item">
	            	<a href="#" target="_blank">
	                    <img src="/httpd/img/footer/twitter_11.gif" alt="Bookmark with delicious" border="0"/>
	                </a>
	            </div>-->
	        	<div class="footer_item">
	        		<a href="http://twitthis.com/twit?url={$serverName}{$url}" target="_blank">
	        			<img src="/httpd/img/footer/twitter.gif" alt="TWEET THIS!" border="0"/>
	        		</a>
	        	</div>
	            <!--<div class="footer_item">
	            	<a href="#" target="_blank">
	                    <img src="/httpd/img/footer/google.gif" alt="Bookmark with delicious" border="0"/>
	                </a>
	            </div>-->
	            <div class="footer_item">
	            	<a href="http://www.digg.com/submit?phase=2&amp;url={$serverName}{$url}" target="_blank">
	                	<img src="/httpd/img/footer/digg.gif" alt="DIGG IT!" borer="0"/>
	                </a>
	            </div>
	        	<!--<div class="footer_item">
	        		<a href="#" target="_blank">
	        			<img src="/httpd/img/footer/windows.gif" alt="Bookmark with delicious" border="0"/>
	        		</a>
	        	</div>-->
	        	<div class="footer_item">
	        		<a href="http://www.stumbleupon.com/submit?url={$serverName}{$url}&amp;title=Tizzit.org" target="_blank">
	        			<img src="/httpd/img/footer/stumbleUpon.gif" alt="STUMBLE THIS!" border="0"/>
	        		</a>
	        	</div>
	        	<xsl:if test="not(contains($userAgentString,'MSIE 6.0'))">
		        	<div class="footer_item">
		        		<a href="content.rss" target="_blank">
	    	    			<img src="/httpd/img/footer/rss.gif" alt="RSS Feed" border="0"/>
	        			</a>
	        		</div>
	        	</xsl:if>
	        </div>
	    </ctmpl:module>	
	</xsl:template>

</xsl:stylesheet>
