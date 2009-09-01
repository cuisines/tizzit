<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
<xsl:output method="text" indent="no" />
    
<xsl:template name="reset" match="styles" mode="reset" priority="0.5">
html,address,blockquote,body,div,dl,dt,dd,fieldset,form,h1,h2,h3,h4,h5,h6,ol,p,ul,li,center,hr,pre,table,tr,td,th,thead,tbody,tfoot,caption {
font-family:inherit; font-style:normal; font-weight:normal;
font-variant:normal; font-size:inherit; line-height:inherit;
text-align:left; text-decoration:none; padding:0; margin:0;
float:none; border:0px; display:block; cursor:default; }
span,a,img,abbr,code,em,strong,b,i,textarea,legend,button,input,object,select,var,tt,kbd,samp,small,sub,sup {
font:inherit; background:transparent; padding:0; margin:0;
color:inherit; display:inline; float:none; border:0; }

li       { display:list-item; }

table    { display:table; border-spacing:0; border-collapse:collapse; }
tr       { display:table-row; }
th,td    { display:table-cell; }
thead    { display:table-header-group; }
tbody    { display:table-row-group; }
tfoot    { display:table-footer-group; }
caption  { display:table-caption; }
colgroup { display:table-column-group; }
col      { display:table-column; }

head     { display:none; }

button,textarea,input,object,select { display:inline-block; }
</xsl:template>
    
</xsl:stylesheet>