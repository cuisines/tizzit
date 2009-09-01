<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctmpl="http://www.conquest-cms.net/template">
	<xsl:include href="includes/date.lib.xsl"/>
	<xsl:include href="news.xsl"/>
	
	<xsl:param name="actualDate" select="''"/>
		
	<xsl:param name="order">
        <xsl:choose>
		    <xsl:when test="$doc//source/all/content/news/sortOrder/value!=''">
			    <xsl:value-of select="//source/all/content/news/sortOrder/value"/>
    		</xsl:when>
	    	<xsl:otherwise>ascending</xsl:otherwise>
    	</xsl:choose>
	</xsl:param>
	
	<xsl:variable name="currentTime">
        <xsl:call-template name="getDate">
            <xsl:with-param name="format" select="'yyyyMMdd'"/>
        </xsl:call-template>
	</xsl:variable>
    
    <xsl:variable name="viewType">
        <xsl:choose>
            <xsl:when test="//source/all/content/newsArchive/value = '1'">live</xsl:when>
            <xsl:when test="//source/all/content/newsArchive/value = '2'">archiv</xsl:when>            
            <xsl:otherwise>all</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <xsl:variable name="viewIdInternalLink">
        <xsl:value-of select="//source/all/content/internalLink/internalLink/@viewid"/>            
    </xsl:variable>

    <xsl:variable name="doc" select="document(concat('cocoon:/content-',$viewIdInternalLink,'.xml'))"/>

    <xsl:template match="content" mode="format" priority="2">   
        <xsl:apply-templates select="$doc//news" mode="newsList"/>
    </xsl:template>
    
    <xsl:template match="news" mode="newsList" priority="1.1">
		<xsl:choose>
			<xsl:when test="$doc//news/newslist//item/newsname!=''">
				<table cellspacing="0" cellpadding="6" border="0" style="border-collapse:collapse;" width="535" class="list-container">
					<tr>
						<th valign="top" width="200" class="list-headline">
							<a>
								<xsl:attribute name="href">page.html?sortBy=title&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Titel</span>
							</a>
						</th>
						<th valign="top" width="135" class="list-headline">
							<a>
								<xsl:attribute name="href">page.html?sortBy=date&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Datum</span>
							</a>
						</th>
						<th valign="top" width="200" class="list-headline">
							<a>
								<xsl:attribute name="href">page.html?sortBy=location&amp;order=<xsl:value-of select="$oppOrder"/></xsl:attribute>
								<span>Ort</span>
							</a>
						</th>
					</tr>
					<xsl:apply-templates select="$doc//news" mode="sort"/>
				</table>
			</xsl:when>
			<xsl:otherwise>Zurzeit liegen leider keine aktuellen Nachrichten und Termine vor. <br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="item" mode="format2">    	
    	<xsl:variable name="newsComparator">
    	    <xsl:choose>
    	        <xsl:when test="newsArchivDate/day != ''">
                	<xsl:call-template name="getFormatDateFromString">
                	    <xsl:with-param name="date" select="concat(newsArchivDate/year,' ',newsArchivDate/month,' ',newsArchivDate/day)"/>
                        <xsl:with-param name="inFormat" select="'yyyy M d'"/>
                        <xsl:with-param name="outFormat" select="'yyyyMMdd'"/>
                    </xsl:call-template>
    	        </xsl:when>
    	        <xsl:otherwise>
                	<xsl:call-template name="getFormatDateFromString">
                	    <xsl:with-param name="date" select="concat(2099,' ',12,' ',01)"/>
                        <xsl:with-param name="inFormat" select="'yyyy M d'"/>
                        <xsl:with-param name="outFormat" select="'yyyyMMdd'"/>
                    </xsl:call-template>    	        
    	        </xsl:otherwise>
    	    </xsl:choose>
        </xsl:variable>	
        
        <xsl:choose>
            <xsl:when test="$viewType = 'archiv'">
                <xsl:if test="$newsComparator &lt; $currentTime">
                    <xsl:apply-templates select="." mode="date"/>  
                </xsl:if>  
            </xsl:when>
            <xsl:when test="$viewType = 'live'">                    
                <xsl:if test="$newsComparator &gt; $currentTime">
                    <xsl:apply-templates select="." mode="date"/>    
                </xsl:if>  
            </xsl:when>            
            <xsl:otherwise>
                <xsl:apply-templates select="." mode="date"/>
            </xsl:otherwise>
        </xsl:choose>        		
	</xsl:template>
	
	<xsl:template match="item" mode="date">
        <tr>
            <td valign="top" class="list-detail">                      	                            	                                
                <a>
                    <xsl:attribute name="href">page.html?newsNr=<xsl:value-of select="@timestamp"/></xsl:attribute>					
                    <xsl:apply-templates select="newsname" mode="format"/>
                </a>                    		
            </td>
            <td valign="top" class="list-detail">
                <xsl:if test="newsdate!=''">
                    <xsl:if test="newsdate-info=''">
                        <xsl:apply-templates select="newsdate" mode="format"/>
                        <xsl:apply-templates select="newsEndDate" mode="format"/>
                    </xsl:if>
                </xsl:if>
                <xsl:if test="newsdate-info!=''">				    
                    <xsl:apply-templates select="newsdate-info"/>
                </xsl:if>
            </td>
            <td valign="top" class="list-detail">
                <xsl:apply-templates select="newslocality"/>
            </td>
        </tr>
    </xsl:template>	

    <xsl:template match="news" mode="sort" priority="1">
		<xsl:choose>
			<xsl:when test="$sortBy='date'">
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">   						
							<xsl:sort select="newsdate/year" data-type="number" order="ascending"/>														
							<xsl:sort select="newsdate/month" data-type="number" order="ascending"/>							
							<xsl:sort select="newsdate/day" data-type="number" order="ascending"/>							
							<xsl:sort select="newsEndDate/year" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/month" data-type="number" order="ascending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="ascending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:when test="$order='descending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">							
							<xsl:sort select="newsdate/year" data-type="number" order="descending"/>							
							<xsl:sort select="newsdate/month" data-type="number" order="descending"/>							
							<xsl:sort select="newsdate/day" data-type="number" order="descending"/>							
							<xsl:sort select="newsEndDate/year" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/month" data-type="number" order="descending"/>
							<xsl:sort select="newsEndDate/day" data-type="number" order="descending"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$sortBy='title'">
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newsname" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$order='ascending'">
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newslocality" data-type="text" order="ascending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="//news/newslist/item" mode="format">
							<xsl:sort select="newslocality" data-type="text" order="descending"/>
							<xsl:sort select="newsdate/year" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/month" data-type="number" order="{$order}"/>
							<xsl:sort select="newsdate/day" data-type="number" order="{$order}"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>		

</xsl:stylesheet>