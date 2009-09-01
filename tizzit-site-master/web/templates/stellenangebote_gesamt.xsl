<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

	<xsl:include href="stellenangebote.xsl"/>

	<xsl:template match="content" mode="format" priority="1.2">
	    <xsl:choose>
	        <xsl:when test="$jobNr != 0">
	            <xsl:apply-templates select="fulltextsearch/jobs" mode="jobDetail" />	        
	        </xsl:when>
	        <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="not(fulltextsearch/jobs)">
                        Zurzeit liegen leider keine Stellenangebote vor. <br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.                    
                    </xsl:when>
                    <xsl:otherwise>
        				<!-- stellenangebote - tabellarische uebersicht -->
        				<table cellspacing="0" cellpadding="6" border="0" style="border-collapse:collapse;">
        					<tr>
        						<th style="border: 1px solid #888888" valign="top" width="150">Stellenbezeichung</th>
        						<th style="border: 1px solid #888888" valign="top" width="150">
        							<a>
        								<xsl:attribute name="href">page.html?sortBy=arbeitsbeginn</xsl:attribute>
        								<span>Arbeitsbeginn</span>
        							</a>
        						</th>
        						<th style="border: 1px solid #888888" valign="top" width="150">
        							<a>
        								<xsl:attribute name="href">page.html?sortBy=berufsgruppe</xsl:attribute>
        								<span>Berufsgruppe</span>
        							</a>
        						</th>
        						<th style="border: 1px solid #888888" valign="top" width="150">
        							<a>
        								<xsl:attribute name="href">page.html?sortBy=einrichtung</xsl:attribute>
        								<span>Klinik</span>
        							</a>
        						</th>
        					</tr>
            	            <xsl:apply-templates select="fulltextsearch" mode="jobList" />
           				</table>
                    </xsl:otherwise>
                </xsl:choose>
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>
	
	<xsl:template match="fulltextsearch" mode="jobList" priority="1.1">	
	    <xsl:call-template name="jobs"/>
	</xsl:template>
	
    <xsl:template name="jobs">
    	<xsl:choose>
    		<xsl:when test="$sortBy='0'">
    			<xsl:apply-templates select="//jobs/joblist/item" mode="format">
    				<xsl:sort select="occupation/value" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/year" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/month" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/day" data-type="number" order="ascending"/>
    				<xsl:sort select="../../unitInformation/@unitName" data-type="text" order="ascending"/>
    			</xsl:apply-templates>
    		</xsl:when>
    		<xsl:when test="$sortBy='arbeitsbeginn'">
    			<xsl:apply-templates select="//jobs/joblist/item" mode="format">
    				<xsl:sort select="workbegin/year" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/month" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/day" data-type="number" order="ascending"/>
    				<xsl:sort select="occupation/value" data-type="number" order="ascending"/>
    				<xsl:sort select="../../unitInformation/@unitName" data-type="text" order="ascending"/>
    			</xsl:apply-templates>
    		</xsl:when>
    		<xsl:when test="$sortBy='berufsgruppe'">
    			<xsl:apply-templates select="//jobs/joblist/item" mode="format">
    				<xsl:sort select="occupation/value" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/year" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/month" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/day" data-type="number" order="ascending"/>
    				<xsl:sort select="../../unitInformation/@unitName" data-type="text" order="ascending"/>
    			</xsl:apply-templates>
    		</xsl:when>
    		<xsl:when test="$sortBy='einrichtung'">
    			<xsl:apply-templates select="//jobs/joblist/item" mode="format">
    				<xsl:sort select="../../unitInformation/@unitName" data-type="text" order="ascending"/>
    				<xsl:sort select="workbegin/year" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/month" data-type="number" order="ascending"/>
    				<xsl:sort select="workbegin/day" data-type="number" order="ascending"/>
    				<xsl:sort select="occupation/value" data-type="number" order="ascending"/>
    			</xsl:apply-templates>
    		</xsl:when>
    	</xsl:choose>
    </xsl:template>

</xsl:stylesheet> 