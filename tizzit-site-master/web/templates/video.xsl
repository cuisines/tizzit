<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="mediaassets" mode="format">
		<xsl:apply-templates select="item" mode="media"/>
	</xsl:template>


	<xsl:template match="item" mode="media">
		<xsl:choose>
			<xsl:when test="mediatype='win'">Format: Windows Media<br/>
		</xsl:when>
			<xsl:when test="mediatype='real'">Format: Real Media <br/>
		</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="bitrate!=''">Bitrate: <xsl:apply-templates select="bitrate"/><br/></xsl:if>
		
		<xsl:choose>
		    <xsl:when test="checkBox/embedded='true'">
		    <br/>   
		   	<xsl:apply-templates select="." mode="embedded"/>
		    </xsl:when>
		    <xsl:otherwise>
		    	<xsl:apply-templates select="." mode="medialink"/>
		    </xsl:otherwise>
		</xsl:choose>
		<br/><br/>
	</xsl:template>


	<xsl:template match="mediaasset">
		<xsl:apply-templates/>
	</xsl:template>

	
	<!-- Link mit Mime type auf Video, dass sich dann im Standar Player des Users oeffnet -->
	<xsl:template match="item" mode="medialink">	    
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="mediaurl"/>
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:choose>
					<xsl:when test="mediatype='win'">video/x-ms-wmv</xsl:when>
					<xsl:when test="mediatype='real'">video/x-pn-realvideo</xsl:when>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="mediaasset"/>
		</a>	
	</xsl:template>
	
	<!-- ein embedded Player auf der Seite darstellen -->
	<xsl:template match="item" mode="embedded">	    

				<xsl:choose>
<!-- ==============
 
                   Windows Media Player
                   
                                       =========================== -->
					<xsl:when test="mediatype='win'">
<object id="MediaPlayer1"
      codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701"
      type="application/x-oleobject"
      standby="Loading Microsoft Windows Media Player components..."
      classid="clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95">
      <xsl:call-template name="widthheight"/>      
	  <param name="Filename">
	      <xsl:call-template name="srcvalue"/>  
	  </param>      
  <param name="AudioStream" value="1" />      
  <param name="AutoSize" value="0" />
  <param name="AutoStart" value="1" />
  <param name="AnimationAtStart" value="1" />        
  <param name="ShowControls" value="1" />      
  <param name="ShowAudioControls" value="1" />      
  <param name="ShowDisplay" value="0" />     
  <param name="ShowGotoBar" value="0" />      
  <param name="ShowPositionControls" value="1" />      
  <param name="ShowStatusBar" value="0" />      
  <param name="ShowTracker" value="0" />         
  <param name="Volume" value="+600" />
        
  <param name="WindowlessVideo" value="0" />
      <embed type="application/x-mplayer2"
      pluginspage="http://www.microsoft.com/windows95/downloads/contents/wurecommended/s_wufeatured/mediaplayer/default.asp"
      name="MediaPlayer1" 
      autostart="1"
      animationatstart="0"
      >      
      <xsl:call-template name="widthheight"/>
      <xsl:call-template name="src"/>  
      </embed>
      
  </object>
 </xsl:when>

<!--     

  <param name="TransparentAtStart" value="0" />      
  <param name="VideoBorderWidth" value="0" />      
  <param name="VideoBorderColor" value="0" />      
  <param name="VideoBorder3D" value="0" />   
  <param name="AllowScan" value="0" />      
  <param name="AllowChangeDisplaySize" value="0" />      
  <param name="AutoRewind" value="0" />      
  <param name="Balance" value="0" />      
  <param name="BufferingTime" value="5" />      
  <param name="ClickToPlay" value="0" />      
  <param name="CursorType" value="0" />      
  <param name="CurrentPosition" value="0" />      
  <param name="CurrentMarker" value="0" />      
  <param name="DisplayBackColor" value="0" />      
  <param name="DisplayForeColor" value="16777215" />      
  <param name="DisplayMode" value="0" />      
  <param name="DisplaySize" value="4" />      
  <param name="Enabled" value="0" />      
  <param name="EnableContextMenu" value="0" />      
  <param name="EnablePositionControls" value="0" />      
  <param name="EnableFullScreenControls" value="0" />      
  <param name="EnableTracker" value="0" />      
  <param name="InvokeURLs" value="0" />      
  <param name="Language" value="0" />      
  <param name="Mute" value="0" />      
  <param name="PlayCount" value="0" />      
  <param name="PreviewMode" value="0" />      
  <param name="Rate" value="1" />      
  <param name="SelectionStart" value="0" />      
  <param name="SelectionEnd" value="0" />      
  <param name="SendOpenStateChangeEvents" value="0" />      
  <param name="SendWarningEvents" value="0" />      
  <param name="SendErrorEvents" value="0" />      
  <param name="SendKeyboardEvents" value="0" />      
  <param name="SendMouseClickEvents" value="0" />      
  <param name="SendMouseMoveEvents" value="0" />      
  <param name="SendPlayStateChangeEvents" value="0" />      
  <param name="ShowCaptioning" value="0" />      
 -->
 <!-- ==============
 
                   Realplayer
                   
                             =========================== -->
<xsl:when test="mediatype='real'">
<object
 id="video1"
 classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA"
 >
   <xsl:call-template name="widthheight"/> 
  <param name="controls" value="ImageWindow" />
  <param name="console" value="Clip1" />
  <param name="autostart" value="false" />
  <param name="src">
    <xsl:call-template name="srcvalue"/>  
  </param>
<EMBED type="audio/x-pn-realaudio-plugin" CONSOLE="Clip1" CONTROLS="ImageWindow" AUTOSTART="false">
   <xsl:call-template name="widthheight"/>
    <xsl:call-template name="src"/>  
  </EMBED>
</object>

<!-- Player Kontrollleiste -->
<object
 id="video1"
 classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA"
 height="125" width="275">
    <param name="controls" value="All" />
    <param name="console" value="Clip1" />
<EMBED type="audio/x-pn-realaudio-plugin" CONSOLE="Clip1" CONTROLS="All"
 HEIGHT="125" WIDTH="275" AUTOSTART="false"/>
 
</object>
					
					</xsl:when>
				</xsl:choose>			
	</xsl:template>
	
	<xsl:template name="widthheight">
		<xsl:attribute name="width"><xsl:value-of select="width"/></xsl:attribute>
		<xsl:attribute name="height"><xsl:value-of select="height"/></xsl:attribute>	
	 </xsl:template>
	 
	<xsl:template name="src">
		<xsl:attribute name="src"><xsl:value-of select="normalize-space(mediaurl)"/></xsl:attribute>	
	 </xsl:template>

	<xsl:template name="srcvalue">
		<xsl:attribute name="value"><xsl:value-of select="normalize-space(mediaurl)"/></xsl:attribute>	
	 </xsl:template>
	
</xsl:stylesheet>