<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="common.xsl"/>
	
	<xsl:param name="format" select="'long'"/>
	<xsl:param name="cqSearchPageSize" select="'10'"/>
    <xsl:param name="cqWebSearchQuery"/>
	
	<xsl:template match="content" mode="include" priority="2">
		<xsl:apply-templates select="../../head/searchresults" mode="start"/>
	</xsl:template>
	 
	<xsl:template match="searchresults" mode="start">
		<xsl:choose>
			<xsl:when test="@totalCount &gt; 0">
				
				<!--Suche liefert Ergebnis(se)-->
				<div class="search-result-headline">
					<xsl:choose>
						<xsl:when test="@totalCount = 1">
						    <xsl:text>Es wurde </xsl:text>
						    <xsl:value-of select="@totalCount"/>
						    <xsl:text> Ergebnis zu "</xsl:text>
						    <span class="search_word">
						        <xsl:value-of select="$cqWebSearchQuery"/>
						    </span>
						    <xsl:text>" gefunden</xsl:text>
						</xsl:when>
						<xsl:when test="@totalCount &gt; 1">
						    <xsl:text>Es wurden </xsl:text>
						    <xsl:value-of select="@totalCount"/>
						    <xsl:text> Ergebnisse zu "</xsl:text>
						    <span class="search_word">
						        <xsl:value-of select="$cqWebSearchQuery"/>
						    </span>
						    <xsl:text>" gefunden</xsl:text>
						</xsl:when>
					</xsl:choose>
				</div>
				<!--
					cqSearchPageSize muss hier nochmals gesetzt werden!!!
				-->
				<xsl:if test="@totalCount &gt; 10">
					<div class="search-pager">
						<xsl:apply-templates select="." mode="header"/>
					</div>
				</xsl:if>
				<div class="search-results">
					<xsl:apply-templates mode="start"/>
				</div>
				<!--
					cqSearchPageSize muss hier nochmals gesetzt werden!!!
				-->
				<xsl:if test="@totalCount &gt; 10">
					<div class="search-pager">
						<xsl:apply-templates select="." mode="header"/>
					</div>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<div class="search-result-headline" style="color:#FF0000;">Es wurden keine Dokumente gefunden</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="searchresults" mode="header" priority="1">
		<xsl:if test="@pageAmount &gt; 1">
			
			<xsl:if test="not(@pageNumber&lt;=0)">
				<a onmousedown="this.href = encodeURI(this.href);" href="?cqSearchPageNumber={number(@pageNumber)-1}&amp;cqWebSearchQuery={$cqWebSearchQuery}&amp;cqSearchPageSize={$cqSearchPageSize}&amp;format={$format}&amp;gg={$cqWebSearchQuery}" class="searchNavigationLink-left">
					<img src="/httpd/img/search_back.gif" alt="zurück" onmouseover="this.src='/httpd/img/search_back_h.gif'" onmouseout="this.src='/httpd/img/search_back.gif'" border="0"/>
				</a>
			</xsl:if>
			
			<span>|&#160;</span>
			
			<xsl:apply-templates select="." mode="pager">
				<xsl:with-param name="startIndex"><xsl:value-of select="'1'"/></xsl:with-param>
				<xsl:with-param name="endIndex"><xsl:value-of select="@pageSize"/></xsl:with-param>
				<xsl:with-param name="pageNo"><xsl:value-of select="'1'"/></xsl:with-param>
				<xsl:with-param name="currentPageNo"><xsl:value-of select="@pageNumber + 1"/></xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:if test="not(count(result)&lt;10)">
				<a onmousedown="this.href = encodeURI(this.href);" href="?cqSearchPageNumber={number(@pageNumber)+1}&amp;cqWebSearchQuery={$cqWebSearchQuery}&amp;cqSearchPageSize={$cqSearchPageSize}&amp;format={$format}&amp;gg={$cqWebSearchQuery}" class="searchNavigationLink-right">
					<img src="/httpd/img/search_next.gif" alt="weiter" onmouseover="this.src='/httpd/img/search_next_h.gif'" onmouseout="this.src='/httpd/img/search_next.gif'" border="0"/>
				</a>
			</xsl:if>
			
			<div class="clear">&#160;</div>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="searchresults" mode="pager">
		<xsl:param name="startIndex"/>
		<xsl:param name="endIndex"/>
		<xsl:param name="pageNo"/>
		<xsl:param name="currentPageNo"/>
		
		<xsl:choose>
			<xsl:when test="$currentPageNo = $pageNo">
			    <span class="currentPage" style="color: #86202E;"><xsl:value-of select="$pageNo"/></span><span>&#160;|&#160;</span>
				<!--<div class="pagesCount">
					<xsl:value-of select="$startIndex"/>-<xsl:value-of select="$endIndex"/>
				</div>-->
			</xsl:when>
			<xsl:otherwise>
				<span>
					<a onmousedown="this.href = encodeURI(this.href);">
						<xsl:attribute name="href">
							<xsl:text>page.html?format=</xsl:text>
							<xsl:value-of select="$format"/>
							<xsl:text>&amp;cqWebSearchQuery=</xsl:text>
							<xsl:value-of select="$cqWebSearchQuery"/>
							<xsl:text>&amp;cqSearchPageSize=</xsl:text>
							<xsl:value-of select="$cqSearchPageSize"/>
							<xsl:text>&amp;cqSearchPageNumber=</xsl:text>
							<xsl:value-of select="$pageNo - 1"/>
							<xsl:text>&amp;gg=</xsl:text>
							<xsl:value-of select="$cqWebSearchQuery"/>
						</xsl:attribute>
						<xsl:value-of select="$pageNo"/>
					</a>&#160;|&#160;
				</span>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:variable name="newEnd">
			<xsl:choose>
				<xsl:when test="$endIndex + @pageSize &lt; @totalCount"><xsl:value-of select="$endIndex + @pageSize"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="@totalCount"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:if test="$pageNo &lt; @pageAmount">
			<xsl:apply-templates select="." mode="pager">
				<xsl:with-param name="startIndex" select="$startIndex + @pageSize"/>
				<xsl:with-param name="endIndex" select="$newEnd"/>
				<xsl:with-param name="pageNo" select="$pageNo + 1"/>
				<xsl:with-param name="currentPageNo" select="$currentPageNo"/>
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="result[@resultType='page']" mode="start" priority="1">
		
		<div>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="position()=1">search-result_first</xsl:when>
					<xsl:otherwise>search-result</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			
			<xsl:variable name="tmp"><xsl:value-of select="url/text()"/></xsl:variable>
			
			<xsl:variable name="length"><xsl:value-of select="string-length($tmp)"/></xsl:variable>
			
			<xsl:variable name="new">
				<xsl:choose>
					<xsl:when test="substring($tmp, $length, 1) != '/'">
						<xsl:variable name="front"><xsl:value-of select="substring-before($tmp,'/serach.html')"/></xsl:variable>
						<xsl:value-of select="concat($front, '/page.html')"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$tmp"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="score">
				<xsl:value-of select="substring-before(score/text(), '%')"/>
			</xsl:variable>
			
			<div class="search-result-title">
				
				<img src="/httpd/img/icon_document.gif" alt="Icon Seite" class="search-icon"/>
				
				<div class="search-result-rating">
					<xsl:choose>
						<xsl:when test="$score &gt; 75">
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
						</xsl:when>
						<xsl:when test="$score &gt; 50">
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
						</xsl:when>
						<xsl:when test="$score &gt; 25">
							<img src="/httpd/img/star.gif"/>
							<img src="/httpd/img/star.gif"/>
						</xsl:when>
						<xsl:when test="$score &gt;= 0">
							<img src="/httpd/img/star.gif"/>
						</xsl:when>
						<xsl:otherwise>&#160;</xsl:otherwise>
					</xsl:choose>
				</div>
				&#160;
				<xsl:choose>
					<xsl:when test="title != '' and title != '&#160;'">
						<a><xsl:attribute name="href"><xsl:value-of select="$tmp"/></xsl:attribute><xsl:value-of select="title/text()"/></a>
					</xsl:when>
					<xsl:otherwise>
					    <a><xsl:attribute name="href"><xsl:value-of select="$new"/></xsl:attribute><xsl:text>Diese Seite hat keine Headline/Seitentitel</xsl:text></a>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			<div class="clear">&#160;</div>
			<div class="search-result-summary">
				<xsl:if test="$format = 'long'"><span>... <xsl:apply-templates select="summary" mode="format"/> ...</span></xsl:if>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="result[@resultType='document']" mode="start" priority="1">
		<div class="search-result">
			
			<xsl:variable name="score">
				<xsl:value-of select="substring-before(score/text(), '%')"/>
			</xsl:variable>
			
			<xsl:if test="string-length(documentName) &gt; 0">
				
				<div class="search-result-title">
					<xsl:choose>
						<xsl:when test="documentName != '' and documentName != '&#160;'">
							<xsl:choose>
							    <xsl:when test="mimeType = 'application/pdf'"><img src="/httpd/img/icon_pdf.gif" alt="Icon PDF" class="search-icon"/>&#160;&#160;</xsl:when>
							    <xsl:when test="mimeType = 'application/rtf'"><img src="/httpd/img/icon_rtf.gif" alt="Icon RTF" class="search-icon"/>&#160;&#160;</xsl:when>
							    <xsl:when test="mimeType = 'application/msword'"><img src="/httpd/img/icon_doc.gif" alt="Icon DOC" class="search-icon"/>&#160;&#160;</xsl:when>
							    <xsl:when test="mimeType = 'application/msexcel'"><img src="/httpd/img/icon_xls.gif" alt="Icon XLS" class="search-icon"/>&#160;&#160;</xsl:when>
							    <xsl:when test="mimeType = 'application/postscript'"><img src="/httpd/img/icon_ps.gif" alt="Icon PS" class="search-icon"/>&#160;&#160;</xsl:when>
							    <xsl:when test="mimeType = 'application/vnd.ms-powerpoint'"><img src="/httpd/img/icon_ppt.gif" alt="Icon PPT" class="search-icon"/>&#160;&#160;</xsl:when>
							</xsl:choose>
							
							<div class="search-result-rating">
								<xsl:choose>
									<xsl:when test="$score &gt; 80">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt; 50">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt; 25">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt;= 0">
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:otherwise>&#160;</xsl:otherwise>
								</xsl:choose>
							</div>
							
							<a target="_blank">
								<xsl:attribute name="href">/img/ejbfile/<xsl:value-of select="documentName"/>?id=<xsl:value-of select="documentId"/></xsl:attribute>
								<xsl:value-of select="documentName/text()"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							
							<div class="search-result-rating">
								<xsl:choose>
									<xsl:when test="$score &gt; 80">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt; 50">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt; 25">
										<img src="/httpd/img/star.gif"/>
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:when test="$score &gt;= 0">
										<img src="/httpd/img/star.gif"/>
									</xsl:when>
									<xsl:otherwise>&#160;</xsl:otherwise>
								</xsl:choose>
							</div>
							
						    <a target="_blank"><xsl:attribute name="href">/img/ejbfile/<xsl:value-of select="documentName"/>?id=<xsl:value-of select="documentId"/></xsl:attribute><xsl:text>Diese Seite hat keine Headline/Seitentitel</xsl:text></a>
						</xsl:otherwise>
					</xsl:choose>
				</div>
				<div class="clear">&#160;</div>
				<div class="search-result-summary">
					<xsl:if test="$format = 'long'"><span>... <xsl:apply-templates select="summary" mode="format"/> ...</span></xsl:if>
				</div>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="viewcomponent" mode="var">
		<xsl:param name="url120"/>
		
		<xsl:variable name="actualUrl">
			<xsl:value-of select="url"/>
		</xsl:variable>
		 
		<xsl:if test="normalize-space($url120)=normalize-space($actualUrl)">
			<xsl:value-of select="position()"/>
		</xsl:if>
		
	</xsl:template>
	
	<xsl:template match="summary" mode="format">
		<xsl:apply-templates mode="format"/>
	</xsl:template>
	
</xsl:stylesheet>