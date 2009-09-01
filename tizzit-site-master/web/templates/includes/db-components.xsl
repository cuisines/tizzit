<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="aggregation">
		<xsl:apply-templates select="include"/>
	</xsl:template>
	<xsl:template match="aggregation" mode="format">
		<xsl:apply-templates select="include"/>
	</xsl:template>


	<!--	<xsl:template match="unitMembersList" mode="format">
		<xsl:apply-templates select="include">
			<xsl:sort select="@position" data-type="number" order="ascending" lang="de"/>
			<xsl:sort select="content/lastname" data-type="text" order="ascending" lang="de"/>
		</xsl:apply-templates>
	</xsl:template>
-->
	<!--=========== Beginn Templates fuer includes| unit, department, person, address, talkTime ===============-->

	<xsl:template match="include[@type='unit']">
		<xsl:apply-templates select="content/uebergeordnete_einrichtung" mode="uebergeordnete_einrichtung"/>
		<xsl:apply-templates select="content/name" mode="name"/>
		<xsl:apply-templates select="include[@type='department']"/>
		<xsl:apply-templates select="include[@type='address']"/>
		<xsl:apply-templates select="include[@type='talkTime']"/>
		<xsl:apply-templates select="include[@type='person']"/>
<!--		<xsl:apply-templates select="include[@type='person']">
			<xsl:sort select="@position" data-type="number" order="ascending" lang="de"/>
			<xsl:sort select="content/lastname" data-type="text" order="ascending" lang="de"/>
		</xsl:apply-templates>-->
	</xsl:template>

	<xsl:template match="include[@type='department']">
		<xsl:apply-templates select="content/name" mode="name"/>
		<xsl:apply-templates select="include[@type='address']"/>
		<xsl:apply-templates select="include[@type='talkTime']"/>
		<xsl:apply-templates select="include[@type='person']"/>
	</xsl:template>

	<xsl:template match="include[@type='person']">
			<xsl:choose>
				<xsl:when test="@listtype='Normal' or @listtype='normal'">
					<div class="person">
						<xsl:apply-templates select="." mode="normal"/>
					</div>
				</xsl:when>
				<xsl:when test="@listtype='Listenform' or @listtype='list'">
					<xsl:apply-templates select="." mode="listenform"/>
				</xsl:when>
				<xsl:when test="@listtype='Fliesstext' or @listtype='flow'">
					<xsl:apply-templates select="." mode="fliesstext"/>
				</xsl:when>
				<xsl:otherwise>
					<div class="person">
					<xsl:apply-templates select="." mode="normal"/>
					</div>
				</xsl:otherwise>
			</xsl:choose>
	
	</xsl:template>

	<xsl:template match="include[@type='person']" mode="normal">
		<xsl:apply-templates select="roleType" mode="roleType"/>
		<xsl:apply-templates select="content/salutation" mode="salutation"/>
		<xsl:apply-templates select="content/title" mode="title"/>
		<xsl:apply-templates select="content/firstname" mode="firstname"/>
		<xsl:apply-templates select="content/lastname" mode="lastname"/>
		<xsl:apply-templates select="content/abteilung" mode="abteilung"/>
		<xsl:apply-templates select="content/job" mode="job"/>
		<xsl:apply-templates select="content/birthDay" mode="birthDay"/>
		<xsl:apply-templates select="include[@type='address']"/>
		<xsl:apply-templates select="include[@type='talkTime']"/>
		<xsl:apply-templates select="content/jobTitle" mode="jobTitle"/>
		<xsl:apply-templates select="content/countryJob" mode="countryJob"/>
		<xsl:apply-templates select="content/medicalAssociation" mode="medicalAssociation"/>
		<xsl:apply-templates select="content/linkMedicalAssociation" mode="linkMedicalAssociation"/>
	</xsl:template>

	<xsl:template match="include[@type='address']">
		<xsl:choose>
			<xsl:when test="postOfficeBox">
				<b>Postanschrift</b>
				<br/>
			</xsl:when>
		</xsl:choose>
		<xsl:apply-templates select="content/buildingNr" mode="buildingNr"/>
		<xsl:apply-templates select="content/buildingLevel" mode="buildingLevel"/>
		<xsl:apply-templates select="content/roomNr" mode="roomNr"/>
		<xsl:apply-templates select="content/street" mode="street"/>
		<xsl:apply-templates select="content/streetNr" mode="streetNr"/>
		<xsl:apply-templates select="content/postOfficeBox" mode="postOfficeBox"/>
		<xsl:apply-templates select="content/countryCode" mode="countryCode"/>
		<xsl:apply-templates select="content/zipcode" mode="zipcode"/>
		<xsl:apply-templates select="content/city" mode="city"/>
		<xsl:apply-templates select="content/country" mode="country"/>
		<xsl:apply-templates select="content/phone1" mode="phone1"/>
		<xsl:apply-templates select="content/phone2" mode="phone1"/>
		<xsl:apply-templates select="content/mobilePhone" mode="mobilePhone"/>
		<xsl:apply-templates select="content/fax" mode="fax"/>
		<xsl:apply-templates select="content/email" mode="email"/>
		<xsl:apply-templates select="content/homepage" mode="homepage"/>
		<xsl:apply-templates select="content/misc" mode="misc"/>
	</xsl:template>

	<xsl:template match="include[@type='talkTime']">
		<xsl:apply-templates select="content" mode="talkTime"/>
	</xsl:template>

	<xsl:template match="content | talkTime" mode="talkTime">
		<br/>
		<xsl:apply-templates select="talkTimeType" mode="talkTimeType"/>
		<xsl:apply-templates select="talkTimes"/>
	</xsl:template>

	<xsl:template match="talkTimes">
		<xsl:apply-templates select="times" mode="times"/>
	</xsl:template>

	<xsl:template match="times" mode="times">
		<xsl:apply-templates select="time" mode="time"/>
	</xsl:template>
	<!--=========== Beginn Templates fuer einzelne Attribute ===============-->

	<xsl:template match="city" mode="city">
		<xsl:choose>
			<xsl:when test="not(boolean(.=''))">
				<xsl:value-of select="."/>
				<br/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<xsl:template match="postOfficeBox" mode="postOfficeBox">
		<xsl:if test=".!=''">Postfach: <xsl:value-of select="."/><br/>
			</xsl:if>
	</xsl:template>

	<xsl:template match="jobTitle" mode="jobTitle">
		<xsl:if test=".!=''">Berufsbezeichung: <xsl:value-of select="."/><br/>
	</xsl:if>
	</xsl:template>
	<xsl:template match="countryJob" mode="countryJob">
		<xsl:if test=".!=''"> Land des Erwerbs der Berufsbezeichung: <xsl:value-of select="."/><br/>
</xsl:if>
	</xsl:template>
	<xsl:template match="medicalAssociation" mode="medicalAssociation">
		<xsl:if test=".!=''">Ärztekammer: <xsl:value-of select="."/><br/>
			</xsl:if>
	</xsl:template>
	<xsl:template match="linkMedicalAssociation" mode="linkMedicalAssociation">
	<xsl:if test=".!=''">
		Link zur Ärztekammer: <a target="_blank"><xsl:attribute name="href">http://<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></a><br/>
			</xsl:if>
	</xsl:template>

	<xsl:template match="talkTimeType" mode="talkTimeType">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="time" mode="time">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>
	</xsl:template>



	<xsl:template match="info" mode="info">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="misc" mode="misc">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="name" mode="name">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="name" mode="talkTimes">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>



	<xsl:template match="lastname" mode="lastname">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="firstname" mode="firstname">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>&#160;</b>
		</xsl:if>
	</xsl:template>

	<xsl:template match="title" mode="title">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>&#160;</b>
		</xsl:if>	</xsl:template>

	<xsl:template match="salutation" mode="salutation">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>&#160;</b>
		</xsl:if>	</xsl:template>

	<xsl:template match="birthDay" mode="birthDay">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>&#160;
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="uebergeordnete_einrichtung" mode="uebergeordnete_einrichtung">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="street" mode="street">
		<xsl:if test=".!=''">
				<xsl:value-of select="."/>&#160;
		</xsl:if>
	</xsl:template>

	<xsl:template match="streetNr" mode="streetNr">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>	</xsl:template>


	<xsl:template match="countryCode" mode="countryCode">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>-</xsl:if>	</xsl:template>

	<xsl:template match="zipcode | zipCode" mode="zipcode">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>&#160;
		</xsl:if>
	</xsl:template>

	<xsl:template match="buildingNr" mode="buildingNr">
		<xsl:if test=".!=''">Gebäude-Nr.:&#160;<xsl:value-of select="."/><br/>
</xsl:if>	</xsl:template>

	<xsl:template match="buildingLevel" mode="buildingLevel">
		<xsl:if test=".!=''">Ebene:&#160;<xsl:value-of select="."/><br/>
</xsl:if>
	</xsl:template>

	<xsl:template match="roomNr" mode="roomNr">
		<xsl:if test=".!=''">Zimmer-Nr.:&#160;<xsl:value-of select="."/><br/>
</xsl:if>
	</xsl:template>

	<xsl:template match="job" mode="job">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="phone1 | phone2" mode="phone1">
		<xsl:if test=".!=''">
			<b>Tel.:&#160; <xsl:value-of select="."/></b>
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="mobilePhone" mode="mobilePhone">
		<xsl:if test=".!=''">
			<b>Mobil:&#160; <xsl:value-of select="."/></b>
			<br/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="fax" mode="fax">
		<xsl:if test=".!=''">Fax:&#160;<xsl:value-of select="."/><br/>
</xsl:if>
	</xsl:template>

	<xsl:template match="email" mode="email">
		<xsl:if test=".!=''"><!--E-Mail:&#160;--><a>
<xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
<xsl:value-of select="."/>
</a><br/>
</xsl:if>
	</xsl:template>

	<xsl:template match="homepage" mode="homepage">
	<xsl:if test=".!=''">
		<a target="_blank">
			<xsl:attribute name="href">
				<xsl:value-of select="."/>
			</xsl:attribute>
			<xsl:value-of select="."/>
		</a>
		<br/>
	</xsl:if>
	</xsl:template>
	<!--Template fuer den RoleType bei Verknuepfungen auf eine Person-->

	<xsl:template match="roleType" mode="roleType">
		<xsl:if test="not(boolean(.=''))">
			<xsl:choose>
				<xsl:when test=".='1'">
					<b>Geschäftsführender Direktor</b>
					<br/>
				</xsl:when>
				<xsl:when test=".='2'">
					<b>Direktor</b>
					<br/>
				</xsl:when>
				<xsl:when test=".='3'">
					<b>Stv. Direktor</b>
					<br/>
				</xsl:when>
				<xsl:when test=".='4'">
					<b>Kommissarischer Direktor</b>
					<br/>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template match="roleType" mode="fliesstext">
		<xsl:if test="not(boolean(.=''))">
			<xsl:choose>
				<xsl:when test=".='1'">, Geschäftsführender Direktor</xsl:when>
				<xsl:when test=".='2'">, Direktor</xsl:when>
				<xsl:when test=".='3'">, Stv. Direktor</xsl:when>
				<xsl:when test=".='4'">, Kommissarischer Direktor</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<!-- Templates fuer Fliesstext-->
	<xsl:template match="birthday | roleType | job | jobTitle | countryJob | medicalAssociation | linkMedicalAssociation" mode="fliesstext">
		<xsl:if test="not(boolean(.='')) and preceding-sibling::*" xml:space="preserve">,&#32;</xsl:if>
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="title" mode="fliesstext">
		<xsl:if test="not(boolean(.=''))  and preceding-sibling::* and not(preceding::firstname) and not(preceding::salutation)">,</xsl:if>
		<xsl:if test="preceding-sibling::*" xml:space="preserve">&#32;</xsl:if>
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="lastname | firstname" mode="fliesstext">
		<xsl:if test="not(boolean(.=''))  and preceding-sibling::* and not(preceding::firstname) and not(preceding::salutation)">,</xsl:if>
		<xsl:if test="preceding-sibling::*" xml:space="preserve">&#32;</xsl:if>
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="salutation" mode="fliesstext">
		<xsl:if test="not(boolean(.=''))  and preceding-sibling::* and not(preceding::firstname) and not(preceding::salutation)">
		</xsl:if>
		<xsl:if test="preceding-sibling::*">
		</xsl:if>
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="street" mode="fliesstext">
		<xsl:if test="not(boolean(.=''))">
			<xsl:if test="preceding-sibling::*" xml:space="preserve">,&#32;</xsl:if>
			<xsl:value-of select="."/>
			<xsl:if test="../streetNr" xml:space="preserve">&#32;<xsl:value-of select="../streetNr"/></xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="birthday" mode="fliesstext">
		<xsl:if test=".!=''" xml:space="preserve">Geburtstag:&#32;<xsl:value-of select="."/>&#32;</xsl:if>
	</xsl:template>

	<xsl:template match="country" mode="country">
		<xsl:if test=".!=''">
			<b>
				<xsl:value-of select="."/>
			</b>
			<br/>
		</xsl:if>
	</xsl:template>
	<!-- END Templates fuer Fliesstext-->

	<xsl:template match="zipcode-ansch" mode="zipcode-ansch">
		<xsl:if test=".!=''">
			<xsl:value-of select="."/>
			<br/>
		</xsl:if>
	</xsl:template>
	<!--START Template fuer Person in Listenform-->
	<xsl:template match="include[@type='person']" mode="listenform">
		<xsl:call-template name="line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="personItem">
			<tr>

				<td align="right" width="50%">
					<img hspace="0" vspace="0">
						<xsl:choose>
							<xsl:when test="boolean(content/image&gt;0)">
								<!-- wenn es ein Bild gibt-->
								<xsl:attribute name="src">/img/ejbimage<xsl:choose>
									<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
									<xsl:otherwise>/dummy.jpg</xsl:otherwise>
								</xsl:choose>?id=<xsl:value-of select="content/image"/>&amp;typ=s</xsl:attribute>
								<xsl:if test="boolean(content/image[@width])">
									<!--nur wenn ein width angegeben ist-->
									<xsl:attribute name="width">
										<xsl:value-of select="@width"/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="boolean(content/image[@height])">
									<!--nur wenn ein height angegeben ist-->
									<xsl:attribute name="height">
										<xsl:value-of select="@height"/>
									</xsl:attribute>
								</xsl:if>
							</xsl:when>
							<!--Wenn es kein Bild gibt-->
							<xsl:otherwise>
								<xsl:attribute name="src">/cms/img/spacer.gif</xsl:attribute>
								<xsl:attribute name="width">10</xsl:attribute>
								<xsl:attribute name="height">50</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</img>
				</td>
				<td width="19">
					<img src="/cms/img/spacer.gif" width="19" height="19"/>
				</td>
				<td align="left" valign="top" width="50%">
					<br/>
					<xsl:apply-templates select="." mode="normal"/>
					<br/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="line"/>
	</xsl:template>


	<xsl:template match="unitMembersList | membersList" mode="format">
		<xsl:apply-templates select="person" mode="membersList">
			<xsl:sort select="position" data-type="number" order="ascending" lang="de"/>
			<xsl:sort select="lastname" data-type="text" order="ascending" lang="de"/>
		</xsl:apply-templates>
	</xsl:template>
	<!--Personen in Listenform aus neuer Datenstruktur, 26.5.2004, Hato-->
	<xsl:template match="person" mode="membersList">
		<xsl:call-template name="line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="personItem">
			<tr>

				<td align="right" width="50%">
					<img hspace="0" vspace="0">
						<xsl:choose>
							<xsl:when test="boolean(@imageid&gt;0)">
								<!-- wenn es ein Bild gibt-->
								<xsl:attribute name="src">/img/ejbimage<xsl:choose>
									<xsl:when test="filename != ''">/<xsl:value-of select="filename"/></xsl:when>
									<xsl:otherwise>/dummy.jpg</xsl:otherwise>
								</xsl:choose>?id=<xsl:value-of select="@imageid"/>&amp;typ=s</xsl:attribute>
							</xsl:when>
							<!--Wenn es kein Bild gibt-->
							<xsl:otherwise>
								<xsl:attribute name="src">/cms/img/spacer.gif</xsl:attribute>
								<xsl:attribute name="width">10</xsl:attribute>
								<xsl:attribute name="height">50</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</img>
				</td>
				<td width="19">
					<img src="/cms/img/spacer.gif" width="19" height="19"/>
				</td>
				<td align="left" valign="top" width="50%">
					<br/>
					<xsl:apply-templates select="." mode="normal"/>
					<br/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="line"/>
	</xsl:template>


	<xsl:template match="person" mode="normal">
		<xsl:apply-templates select="roleType" mode="roleType"/>
		<xsl:apply-templates select="salutation" mode="salutation"/>
		<xsl:apply-templates select="title" mode="title"/>
		<xsl:apply-templates select="firstname" mode="firstname"/>
		<xsl:apply-templates select="lastname" mode="lastname"/>
		<xsl:apply-templates select="job" mode="job"/>
	
		<!--Bei Adressen in Teamkontaktlisste immer erst die Sekretariatsadresse und dann die Bueroadresse-->
		<xsl:apply-templates select="address[addressType='Sekretariat']" mode="normal"/>
		<xsl:apply-templates select="address[addressType='Büro' or addressType='null' or addressType='']" mode="normal"/>
	</xsl:template>

	<xsl:template match="address" mode="normal">
	<div class="address">
		<xsl:choose>
			<xsl:when test="postOfficeBox!=''">
				<b>Postanschrift</b>
				<br/>
			</xsl:when>
		</xsl:choose>
		<xsl:apply-templates select="buildingNr" mode="buildingNr"/>
		<xsl:apply-templates select="buildingLevel" mode="buildingLevel"/>
		<xsl:apply-templates select="roomNr" mode="roomNr"/>
		<xsl:apply-templates select="street" mode="street"/>
		<xsl:apply-templates select="streetNr" mode="streetNr"/>
		<xsl:apply-templates select="postOfficeBox" mode="postOfficeBox"/>
		<xsl:apply-templates select="countryCode" mode="countryCode"/>
		<xsl:apply-templates select="zipCode" mode="zipcode"/>
		<xsl:apply-templates select="city" mode="city"/>
		<xsl:apply-templates select="country" mode="country"/>
		<xsl:apply-templates select="phone1" mode="phone1"/>
		<xsl:apply-templates select="phone2" mode="phone1"/>
		<xsl:apply-templates select="mobilePhone" mode="mobilePhone"/>
		<xsl:apply-templates select="fax" mode="fax"/>
		<xsl:apply-templates select="email" mode="email"/>
		<xsl:apply-templates select="homepage" mode="homepage"/>
		<xsl:apply-templates select="misc" mode="misc"/>
		</div>
	</xsl:template>
	<!--END Template fuer Person in Listenform-->
	<!--START Templates fuer fliesstext-->
	<xsl:template match="include[@type='person']" mode="fliesstext">
		<xsl:apply-templates select="content/salutation" mode="fliesstext"/>
		<xsl:apply-templates select="content/title" mode="fliesstext"/>
		<xsl:apply-templates select="content/firstname" mode="fliesstext"/>
		<xsl:apply-templates select="content/lastname" mode="fliesstext"/>
		<xsl:apply-templates select="roleType" mode="fliesstext"/>
		<xsl:apply-templates select="content/job" mode="fliesstext"/>
		<xsl:apply-templates select="content/birthDay" mode="fliesstext"/>
		<xsl:apply-templates select="content/jobTitle" mode="fliesstext"/>
		<xsl:apply-templates select="content/countryJob" mode="fliesstext"/>
		<xsl:apply-templates select="content/medicalAssociation" mode="fliesstext"/>
		<xsl:apply-templates select="content/linkMedicalAssociation" mode="fliesstext"/>
		<xsl:apply-templates select="include[@type='address']" mode="fliesstext"/>
		<xsl:apply-templates select="include[@type='talkTime']" mode="fliesstext"/>
	</xsl:template>

	<xsl:template match="include[@type='address']" mode="fliesstext">
		<xsl:if test="content/roomNr!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Raumnummer:&#32;<xsl:apply-templates select="content/roomNr" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/buildingLevel!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Etage:&#32;<xsl:apply-templates select="content/buildingLevel" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/buildingNr!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Gebäudenummer:&#32;<xsl:apply-templates select="content/buildingNr" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/street!='' and (preceding-sibling::content)" xml:space="preserve"><xsl:apply-templates select="content/street" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/streetNr!='' and (preceding-sibling::content)" xml:space="preserve">&#32;<xsl:apply-templates select="content/streetNr" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/postOfficeBox!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Postfach:&#32;<xsl:apply-templates select="content/postOfficeBox" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/zipcode!=''" xml:space="preserve">,&#32;<xsl:apply-templates select="content/zipcode" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/city!='' and (preceding-sibling::content)" xml:space="preserve">&#32;<xsl:apply-templates select="content/city" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/countryCode!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Ländercode:&#32;<xsl:apply-templates select="content/countryCode" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/country!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/country" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/phone1!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/phone1" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/phone2!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/phone2" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/email!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/email" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/homepage!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/homepage" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/mobilePhone!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;<xsl:apply-templates select="content/mobilePhone" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/fax!='' and (preceding-sibling::content)" xml:space="preserve">,&#32;Faxnummer:&#32;<xsl:apply-templates select="content/fax" mode="fliesstext"/></xsl:if>
		<xsl:if test="content/misc!=''" xml:space="preserve">,&#32;<xsl:apply-templates select="content/misc" mode="fliesstext"/></xsl:if>
	</xsl:template>


	<xsl:template match="email" mode="fliesstext">
		<a>
			<xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
			<xsl:value-of select="."/>
		</a>
	</xsl:template>
</xsl:stylesheet>



<!-- Stylus Studio meta-information - (c)1998-2004. Sonic Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario0" userelativepaths="yes" externalpreview="no" url="..\..\ukd\tests\dbcomponents.xml" htmlbaseurl="" outputurl="" processortype="internal" profilemode="0" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
</metaInformation>
--><!-- Stylus Studio meta-information - (c)1998-2004. Sonic Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario0" userelativepaths="yes" externalpreview="no" url="..\..\ukd\tests\dbcomponents.xml" htmlbaseurl="" outputurl="" processortype="internal" profilemode="0" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""/></scenarios><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
</metaInformation>
-->