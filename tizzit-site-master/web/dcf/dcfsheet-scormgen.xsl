<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="keyvalue_type">
        <xsl:element name="keyvalue_type">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">keyvalue_type</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Default</name>
                    <value>extra</value>
                </property>
                <property name="dropdownValues">
                    <name>Novartis</name>
                    <value>novartis</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="layout">
        <xsl:element name="layout">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">layout</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Default</name>
                    <value>default</value>
                </property>
                <property name="dropdownValues">
                    <name>Serono Symposia</name>
                    <value>serono_symposia</value>
                </property>
                <property name="dropdownValues">
                    <name>Staarnet</name>
                    <value>staarnet</value>
                </property>
                <property name="dropdownValues">
                    <name>Sandoz</name>
                    <value>sandoz</value>
                </property>
                <property name="dropdownValues">
                    <name>Primovist</name>
                    <value>primovist</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="alignParagraph">
        <xsl:element name="{@name}">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname"><xsl:value-of select="@name"/></xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Bild Rechts / Image Right</name>
                    <value>right</value>
                </property>
                <property name="dropdownValues">
                    <name>Bild Links / Image Left</name>
                    <value>left</value>
                </property>
                <property name="dropdownValues">
                    <name>Mittig / Center (Nur bei Paragraphen mit Bild oder Ani OHNE Text verwenden)</name>
                    <value>center</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="key">
        <xsl:element name="key">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">key</xsl:attribute>
            <xsl:attribute name="name">@name</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>CME-Points</name>
                    <value>cme-points</value>
                </property>
                <property name="dropdownValues">
                    <name>Certificate (true/false)</name>
                    <value>certificate</value>
                </property>
                <property name="dropdownValues">
                    <name>Duration of Course (e.g. 50 min)</name>
                    <value>duration</value>
                </property>
                <property name="dropdownValues">
                    <name>Author of the CME Course</name>
                    <value>author</value>
                </property>
                <property name="dropdownValues">
                    <name>Points that can be reached in the course</name>
                    <value>points</value>
                </property>
                <property name="dropdownValues">
                    <name>Key Visual for the course</name>
                    <value>picture</value>
                </property>
                <property name="dropdownValues">
                    <name>Profession /Beruf</name>
                    <value>profession</value>
                </property>
                <property name="dropdownValues">
                    <name>Therapy Area / Fachgebiet</name>
                    <value>ta</value>
                </property>
                <property name="dropdownValues">
                    <name>Anzahl Folien / Number of Screens (overwrites automatic value)</name>
                    <value>number_of_screens</value>
                </property>
                <property name="dropdownValues">
                    <name>Anzahl Kapitel / Number of Chapters (overwrites automatic value)</name>
                    <value>number_of_scos</value>
                </property>
                <property name="dropdownValues">
                    <name>Anzahl Videos / Number of videos (overwrites automatic value)</name>
                    <value>number_of_videos</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="choice_type">
        <xsl:element name="choice_type">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">choice_type</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Single Choice</name>
                    <value>single</value>
                </property>
                <property name="dropdownValues">
                    <name>Multiple Choice</name>
                    <value>multi</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="lesson_type">
        <xsl:element name="lesson_type">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">lesson_type</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Lesson</name>
                    <value>lesson</value>
                </property>
                <property name="dropdownValues">
                    <name>Test</name>
                    <value>test</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="flash_image_size">
        <xsl:element name="flash_image_size">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">image_size</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>false</mandatory>
                <property name="dropdownValues">
                    <name>Large</name>
                    <value>l</value>
                </property>
                <property name="dropdownValues">
                    <name>Medium</name>
                    <value>m</value>
                </property>
                <property name="dropdownValues">
                    <name>Small</name>
                    <value>s</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    
    <!-- Drop Down zur Auswahl, ob die Antwort richtig oder falsch ist -->
    <xsl:template match="answer">
        <xsl:variable name="elementname">
            <xsl:choose>
                <xsl:when test="@name">
                    <xsl:value-of select="@name"/>
                </xsl:when>
                <xsl:otherwise>distractor</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        
        <!-- Zusammenbauen Drop Down -->
        <xsl:element name="{$elementname}">
            <xsl:attribute name="label">Answer is</xsl:attribute>
            <xsl:attribute name="dcfname">
                <xsl:value-of select="$elementname"/>
            </xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>correct</name>
                    <value>true</value>
                </property>
                <property name="dropdownValues">
                    <name>not correct</name>
                    <value>false</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
        
    </xsl:template>
    
    
    
    
    <xsl:template match="document_type">
        <xsl:element name="document_type">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname">document_type</xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>Animation (.swf)</name>
                    <value>swf</value>
                </property>
                <property name="dropdownValues">
                    <name>Audio (.mp3)</name>
                    <value>mp3</value>
                </property>
                <property name="dropdownValues">
                    <name>PDF (.pdf)</name>
                    <value>pdf</value>
                </property>
                <property name="dropdownValues">
                    <name>Picture (.jpg, .jpeg)</name>
                    <value>jpg</value>
                </property>
                <property name="dropdownValues">
                    <name>Video (.flv)</name>
                    <value>flv</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="true_or_false">
        <xsl:element name="{@name}">
            <xsl:attribute name="label"><xsl:value-of select="@label"/></xsl:attribute>
            <xsl:attribute name="dcfname"><xsl:value-of select="@name"/></xsl:attribute>
            <dcfConfig>
                <classname>de.juwimm.cms.content.modules.SimpleDropDown</classname>
                <mandatory>true</mandatory>
                <property name="dropdownValues">
                    <name>true</name>
                    <value>true</value>
                </property>
                <property name="dropdownValues">
                    <name>false</name>
                    <value>false</value>
                </property>
            </dcfConfig>
            <dcfInitial/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>