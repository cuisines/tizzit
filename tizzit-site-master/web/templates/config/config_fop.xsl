<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="absCmsPath"/>

<xsl:template match="/">
    <configuration>
    	<fonts>
    		<font metrics-file="{$absCmsPath}fop-fonts/arial.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/arial.ttf">
    			<font-triplet name="Arial" style="normal" weight="normal"/>
    			<font-triplet name="ArialMT" style="normal" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/arialbd.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/arialbd.ttf">
    			<font-triplet name="Arial" style="normal" weight="bold"/>
    			<font-triplet name="ArialMT" style="normal" weight="bold"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/ariali.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/ariali.ttf">
    			<font-triplet name="Arial" style="italic" weight="normal"/>
    			<font-triplet name="ArialMT" style="italic" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/arialbi.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/arialbi.ttf">
    			<font-triplet name="Arial" style="italic" weight="bold"/>
    			<font-triplet name="ArialMT" style="italic" weight="bold"/>
    		</font>

    		<font metrics-file="{$absCmsPath}fop-fonts/frabk.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/frabk.ttf">
    			<font-triplet name="Franklin" style="normal" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/frabkit.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/frabkit.ttf">
    			<font-triplet name="Franklin" style="italic" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/framd.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/framd.ttf">
    			<font-triplet name="Franklin" style="normal" weight="bold"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/framdit.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/framdit.ttf">
    			<font-triplet name="Franklin" style="italic" weight="bold"/>
    		</font>

            <!-- ltunivers (ukd) -->
    		<font metrics-file="{$absCmsPath}fop-fonts/LTUniversBasicRegular/LT_53765.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/LTUniversBasicRegular/LT_53765.pfb">
    			<font-triplet name="LTUnivers" style="normal" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/LTUniversBasicBold/LT_53769.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/LTUniversBasicBold/LT_53769.pfb">
    			<font-triplet name="LTUnivers" style="normal" weight="bold"/>
    		</font>

            <!-- univers middle -->
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr55w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr55w.ttf">
    			<font-triplet name="Univers" style="normal" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr56w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr56w.ttf">
    			<font-triplet name="Univers" style="italic" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr65w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr65w.ttf">
    			<font-triplet name="Univers" style="normal" weight="bold"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr66w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr66w.ttf">
    			<font-triplet name="Univers" style="italic" weight="bold"/>
    		</font>


            <!-- univers condensed -->
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr57w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr57w.ttf">
    			<font-triplet name="UniversCondensed" style="normal" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr58w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr58w.ttf">
    			<font-triplet name="UniversCondensed" style="italic" weight="normal"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr67w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr67w.ttf">
    			<font-triplet name="UniversCondensed" style="normal" weight="bold"/>
    		</font>
    		<font metrics-file="{$absCmsPath}fop-fonts/Univers/unvr68w.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Univers/unvr68w.ttf">
    			<font-triplet name="UniversCondensed" style="italic" weight="bold"/>
    		</font>
			
			
			<!-- Code39 -->
    		<font metrics-file="{$absCmsPath}fop-fonts/Code39r.xml" kerning="yes" embed-file="{$absCmsPath}fop-fonts/Code39r.ttf">
    			<font-triplet name="Code39" style="normal" weight="normal"/>
    		</font>
    	</fonts>
    </configuration>
</xsl:template>

</xsl:stylesheet>