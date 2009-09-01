<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- 	Dieses Template ist das gleiche wie das Stellenangebot-template, au�er dass die
		Parameter aus der variables.xsl kommen sollten.
		Folgende Parameter m�ssen lokal vorhanden sein:
		
		language
		url
		liveserver
		anchor
		print
		
		 -->
	<xsl:param name="sort" select="0"/>
	<xsl:param name="sortBy" select="0"/>
	<xsl:param name="jobNr" select="0"/>

	
	<xsl:param name="currentDate" select="''"/>

	<!--day, month and year of the currentDate-->
	<xsl:variable name="day">
		<xsl:value-of select="number(substring($currentDate,1,2))"/>
	</xsl:variable>
	<xsl:variable name="month">
		<xsl:value-of select="number(substring($currentDate,4,2))"/>
	</xsl:variable>
	<xsl:variable name="year">
		<xsl:value-of select="number(substring($currentDate,7,4))"/>
	</xsl:variable>

	<xsl:template match="content" mode="format" priority="1.1">
	    <xsl:choose>
	        <xsl:when test="$jobNr != 0">
	            <xsl:apply-templates select="jobs" mode="jobDetail" />	        
	        </xsl:when>
	        <xsl:otherwise>
	            <xsl:apply-templates select="jobs" mode="jobList" />
	        </xsl:otherwise>
	    </xsl:choose>
	</xsl:template>

    <xsl:template match="jobs" mode="jobList">
        <xsl:choose>
			<xsl:when test="//jobs/joblist//item/jobname!=''">
				<!-- stellenangebote - tabellarische uebersicht -->
				<table cellspacing="0" cellpadding="6" border="0" style="border-collapse:collapse;">
					<tr>
						<th style="border: 1px solid #888888" valign="top" width="25%">Stellenbezeichung</th>
						<th style="border: 1px solid #888888" valign="top" width="25%">
							<a>
								<xsl:attribute name="href">page.html?sortBy=arbeitsbeginn</xsl:attribute>
								<span>Arbeitsbeginn</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="25%">
							<a>
								<xsl:attribute name="href">page.html?sortBy=berufsgruppe</xsl:attribute>
								<span>Berufsgruppe</span>
							</a>
						</th>
						<th style="border: 1px solid #888888" valign="top" width="25%">
							<a>
								<xsl:attribute name="href">page.html?sortBy=einrichtung</xsl:attribute>
								<span>Klinik</span>
							</a>
						</th>
					</tr>
					<!--Sortierung der Eintraege-->
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
				</table>
			</xsl:when>
			<xsl:otherwise>Zurzeit liegen leider keine Stellenangebote vor. <br/><br/>Bitte versuchen Sie es zu einem sp&#228;teren Zeitpunkt wieder.</xsl:otherwise>
		</xsl:choose>
    </xsl:template>

	<!--Hier werden auf dem Live Server die Stellenangebote ausgesucht, deren
	    online start Zeit gegeben ist. In Integration werden alle dargestellt
    -->
	<xsl:template match="item" mode="format">

		<xsl:if test="jobname!=''">
			<xsl:choose>
				<!--if it's the Live Server-->
				<xsl:when test="$liveserver='true'">
					<xsl:choose>
						<!--if there's no start date-->
						<xsl:when test="deploy-start-stop/start/year=''">
							<xsl:apply-templates select="." mode="checkStopTime"/>
						</xsl:when>
						<xsl:otherwise>
							<!--	Test the Year -->
							<xsl:choose>
								<xsl:when test="$year = deploy-start-stop/start/year">
									<!--Test the month-->
									<xsl:choose>
										<xsl:when test="$month = deploy-start-stop/start/month">
											<!--Test the day-->
											<xsl:choose>
												<xsl:when test="$day &gt;= deploy-start-stop/start/day">
													<xsl:apply-templates select="." mode="checkStopTime"/>
												</xsl:when>
											</xsl:choose>
										</xsl:when>
										<xsl:when test="$month &gt; deploy-start-stop/start/month">
											<xsl:apply-templates select="." mode="checkStopTime"/>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$year &gt; deploy-start-stop/start/year">
									<xsl:apply-templates select="." mode="checkStopTime"/>
								</xsl:when>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!--	If it's Integration Area show all the items-->
				<xsl:otherwise>
					<xsl:apply-templates select="." mode="format2"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!--Hier werden auf dem Live Server die Stellenangebote ausgesucht, deren online stop Zeit noch nicht gekommen ist. In Integration werden alle dargestellt
-->
	<xsl:template match="item" mode="checkStopTime">
		<!--If there's no stop date-->
		<xsl:choose>
			<xsl:when test="deploy-start-stop/stop/year=''">
				<xsl:apply-templates select="." mode="format2"/>
			</xsl:when>
			<xsl:otherwise>
				<!--	Test the Year -->
				<xsl:choose>
					<xsl:when test="$year = deploy-start-stop/stop/year">
						<!--Test the month-->
						<xsl:choose>
							<xsl:when test="$month = deploy-start-stop/stop/month">
								<!--Test the day-->
								<xsl:choose>
									<xsl:when test="$day &lt;= deploy-start-stop/stop/day">
										<xsl:apply-templates select="." mode="format2"/>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="$month &lt; deploy-start-stop/stop/month">
								<xsl:apply-templates select="." mode="format2"/>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="$year &lt; deploy-start-stop/stop/year">
						<xsl:apply-templates select="." mode="format2"/>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- JOBOVERVIEW: Hier wird die Tabelle fuer die jobuebersicht geschrieben-->
	<xsl:template match="item" mode="format2">
		<tr>
			<td style="border: 1px solid #888888" valign="top">
				<a>
					<xsl:attribute name="href">page.html?sortBy=<xsl:value-of select="$sortBy"/>&amp;jobNr=<xsl:value-of select="@timestamp"/></xsl:attribute>
					<xsl:apply-templates select="jobname" mode="format"/>
				</a>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:if test="workbegin!=''">
					<xsl:apply-templates select="workbegin" mode="format"/>
				</xsl:if>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:apply-templates select="occupation" mode="resolve"/>
			</td>
			<td style="border: 1px solid #888888" valign="top">
				<xsl:apply-templates select="../../unitInformation/@unitName"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =================== Anfang templates fuer singleJob ========================-->
	<!-- Anfang templates fuer singleJob-->
	<xsl:template match="jobs" mode="jobDetail">
		<xsl:apply-templates select="joblist" mode="jobDetail"/>
	</xsl:template>
	<!-- Das job mit der richtigen Nummer finden-->
	<xsl:template match="joblist" mode="jobDetail">
		<xsl:apply-templates select="item[@timestamp = $jobNr]" mode="jobDetail"/>
	</xsl:template>
	<!-- Die Tabelle darstellen mit den jobsinformationen-->
	<xsl:template match="item" mode="jobDetail">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top">Stellenbezeichnung:</td>
				<td width="19">
				</td>
				<td valign="top">
					<b>
						<xsl:apply-templates select="jobname" mode="format"/>
					</b>
					<br/>
					<br/>
				</td>
			</tr>
			<xsl:if test="../../unitInformation/@unitName!=''">
			<tr>
				<td valign="top">Klinik:</td>
				<td width="19">
				</td>
				<td valign="top">
					<b>
						<xsl:apply-templates select="../../unitInformation/@unitName"/>
					</b>
					<br/>
					<br/>
				</td>
			</tr>
			</xsl:if>
			<tr>
				<td valign="top">Berufsgruppe:</td>
				<td width="19">
				</td>
				<td valign="top">
					<xsl:apply-templates select="occupation" mode="resolve"/>
					<br/>
					<br/>
				</td>
			</tr>
			<xsl:if test="workbegin!=''">
				<tr>
					<td valign="top">Arbeitsbeginn:</td>
					<td width="19">
					</td>
					<td valign="top">
						<xsl:apply-templates select="workbegin" mode="format"/>
						<br/>
						<br/>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="application-deadline!=''">
				<tr>
					<td valign="top">Bewerbungsfrist:</td>
					<td width="19">
					</td>
					<td valign="top">
						<xsl:apply-templates select="application-deadline" mode="format"/>
						<br/>
						<br/>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td valign="top">Kontakt:</td>
				<td width="19">
				</td>
				<td valign="top">
					<xsl:apply-templates select="aggregation"/>
				</td>
			</tr>
		</table>
		<br/>
		<xsl:apply-templates select="text" mode="format"/>
		<br/>
		<xsl:call-template name="line"/>
		<br/>
		<a>
		<xsl:attribute name="href">page.html?sortBy=<xsl:value-of select="$sortBy"/>&amp;jobNr=0</xsl:attribute>zur&#252;ck</a>
	</xsl:template>

	<!-- ================================ END Templates fuer singleJob====================-->
	
	<!-- templates mit Leerausgabe-->
	<xsl:template match="stickyPad" mode="jobDetail"/>

	<xsl:template match="workbegin" mode="format">
		<xsl:apply-templates mode="format"/>
	</xsl:template>

	<xsl:template match="subtextline1" mode="jobDetail">
		<xsl:apply-templates select="." mode="format"/>&#160;
	</xsl:template>

	<xsl:template match="subtextline2" mode="jobDetail">
		<xsl:apply-templates select="." mode="format"/>&#160;
	</xsl:template>

	<xsl:template match="jobname" mode="format">
		<xsl:value-of select="."/>
	</xsl:template>

    <!-- sollte immer lokal überschrieben werden -->
	<xsl:template match="occupation" mode="resolve">
		<xsl:choose>
			<xsl:when test="value=1">&#196;rztlicher Dienst</xsl:when>
			<xsl:when test="value=2">Pflegedienst</xsl:when>
			<xsl:when test="value=3">Studierende</xsl:when>
			<xsl:when test="value=4">Wissenschaftlicher Mitarbeiter</xsl:when>
			<xsl:when test="value=5">Funktionsdienst</xsl:when>
			<xsl:when test="value=6">MTA</xsl:when>
			<xsl:when test="value=7">MTR</xsl:when>
			<xsl:when test="value=8">Verwaltung</xsl:when>
			<xsl:when test="value=9">EDV</xsl:when>
			<xsl:when test="value=10">Therapeutischer Dienst</xsl:when>
			<xsl:when test="value=11">Rettungsdienst</xsl:when>
			<xsl:otherwise>Sonstige</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
<!--
Sortiert wird nach folgendem ID Schema:

ÃÂ?rztlicher Dienst				1
Pflegedienst					2
Studierende						3
Wissenschaftlicher Mitarbeiter	4
Funktionsdienst					5
MTA								6
MTR								7
Verwaltung						8
EDV								9
Sonstige						10


-->