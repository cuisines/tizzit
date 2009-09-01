<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    
    <!-- 
        <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd" [
        <!ENTITY ns_svg "http://www.w3.org/2000/svg">
        <!ENTITY ns_xlink "http://www.w3.org/1999/xlink">
        ]>
        xmlns="&ns_svg;" xmlns:xlink="&ns_xlink;"
     -->
    
    
 <xsl:param name="text"/>
    
  <xsl:template match="page">
      <svg  version="1.1" id="Ebene_1" width="195.03" height="18" viewBox="0 0 195.03 18"
          overflow="visible" enable-background="new 0 0 195.03 18" xml:space="preserve">
          <g>
              <g>
                  <g>
                      <radialGradient id="XMLID_2_" cx="10.6558" cy="9.0527" r="7.9478" gradientUnits="userSpaceOnUse">
                          <stop  offset="0" style="stop-color:#FFFFFF"/>
                          <stop  offset="1" style="stop-color:#000000"/>
                      </radialGradient>
                      <circle opacity="0.83" fill="url(#XMLID_2_)" cx="10.656" cy="9.053" r="7.948"/>
                      <circle opacity="0.56" fill="#FF0000" cx="10.656" cy="9.053" r="7.948"/>
                  </g>
                  <g>
                      <g>
                          <g>
                              <path fill="none" d="M14.102,5.098c-1.903,0.707-2.845,1.603-3.33,3.51c-0.792,2.969-0.72,3.409-3.805,4.017"/>
                              <path fill="#FFFFFF" d="M13.781,4.424c-1.915,0.753-3.143,1.92-3.709,3.925c-0.239,0.845-0.365,1.808-0.833,2.564
                                  c-0.446,0.722-1.664,0.826-2.401,0.977c-0.941,0.191-0.685,1.662,0.259,1.469c1.532-0.313,2.907-0.654,3.661-2.127
                                  c0.429-0.839,0.572-1.859,0.826-2.759c0.413-1.46,1.498-2.173,2.839-2.701C15.317,5.419,14.673,4.073,13.781,4.424
                                  L13.781,4.424z"/>
                          </g>
                      </g>
                      <g>
                          <g>
                              <path fill="none" d="M10.744,9.11c0.81-0.027,1.565-0.027,2.322,0.007"/>
                              <path fill="#FFFFFF" d="M10.674,9.854c0.774-0.024,1.548-0.025,2.322,0.007c0.959,0.04,1.102-1.446,0.141-1.486
                                  c-0.774-0.032-1.548-0.031-2.322-0.007c-0.406,0.013-0.771,0.23-0.813,0.673C9.967,9.406,10.267,9.866,10.674,9.854
                                  L10.674,9.854z"/>
                          </g>
                      </g>
                  </g>
              </g>
              <line fill="none" stroke="#BF0000" x1="0.5" y1="0" x2="0.5" y2="18"/>
              <line fill="none" stroke="#BF0000" x1="194.531" y1="0" x2="194.531" y2="18"/>
              <xsl:apply-templates />
          </g>
      </svg>
</xsl:template>

    <xsl:template match="para">
        <text transform="matrix(1 0 0 1 22.5415 13.5)" fill="#BF0000" font-family="'ArialMT'" font-size="12">
            <xsl:value-of select="$text"/>
        </text>
    </xsl:template>

</xsl:stylesheet>