<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- team template  -->
	<xsl:template match="team" mode="format">
		<xsl:apply-templates select="."/>
	</xsl:template>


	<xsl:template match="team-mitglied">
		<xsl:text disable-output-escaping="yes">&lt;/font&gt;</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td width="18" height="19"&gt;</xsl:text>
		<img src="/cms/img/spacer.gif" width="18" height="18"/>
		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;/tr&gt;</xsl:text>
		<tr>
			<td width="19" height="1" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td width="19" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td bgcolor="000000">
				<img src="/cms/img/spacer.gif" height="1"/>
			</td>
			<td width="19" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td width="18" height="1" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="18" height="1"/>
			</td>
		</tr>
		<tr>
			<td width="19" height="1">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td colspan="4">


				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>

						<td align="right" width="50%">
							<img hspace="0" vspace="0">
								<xsl:if test="not(boolean(image=''))">
									<!-- wenn es src einen Inhalt hat, also ein Bild angegeben wurde-->
										<xsl:attribute name="src">ejbimage?id=<xsl:value-of select="image"/>&amp;typ=s</xsl:attribute>
										<xsl:choose>
											<xsl:when test="boolean(@width)">
												<!--nur wenn ein width angegeben ist-->
												<xsl:attribute name="width">
													<xsl:value-of select="@width"/>
												</xsl:attribute>
											</xsl:when>
										</xsl:choose>
										<xsl:choose>
											<xsl:when test="boolean(@height)">
												<!--nur wenn ein height angegeben ist-->
												<xsl:attribute name="height">
													<xsl:value-of select="@height"/>
												</xsl:attribute>
											</xsl:when>
										</xsl:choose>
									</xsl:if>
									<!-- wenn src keinen Inhalt hat-->
									<xsl:if test="boolean(image='') or not(boolean(image))">
										<xsl:attribute name="src">/cms/img/spacer.gif</xsl:attribute>
										<xsl:attribute name="width">10</xsl:attribute>
										<xsl:attribute name="height">50</xsl:attribute>
									</xsl:if>
							
							</img>
						</td>
						<td width="19">
							<img src="/cms/img/spacer.gif" width="19" height="19"/>
						</td>
						<td align="left" valign="top" width="50%">
								<br/>
								<xsl:apply-templates mode="format"/>
								<br/>
						</td>
					</tr>
				</table>
			</td>
			<td width="18" height="1">
				<img src="/cms/img/spacer.gif" width="18" height="1"/>
			</td>
		</tr>
		<tr>
			<td width="19" height="1" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td width="19" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td width="150" bgcolor="000000">
				<img src="/cms/img/spacer.gif" height="1"/>
			</td>
			<td width="19" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="19" height="1"/>
			</td>
			<td width="18" height="1" bgcolor="000000">
				<img src="/cms/img/spacer.gif" width="18" height="1"/>
			</td>
		</tr>
		<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
		<td width="19" height="19">
			<img src="/cms/img/spacer.gif" width="19" height="19"/>
		</td>
		<xsl:text disable-output-escaping="yes">&lt;td colspan="4"&gt;</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;font class="text"&gt;</xsl:text>
	</xsl:template>
</xsl:stylesheet>


<!-- Stylus Studio meta-information - (c)1998-2001 eXcelon Corp.
<metaInformation>
<scenarios/><MapperInfo  srcSchemaPath="" srcSchemaRoot="" srcSchemaPathIsRelative="yes" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" />
</metaInformation>
-->
