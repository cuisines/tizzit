<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
 
	<xsl:param name="newsNr" select="0"/>
	<xsl:param name="jobNr" select="0"/>
	<xsl:param name="currentDate" select="''"/>
	<xsl:param name="liveserver" select="''"/>


	<xsl:variable name="day">
		<xsl:value-of select="number(substring($currentDate,1,2))"/>
	</xsl:variable>
	<xsl:variable name="month">
		<xsl:value-of select="number(substring($currentDate,4,2))"/>
	</xsl:variable>
	<xsl:variable name="year">
		<xsl:value-of select="number(substring($currentDate,7,4))"/>
	</xsl:variable>


	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="firstPageMaster" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="4cm" margin-left="2.5cm" margin-right="2.5cm">
					<fo:region-before extent="2.5cm"/>
					<fo:region-body margin-top="2.5cm" margin-bottom="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="restPageMaster" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="4cm" margin-left="2.5cm" margin-right="2.5cm">
					<fo:region-before extent="2.5cm"/>
					<fo:region-body margin-top="1.5cm" margin-bottom="1cm"/>
					<fo:region-after extent="1cm"/>
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="all">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference master-reference="firstPageMaster" page-position="first"/>
						<fo:conditional-page-master-reference master-reference="restPageMaster" page-position="rest"/>
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="all">
				<fo:static-content flow-name="xsl-region-before" />
				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="right" font-size="10pt" font-family="serif" line-height="14pt" padding-top="5pt" space-after="20pt">Seite <fo:page-number/></fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!--Titel-->
	<xsl:template match="title">
		<fo:block font-size="18pt" space-before.optimum="0pt" space-after="9pt" text-align="left" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="all">
		<fo:block font-size="12pt" space-before.optimum="0pt" text-align="left">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<!--	Textblock-->
	<xsl:template match="text">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<!--	Zeilenumbruch -->
	<xsl:template match="br">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<!--	Absatz -->
	<xsl:template match="p" >
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left">
		    <xsl:apply-templates/>
		</fo:block>
	</xsl:template>	
	<!--	fette Schrift -->
	<xsl:template match="b | strong | subline  | subtextline | textline">
		<fo:inline font-size="12pt" font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<!--	Bilder-->
	<xsl:template match="image">
		<fo:block space-before="9pt" space-after="9pt">
		<xsl:choose>
			<xsl:when test="@type='left'">
				<fo:block text-align="left">					
					<fo:external-graphic>
						<!-- <xsl:attribute name="src"><xsl:value-of select="$contextRoot"/>cms/img/ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute> -->
						<!--<xsl:attribute name="src"><xsl:value-of select="$contextRoot"/>ukw/img/ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute>-->
					</fo:external-graphic>
					<xsl:choose>
						<xsl:when test=".!=''">
							<fo:block padding-top="1pt">
								<xsl:value-of select="." />
							</fo:block>
						</xsl:when>
					</xsl:choose>
				</fo:block>
			</xsl:when>
			<xsl:when test="@type='center'">
				<fo:block text-align="center">					
					<fo:external-graphic>
						<!-- <xsl:attribute name="src">http://localhost:/cms/img/ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute> -->
						<!--<xsl:attribute name="src"><xsl:value-of select="$contextRoot"/>ukw/img/ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute>-->
					</fo:external-graphic>
					<xsl:choose>
						<xsl:when test=".!=''">
							<fo:block padding-top="1pt">
								<xsl:value-of select="." />
							</fo:block>
						</xsl:when>
					</xsl:choose>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<fo:block text-align="left">
					<fo:external-graphic>
						<!-- <xsl:attribute name="src">http://localhost:/cms/img/ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute> -->
						<!--<xsl:attribute name="src"><xsl:value-of select="$contextRoot"/><xsl:value-of select="$globalImgDir"/>ejbimage?id=<xsl:value-of select="@src"/>&amp;typ=s</xsl:attribute>-->
					</fo:external-graphic>
					<xsl:choose>
						<xsl:when test=".!=''">
							<fo:block padding-top="1pt">
								<xsl:value-of select="." />
							</fo:block>
						</xsl:when>
					</xsl:choose>
				</fo:block>
			</xsl:otherwise>
		</xsl:choose>
		</fo:block>
	</xsl:template>
	<!--	unnumerierte Liste -->
	<xsl:template match="ul">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
		<fo:list-block padding-start="20pt">
			<xsl:apply-templates mode="ul"/>
		</fo:list-block>
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<xsl:template match="li" mode="ul">
		<fo:list-item>
			<fo:list-item-label>
				<!--<fo:block>
					<fo:external-graphic src="url('http://localhost:/ukw/httpd/img/linkpfeil.gif')"/>
				</fo:block>-->
				<fo:block>•</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="13pt" font-size="12pt" space-before.optimum="19pt" text-align="left">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<!--	numerierte Liste -->
	<xsl:template match="ol">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
		<fo:list-block>
			<xsl:apply-templates mode="ol"/>
		</fo:list-block>
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<xsl:template match="li" mode="ol">
		<fo:list-item>
			<fo:list-item-label>
				<fo:block>
					<xsl:number level="single" count="li"/>.</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="19pt" font-size="12pt" space-before.optimum="19pt" text-align="left">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<!--	Tabellen -->
	<xsl:template match="table">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
		<fo:table border-width="0.5pt" border-color="grey">
			<xsl:apply-templates select="tr[1]" mode="column"/>
			<fo:table-body border-width="0.5pt" border-color="grey">
				<xsl:apply-templates select="tr"/>
			</fo:table-body>
		</fo:table>
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<xsl:template match="td">
		<fo:table-cell border="0.5pt" border-color="grey" border-style="solid">
			<fo:block margin-left="5pt" margin-right="5pt" padding-top="5pt" padding-bottom="5pt">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="th">
		<fo:table-cell border-width="0.5pt" border-color="grey" border-style="solid" background-color="grey">
			<fo:block color="white" font-weight="bold" margin-left="5pt" margin-right="5pt" padding-top="5pt" padding-bottom="5pt">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="tr" mode="column">
		<xsl:apply-templates mode="column"/>
	</xsl:template>
	<xsl:template match="td | th" mode="column">
		<fo:table-column>
			<xsl:choose>
				<xsl:when test="count(../*)=5">
					<xsl:attribute name="column-width">3cm</xsl:attribute>
				</xsl:when>
				<xsl:when test="count(../*)=4">
					<xsl:attribute name="column-width">4cm</xsl:attribute>
				</xsl:when>
				<xsl:when test="count(../*)=3">
					<xsl:attribute name="column-width">5cm</xsl:attribute>
				</xsl:when>
				<xsl:when test="count(../*)=2">
					<xsl:attribute name="column-width">7cm</xsl:attribute>
				</xsl:when>
				<xsl:when test="count(../*)=1">
					<xsl:attribute name="column-width">15cm</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="column-width">3cm</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</fo:table-column>
	</xsl:template>
	<xsl:template match="tr">
		<fo:table-row>
			<xsl:apply-templates/>
		</fo:table-row>
	</xsl:template>
	<!-- Literaturangabe -->
	<xsl:template match="literaturangabe">
		<fo:block>			
			<xsl:apply-templates select="literatur-zitat" /> <xsl:call-template name="xsl-literaturquelle"/>
		</fo:block>
	</xsl:template>

	<xsl:template name="xsl-literaturquelle">
		<xsl:choose>
			<xsl:when test="literatur-author!='' and literatur-year!=''">
				(<xsl:value-of select="literatur-author" />, <xsl:value-of select="literatur-year" />)
			</xsl:when>
		</xsl:choose>		
	</xsl:template>

	<xsl:template match="literatur-zitat">
		<xsl:choose>
			<xsl:when test="@quotEnabled='true'">
				&quot;<xsl:value-of select="." />&quot;
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="." />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--	<xsl:template match="table">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
		<fo:table border-width="0.5pt" border-color="grey">
			<xsl:apply-templates select="tr" mode="column"/>
			<fo:table-body border-width="0.5pt" border-color="grey" border-style="solid">
				<xsl:apply-templates select="tr"/>
			</fo:table-body>
		</fo:table>
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<xsl:template match="td">
		<fo:table-cell border="0.5pt" border-color="grey" border-style="solid">
			<fo:block margin-left="5pt" margin-right="5pt" margin-top="5pt" margin-bottom="5pt">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="th">
	</xsl:template>
	<xsl:template match="tr" mode="column" border-style="solid">
		<fo:table-column column-width="4cm"/>
	</xsl:template>
	<xsl:template match="tr">
		<fo:table-row border-width="1pt" border-color="grey" border-style="solid">
			<xsl:apply-templates/>
		</fo:table-row>
	</xsl:template>-->
	<!--		<xsl:template match="table">
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
		<fo:table border-width="0.5pt" border-color="grey">
			<xsl:apply-templates select="tr" mode="column"/>
			<fo:table-body border-width="0.5pt" border-color="green">
				<xsl:apply-templates select="tr"/>
			</fo:table-body>
		</fo:table>
		<fo:block font-size="12pt" space-before.optimum="19pt" text-align="left"/>
	</xsl:template>
	<xsl:template match="td">
		<fo:table-cell border="0.5pt" border-color="grey" border-style="solid" table-layout="auto">
			<fo:block margin-left="5pt" margin-right="5pt" margin-top="5pt" margin-bottom="5pt">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="th">
		<fo:table-cell border-width="0.5pt" border-color="grey" border-style="solid">
			<fo:block color="white" font-weight="bold" background-color="grey" padding="2pt">
				<xsl:apply-templates/>
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="tr" mode="column">
		<fo:table-column column-width="4cm">
			<xsl:apply-templates/>
		</fo:table-column>
	</xsl:template>
	<xsl:template match="tr">
		<fo:table-row border-width="1px" border-color="grey" border-style="solid">
			<xsl:apply-templates/>
		</fo:table-row>
	</xsl:template>-->
	<!--===============DB Komponententen ================-->
	<!--=========== Beginn Templates fuer includes| unit, department, person, address, talkTime ===============-->
	<xsl:template match="include[@type='unit']">
		<fo:block space-after="20px">
			<xsl:apply-templates select="content/uebergeordnete_einrichtung"/>
			<xsl:apply-templates select="content/name"/>
			<xsl:apply-templates select="include[@type='department']"/>
			<xsl:apply-templates select="include[@type='address']"/>
			<xsl:apply-templates select="include[@type='talkTime']"/>
			<xsl:apply-templates select="include[@type='person']"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="include[@type='department']">
		<fo:block space-after="20px">
			<xsl:apply-templates select="content/name"/>
			<xsl:apply-templates select="include[@type='address']"/>
			<xsl:apply-templates select="include[@type='talkTime']"/>
			<xsl:apply-templates select="include[@type='person']"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="include[@type='person'][@listtype='Fliesstext' or @listtype='flow']">
			<xsl:apply-templates select="content/lastname" mode="fliesstext"/>
			<xsl:apply-templates select="content/abteilung" mode="fliesstext"/>
			<xsl:apply-templates select="content/job" mode="fliesstext"/>
			<xsl:apply-templates select="content/birthDay" mode="fliesstext"/>
			<xsl:apply-templates select="include[@type='address']" mode="fliesstext"/>
			<xsl:apply-templates select="include[@type='talkTime']" mode="fliesstext"/>
	</xsl:template>

	<xsl:template match="include[@type='person'][@listtype=''] | include[@type='person'][@listtype='Normal' or @listtype='normal'] | include[@type='person'][@listtype='Listenform' or @listtype='list'] ">
		
		              <fo:table>
					  <fo:table-column column-width="400px"/>
                    <fo:table-body>
                         <fo:table-row keep-together="always">
                              <fo:table-cell>
							  <fo:block space-after="20px" >
			<xsl:apply-templates select="content/lastname"/>
			<xsl:apply-templates select="content/abteilung"/>
			<xsl:apply-templates select="content/job"/>
			<xsl:apply-templates select="include[@type='address']"/>
			<xsl:apply-templates select="include[@type='talkTime']"/>
			<xsl:apply-templates select="content/image" mode="person"/>
			</fo:block>
			</fo:table-cell>
			</fo:table-row>
			</fo:table-body>
			</fo:table>		
	</xsl:template>




	<xsl:template match="include[@type='address']">
		<xsl:choose>
			<xsl:when test="postOfficeBox">
				<fo:block font-weight="bold">
					Postanschrift
				</fo:block>
			</xsl:when>
		</xsl:choose>
		<xsl:apply-templates select="content/buildingNr"/>
		<xsl:apply-templates select="content/buildingLevel"/>
		<xsl:apply-templates select="content/roomNr"/>
		<xsl:apply-templates select="content/street"/>
		<xsl:apply-templates select="content/postOfficeBox"/>
		<xsl:apply-templates select="content/city"/>
		<xsl:apply-templates select="content/country"/>
		<xsl:apply-templates select="content/phone1"/>
		<xsl:apply-templates select="content/phone2"/>
		<xsl:apply-templates select="content/mobilePhone"/>
		<xsl:apply-templates select="content/fax"/>
		<xsl:apply-templates select="content/email"/>
		<xsl:apply-templates select="content/homepage"/>
		<xsl:apply-templates select="content/misc"/>
	</xsl:template>
	<xsl:template match="include[@type='talkTime']">
		<xsl:apply-templates select="content" mode="talkTime"/>
	</xsl:template>
	<xsl:template match="content" mode="talkTime">
		<xsl:apply-templates select="talkTimeType"/>
		<xsl:apply-templates select="talkTimes"/>
	</xsl:template>
	<xsl:template match="talkTimes">
		<xsl:apply-templates select="times" mode="times"/>
	</xsl:template>
	<xsl:template match="times" mode="times">
		<xsl:apply-templates select="time"/>
	</xsl:template>
	<!--=========== Beginn Templates fuer einzelne Attribute der DB Komponenten ===============-->
	<!-- mit anschliessendem Zeilenumbruch-->
	<xsl:template match="info | misc | job | homepage | zipcode-ansch ">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- fett mit anschliessendem Zeilenumbruch-->
	<xsl:template match="talkTimeType | time | uebergeordnete_einrichtung | country | name">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block font-weight="bold">
					<xsl:apply-templates/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!--fett in der Zeile mit anschliessendem Leerzeichen-->
	<!--	countryCode | zipcode-->
	<xsl:template match="city">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>
					<xsl:if test="../countryCode!=''">
						<fo:inline padding-after="12pt" font-weight="bold">
							<xsl:value-of select=" ../countryCode"/>-</fo:inline>
					</xsl:if>
					<xsl:if test="../zipcode">
						<fo:inline padding-after="12pt" font-weight="bold">
							<xsl:value-of select="../zipcode"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:if test="../zipCode">
						<fo:inline padding-after="12pt" font-weight="bold">
							<xsl:value-of select="../zipCode"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="postOfficeBox">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>Postfach: <xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="street">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>
					<xsl:value-of select="."/>&#160;
					<fo:inline>
						<xsl:value-of select="../streetNr"/>
					</fo:inline>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="lastname">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block font-weight="bold">
					<xsl:if test="../salutation!=''">
						<fo:inline space-after="12pt">
							<xsl:value-of select="../salutation"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:if test="../title!=''">
						<fo:inline>
							<xsl:value-of select="../title"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:if test="../firstname!=''">
						<fo:inline>
							<xsl:value-of select="../firstname"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="buildingNr">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>Raumnr.:&#160;<xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="buildingLevel">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>Ebene:&#160;<xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="roomNr">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>Zimmer-Nr.:&#160;<xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="phone1 | phone2">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block font-weight="bold">Tel.: <xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="mobilePhone">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block font-weight="bold">Mobil: <xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="fax">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>Fax: <xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="email">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:block>E-Mail: <xsl:value-of select="."/>
				</fo:block>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="stickyPad">
	</xsl:template>
	<xsl:template match="dcfinformation">
	</xsl:template>


	<xsl:template match="notice">
	</xsl:template>

	
	<xsl:template match="publikation-komp">
	<fo:block space-after="20px">
		<xsl:apply-templates select="pubhead"/>		
		<xsl:apply-templates select="pubsubhead"/>
		<xsl:apply-templates select="autor"/>
		<xsl:apply-templates select="inhalt"/>
		<xsl:apply-templates select="veroeffentlicht-in"/>
		<xsl:apply-templates select="pubtel"/>
		<xsl:apply-templates select="pubemail"/>
		<xsl:apply-templates select="pubhomepage"/>
		<xsl:apply-templates select="pubsonstiges"/>
		</fo:block>
	</xsl:template>


	<xsl:template match="pubhead">
		<fo:block font-size="12pt" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="pubsubhead">
		<fo:block font-size="12pt" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="autor">
		<fo:block font-size="12pt" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="inhalt">
		<fo:block font-size="12pt" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="veroeffentlicht-in">
		<fo:block font-size="12pt" font-weight="normal">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="pubtel">
		<xsl:if test=".!=''">
		<fo:block font-size="12pt" font-weight="bold">
			Tel.:&#160;<xsl:apply-templates/>
		</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="pubemail">
	<xsl:if test=".!=''">
		<fo:block font-size="12pt" font-weight="bold">
		E-Mail:&#160;<xsl:apply-templates/>
		</fo:block>
	</xsl:if>
	</xsl:template>

	<xsl:template match="pubhomepage">
		<fo:block font-size="12pt" font-weight="normal">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="pubsonstiges">
		<fo:block font-size="12pt" font-weight="normal">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<!--Templates DB Komponenten Fliesstext-->

		<xsl:template match="lastname" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline font-weight="bold">
					<xsl:if test="../salutation!=''">
						<fo:inline space-after="12pt">
							<xsl:value-of select="../salutation"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:if test="../title!=''">
						<fo:inline>
							<xsl:value-of select="../title"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:if test="../firstname!=''">
						<fo:inline>
							<xsl:value-of select="../firstname"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="info | misc  | homepage | zipcode-ansch " mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>
					<xsl:apply-templates/>
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

<!--Fliesstext mit Komma-->
	<xsl:template match="birthDay | job | homepage" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>
					<xsl:apply-templates/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

		<xsl:template match="include[@type='address']" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="postOfficeBox!=''">
				<fo:block font-weight="bold">
					Postanschrift
				</fo:block>
			</xsl:when>
		</xsl:choose>
		<xsl:apply-templates select="content/buildingNr" mode="fliesstext"/>
		<xsl:apply-templates select="content/buildingLevel" mode="fliesstext"/>
		<xsl:apply-templates select="content/roomNr" mode="fliesstext"/>
		<xsl:apply-templates select="content/street" mode="fliesstext"/>
		<xsl:apply-templates select="content/postOfficeBox" mode="fliesstext"/>
		<xsl:apply-templates select="content/city" mode="fliesstext"/>
		<xsl:apply-templates select="content/country" mode="fliesstext"/>
		<xsl:apply-templates select="content/phone1" mode="fliesstext"/>
		<xsl:apply-templates select="content/phone2" mode="fliesstext"/>
		<xsl:apply-templates select="content/mobilePhone" mode="fliesstext"/>
		<xsl:apply-templates select="content/fax" mode="fliesstext"/>
		<xsl:apply-templates select="content/email" mode="fliesstext"/>
		<xsl:apply-templates select="content/homepage" mode="fliesstext"/>
		<xsl:apply-templates select="content/misc" mode="fliesstext"/>
	</xsl:template>

<xsl:template match="street" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline	>
					<xsl:value-of select="."/>&#160;
					<fo:inline>
						<xsl:value-of select="../streetNr" />,&#160;
					</fo:inline>
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="buildingNr" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>Raumnr.:&#160;<xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="buildingLevel" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>Ebene:&#160;<xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="roomNr" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>Zimmer-Nr.:&#160;<xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="phone1 | phone2" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline font-weight="bold">Tel.: <xsl:value-of select="."/>,
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="mobilePhone" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline font-weight="bold">Mobil: <xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="fax" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>Fax: <xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="email" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>E-Mail: <xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

<xsl:template match="city" mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>
					<xsl:if test="../countryCode!=''">
						<fo:inline padding-after="12pt" font-weight="bold">
							<xsl:value-of select=" ../countryCode"/>-</fo:inline>
					</xsl:if>
					<xsl:if test="../zipcode">
						<fo:inline padding-after="12pt" font-weight="bold">
							<xsl:value-of select="../zipcode"/>&#160;
						</fo:inline>
					</xsl:if>
					<xsl:value-of select="."/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="postOfficeBox">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline>Postfach: <xsl:value-of select="."/>
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="include[@type='talkTime']" mode="fliesstext">
		<xsl:apply-templates select="content" mode="fliesstext"/>
	</xsl:template>

	<xsl:template match="talkTimeType | time | uebergeordnete_einrichtung | country | name " mode="fliesstext">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<fo:inline font-weight="bold">
					<xsl:apply-templates/>,&#160;
				</fo:inline>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

<!--TEmplates fuer Teamliste-->


<xsl:template match="unitMembersList | membersList">
	<xsl:apply-templates select="person">		
		<xsl:sort select="position" data-type="number" order="ascending"  lang="de"/>
		<xsl:sort select="lastname" data-type="text" order="ascending" lang="de"/>
	</xsl:apply-templates>
</xsl:template>


<!--Teamliste nach neuer Datenstruktur, 10.6.04, Hato-->
<xsl:template match="person">
              <fo:table>
					  <fo:table-column column-width="400px"/>
                    <fo:table-body>
                         <fo:table-row keep-together="always">
                              <fo:table-cell>
							  <fo:block space-after="20px" >

			<xsl:apply-templates select="lastname"/>
			<xsl:apply-templates select="abteilung"/>
			<xsl:apply-templates select="job"/>
			
			<!--Bei Adressen in Teamkontaktlisste immer erst die Sekretariatsadresse und dann die Bueroadresse-->
			<xsl:apply-templates select="address[addressType='Sekretariat']"/>			
			<xsl:apply-templates select="address[addressType='Büro' or addressType='null' or addressType='']" />
			<xsl:apply-templates select="talkTime"/>
			<xsl:if test="@imageid!='0'">
				<xsl:apply-templates select="@imageid" mode="person"/>
			</xsl:if>
			</fo:block>
			</fo:table-cell>
			</fo:table-row>
			</fo:table-body>
			</fo:table>		
	</xsl:template>

	<!--	Bilder in Teamliste und bei Personen-->
	<xsl:template match="@imageid" mode="person">
	<xsl:if test=".!='0'">
		<fo:block>
			<fo:external-graphic>
				<xsl:attribute name="src">http://localhost:/cms/img/ejbimage?id=<xsl:value-of select="."/>&amp;typ=s</xsl:attribute>
				<xsl:attribute name="width">80px</xsl:attribute>
				<xsl:attribute name="scale">uniform</xsl:attribute>
			</fo:external-graphic>
		</fo:block>
	</xsl:if>
	</xsl:template>

	<xsl:template match="address">
		<fo:block space-after="10px">
			<xsl:choose>
				<xsl:when test="postOfficeBox!=''">
					<fo:block font-weight="bold">
						Postanschrift
					</fo:block>
				</xsl:when>
			</xsl:choose>
			<xsl:apply-templates select="buildingNr"/>
			<xsl:apply-templates select="buildingLevel"/>
			<xsl:apply-templates select="roomNr"/>
			<xsl:apply-templates select="street"/>
			<xsl:apply-templates select="postOfficeBox"/>
			<xsl:apply-templates select="city"/>
			<xsl:apply-templates select="country"/>
			<xsl:apply-templates select="phone1"/>
			<xsl:apply-templates select="phone2"/>
			<xsl:apply-templates select="mobilePhone"/>
			<xsl:apply-templates select="fax"/>
			<xsl:apply-templates select="email"/>
			<xsl:apply-templates select="homepage"/>
			<xsl:apply-templates select="misc"/>
		</fo:block>
	</xsl:template>

<!--Templates fuer news-->

<xsl:template match="news">
<!--CurrentDate:<xsl:value-of select="$currentDate"/>Liverserver=<xsl:value-of select="$liveserver"/>newsNr=<xsl:value-of select="$newsNr"/>-->
	<xsl:choose>
		<xsl:when test="$newsNr!=0">
			<xsl:apply-templates select="newslist/item[@timestamp=$newsNr]" mode="deploycheck"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="newslist" mode="deploycheck"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="jobs">
<!--CurrentDate:<xsl:value-of select="$currentDate"/>Liverserver=<xsl:value-of select="$liveserver"/>newsNr=<xsl:value-of select="$newsNr"/>-->
	<xsl:choose>
		<xsl:when test="$jobNr!=0">
			<xsl:apply-templates select="joblist/item[@timestamp=$jobNr]" mode="deploycheck"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="joblist" mode="deploycheck"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="item" mode="format2">
<fo:block>
<xsl:if test="following-sibling::* and $newsNr=0 and $jobNr=0"><xsl:attribute name="break-after">page</xsl:attribute></xsl:if>
	<xsl:apply-templates />
</fo:block>
</xsl:template>

	<xsl:template match="jobname">
		<fo:block font-size="15pt" font-weight="bold" space-after="19pt" space-before.optimum="19pt">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="newsname">
		<fo:block font-size="15pt" font-weight="bold" space-after="19pt" space-before.optimum="19pt">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="newslocality | newsdate | newsdate-info">
		<fo:block font-size="12pt" font-weight="normal">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="workbegin">
		<xsl:if test="day!=''">
			<fo:block font-size="12pt" font-weight="normal">
				Arbeitsbeginn: <xsl:apply-templates/>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="application-deadline">
		<xsl:if test="day!=''">
			<fo:block font-size="12pt" font-weight="normal">
				Bewerbungsfrist: <xsl:apply-templates/>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<xsl:template match="occupation">
		<xsl:if test="value!=''">
			<fo:block font-size="12pt" font-weight="normal">
				Berufsgrupppe: <xsl:apply-templates select="." mode="resolve"/>
			</fo:block>
		</xsl:if>
	</xsl:template>


<xsl:template match="day | month">
	<xsl:apply-templates/>.
</xsl:template>

<xsl:template match="year">
	<xsl:apply-templates />
</xsl:template>


	<!--Hier werden auf dem Live Server die News ausgesucht, deren online start Zeit gegeben ist. In Integration werden alle dargestellt
-->
	<xsl:template match="item" mode="deploycheck">
		<xsl:if test="newsname!='' or jobname!=''">
			<xsl:choose>
			<!--if it's the Live Server-->
				<xsl:when test="$liveserver='true'">
					<xsl:choose>
					<!--if there's no start date-->
					<xsl:when test="deploy-start-stop/start/year='' or not(deploy-start-stop)">
					<!--<xsl:when test="not(deploy-start-stop)">-->
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
										<xsl:when test="$day &gt;= deploy-start-stop/start/day"><xsl:apply-templates select="." mode="checkStopTime"/>
										</xsl:when>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="$month &gt; deploy-start-stop/start/month"><xsl:apply-templates select="." mode="checkStopTime"/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="$year &gt; deploy-start-stop/start/year"><xsl:apply-templates select="." mode="checkStopTime"/>
						</xsl:when>
					</xsl:choose>
					</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<!--	If it's Integration Area show all the items-->
				<xsl:otherwise><xsl:apply-templates select="." mode="format2"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if></xsl:template>
	<!--Hier werden auf dem Live Server die news ausgesucht, deren online stop Zeit noch nicht gekommen ist. In Integration werden alle dargestellt
-->
	<xsl:template match="item" mode="checkStopTime">
		<!--If there's no stop date-->
		<xsl:choose>
			<xsl:when test="deploy-start-stop/stop/year='' or not(deploy-start-stop)">
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

		<xsl:template match="occupation" mode="resolve">
		<xsl:choose>
			<xsl:when test="value=1">Ärztlicher Dienst</xsl:when>
			<xsl:when test="value=2">Pflegedienst</xsl:when>
			<xsl:when test="value=3">Studierende</xsl:when>
			<xsl:when test="value=4">Wissenschaftlicher Mitarbeiter</xsl:when>
			<xsl:when test="value=5">Funktionsdienst</xsl:when>
			<xsl:when test="value=6">MTA</xsl:when>
			<xsl:when test="value=7">MTR</xsl:when>
			<xsl:when test="value=8">Verwaltung</xsl:when>
			<xsl:when test="value=9">EDV</xsl:when>
			<xsl:otherwise>Sonstige</xsl:otherwise>
		</xsl:choose>
	</xsl:template>





<!--Templates mit Leerausgabe-->

<xsl:template match="sortOrder"></xsl:template>
<xsl:template match="metaInformation"></xsl:template>
<xsl:template match="unitInformation"></xsl:template>
<xsl:template match="deploy-start-stop"></xsl:template>
<xsl:template match="menue"></xsl:template>
<xsl:template match="submenue"></xsl:template>
<xsl:template match="top-bottom"></xsl:template>
<xsl:template match="breadcrumbs"></xsl:template>
<xsl:template match="colorsection"></xsl:template>
<xsl:template match="next"></xsl:template>
<xsl:template match="info"></xsl:template>

</xsl:stylesheet>