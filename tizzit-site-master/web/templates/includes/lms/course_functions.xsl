<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">

<!-- Zu timerfunction: -->
<!-- 
Bei Verwendung des Templates timerfunction muss unbedingt beachtet
werden, dass der Namesspace xs angegeben wird und die XSLT-Version 
auf 2.0 gesetzt wird.
-->
    


    
    <!-- Kurs-Link über das LMS -->
    <xsl:template name="courselink_lms">
        <xsl:param name="userid" />
        <xsl:attribute name="href">
            <xsl:text>javascript:MM_openBrWindow('</xsl:text>
            <xsl:value-of
                select="concat('/corelms/launch?cid=', normalize-space(courseid) ,'&amp;sessionid=',normalize-space(sessionid),'&amp;userid=', $userid)"/>
            <xsl:text>', 'module', 'width=765, height=455');</xsl:text>
        </xsl:attribute>
    </xsl:template>

    <!-- Kurs Link ohne LMS -->
    <xsl:template name="courselink_withoutlms">
        <xsl:attribute name="href">
            <xsl:text>javascript:MM_openBrWindow('/courses/</xsl:text>
            <xsl:value-of select="normalize-space(courseid)"/>
            <xsl:text>/index.html', 'module', 'width=760, height=450');</xsl:text>
        </xsl:attribute>
    </xsl:template>
    
    <!-- Kurs Registrierung -->
    <xsl:template name="courseregistration">
        <xsl:param name="userid" />
        <!-- COURSEID kann übergeben werden -->
        <xsl:param name="courseid" select="''"/>
        <xsl:attribute name="href">
            <!-- Wenn courseid übergeben wurde, dann diese benutzen, ansonsten das courseid-tag aus der xml verwenden -->
            <xsl:choose>
                <xsl:when test="$courseid!=''">
                    <xsl:value-of   select="concat('/courseregistration?courseid=', $courseid, '&amp;mandant=', $clientCode ,'&amp;userid=', $userid, '&amp;register=true')" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of   select="concat('/courseregistration?courseid=', courseid, '&amp;mandant=', $clientCode ,'&amp;userid=', $userid, '&amp;register=true')" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    <!-- Kurs Zertifikat of Completion -->
    <xsl:template name="course_certificateCompletion">
        <xsl:attribute name="href">
            <xsl:text>/</xsl:text>
            <xsl:value-of select="$language" />
            <xsl:text>/certificates/certificateOfCompletion/content.pdf?courseid=</xsl:text>
            <xsl:value-of select="courseid" />
        </xsl:attribute>
    </xsl:template>
    
    <!-- Kurs Zertifikat CME -->
    <xsl:template name="course_certificateCME">
        <xsl:attribute name="href">
            <xsl:text>/</xsl:text>
            <xsl:value-of select="$language" />
            <xsl:text>/certificates/certificateCME/content.pdf?courseid=</xsl:text>
            <xsl:value-of select="courseid" />
        </xsl:attribute>
    </xsl:template>
    
    <!-- Timer-Funktion -->
    <xsl:template name="timerfunction">
        <xsl:param name="timer" select="''" />
        <xsl:variable name="hours" select="floor($timer div 3600)" />
        <xsl:variable name="minutes" select="floor(($timer mod 3600) div 60)" />
        <xsl:variable name="seconds" select="floor($timer mod 60)" />
        
        <xsl:if test="string-length(xs:string($hours)) &lt; 2">0</xsl:if>
        <xsl:value-of select="$hours" />
        <xsl:text>:</xsl:text>
        <xsl:if test="string-length(xs:string($minutes)) &lt; 2">0</xsl:if>
        <xsl:value-of select="$minutes" />
        <xsl:text>:</xsl:text>
        <xsl:if test="string-length(xs:string($seconds)) &lt; 2">0</xsl:if>
        <xsl:value-of select="$seconds" />
    </xsl:template>
    
    <!-- Kurs Beschreibung -->
    <xsl:template name="coursedescription">
        <xsl:param name="countries" />
        <xsl:if test="metadata/country">
            <div class="found_course_countries">
                <span class="stress">Country: </span>
                <xsl:apply-templates select="metadata/country">
                    <xsl:with-param name="countries" select="$countries"/>
                </xsl:apply-templates>
            </div>
        </xsl:if>
        <xsl:if test="metadata/autor">
            <div class="found_course_author">
                <span class="stress">Author: </span>
                <xsl:value-of select="metadata/autor"/>
            </div>
        </xsl:if>
        <xsl:if test="normalize-space(description)!=''">
            <div>
                <span class="stress">Course Description: </span>
                <xsl:value-of select="description"/>
            </div>
        </xsl:if>
        <div>
            <span class="stress">Course Accreditation: </span>
            <xsl:choose>
                <xsl:when test="metadata/archived='true'"> This course is archived, there is
                    no CME-Accreditation. </xsl:when>
                <xsl:when test="metadata/points"> This course is accredited with
                    <xsl:value-of select="metadata/points"/> CME-Points in <xsl:value-of
                        select="metadata/country"/>
                </xsl:when>
                <xsl:otherwise> This course is not CME-accredited. </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    
    <!-- Kurs -->
    <xsl:template name="coursedescription_link">
        <xsl:param name="courseid" />
        <xsl:attribute name="href">
            <xsl:text>/</xsl:text>
            <xsl:value-of select="$language"/>
            <xsl:text>/coursedescription/</xsl:text>
            <xsl:value-of select="$courseid"/>
            <xsl:text>/page.html</xsl:text>
        </xsl:attribute>
    </xsl:template>

 </xsl:stylesheet>
