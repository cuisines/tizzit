<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="getAttr">
        <![CDATA[ 
        function getAttr(object,attr){
            try{
                if (document.all){
                    return object.getAttributeNode(attr).value;
                }
                else{
                    return object.getAttribute(attr);
                }
            }
            catch(e){
                //  alert(e);
                return false;
            }
        }
    ]]>        
</xsl:template>


<xsl:template match="zoom">
 <![CDATA[ 
	zoomStatus=0;
	function zoom(){
	        if (!document.all){
	        alert("Leider steht diese Funktion nur Benutzern des Internet Explorers zur Verfügung.");
	        return;
	        }
	        if (zoomStatus==0){
	                document.all.tags('body')[0].style.setAttribute('zoom','125%','false');
	                zoomStatus=1;
	        }
	        else{
	                document.all.tags('body')[0].style.setAttribute('zoom','100%','false');
	                zoomStatus=0;
	        }

	}
]]>
</xsl:template>
    
<!-- 
    ACHTUNG:
    winName = winName.replace(/[^A-Z,0-9]/gi, '');
    Ersetzt alle Zeichen ausser a-z, A-Z und 0-9.
    Alle Anderen Zeichen führen im IE zu einem JavaScript-Fehler!!!  
-->
<xsl:template match="popup">
 <![CDATA[function MM_openBrWindow(theURL,winName,features) {
        winName = winName.replace(/[^A-Z,0-9]/gi, '');
  		x = window.open(theURL,winName,features);
  		x.focus();
	}
 ]]>
</xsl:template>


       <xsl:template match="ImgSize">
 <![CDATA[ 
/*Funktionen fuer FooterOptionen*/


function detectImgsize(){
	hoehe=document.images[0].height+30;
	breite=document.images[0].width+10;
	resizeTo(breite,hoehe);
}]]>
</xsl:template>

<xsl:template match="Anchor | jumpAnchor">
 <![CDATA[ 
function jumpAnchor(anchor){

 self.location.hash = '#'+anchor;

//a= eval('document.anchors["'+anchor+'"].name');
//a= document.anchors["unten"].name;
//alert(a);

}]]>
</xsl:template>


<xsl:template match="getNewRequest" name="getNewRequest">
 <![CDATA[ 
function getNewRequest(replacer){
  request = self.location.href;
  temp = /page.html/;  
  newRequest = request.replace(temp,replacer);
  if (request == newRequest) {
      newRequest = request + "/deutsch/" + replacer;
  }
  return newRequest;
}]]>
</xsl:template>


<xsl:template match="pdf">
 <![CDATA[ 
function pdf(){
newRequest = getNewRequest ('content.pdf');
MM_openBrWindow(newRequest,'PDF','scrollbars=yes,width=800,height=600');
}]]>
</xsl:template>


<xsl:template match="replaceAnd">
 <![CDATA[ 
function replaceAnd(request){
 //alert(request);
       temp = new Array;
       temp[0] = /&/;
       temp[1]= /%20/;
       //temp[2]= /%/;

       tempReplacer = new Array;
       tempReplacer[0] ="%26";
       tempReplacer[1]= "+";
       //tempReplacer[2]= "%25";

        for (i=0;i<temp.length;i++){
            requestTempArray = request.split(temp[i]);
            newRequest = requestTempArray[0];
            for (ii=1;ii<requestTempArray.length;ii++){
                    requestTempArray[ii].slice(1);
                    newRequest += tempReplacer[i]+requestTempArray[ii];
            }
            request = newRequest;
        }
 //alert(newRequest);
return newRequest;
}]]>
</xsl:template>


<xsl:template match="mailTo">
 <![CDATA[ 
    function mailTo(){
        var myUrl = escape(parent.location.href);
        parent.location.href="mailto:Ihre%20Kontaktadresse?subject=Link-Tipp&body=Dieser%20Link%20wird%20Ihnen%20empfohlen:%20%20" + myUrl;
}]]>
</xsl:template>


<xsl:template match="printView">
 <![CDATA[ 
function printView(){
        printRequest = getNewRequest("print.html");
        MM_openBrWindow(printRequest,'druckAnsicht','scrollbars=yes,width=600,height=420');
}
]]>
</xsl:template>


<xsl:template match="printView1">
 <![CDATA[ 
function printView(){
        request = getNewRequest("print.html");
        MM_openBrWindow(request,'druckAnsicht','scrollbars=yes,width=600,height=420');
}
]]>
</xsl:template>

<xsl:template match="printView2">
 <![CDATA[ 
function thisPrint(){
document.print.src = '/cms/img/spacer.gif';
window.print();
}]]>
</xsl:template>


<xsl:template match="favorite">
 <![CDATA[ 
function favorite(label){
if (label==undefined) label='';
  if (!document.all){
  alert("Bitte verwenden Sie die Tastenkombination STRG+D.");
  return;
  }
  var title = self.document.all.tags("title")[0].innerText;
  getNewRequest('index_1.html');
  window.external.AddFavorite(newRequest,label+title);
}]]>
</xsl:template>


<!--===================Allgemein nuetzliche Funktionen=============================-->

<xsl:template match="encodeUrl">
 <![CDATA[ 
function encodeUrl(theURL){
  urlTemp = theURL.split("?");
  urlTempFirstPart = escape(unescape(urlTemp[0]));
  theNewURL = urlTempFirstPart+"?"+urlTemp[1];
  return theNewURL;
}]]>
</xsl:template>

<xsl:template match="MM_openBrWindowForInternalLink">
 <![CDATA[ 
// Fenster öffnen für internen Link mit URL encoding (mozi+ie)
function MM_openBrWindowForInternalLink(theURL,winName,features) {
urlTemp = theURL.split("?");
urlTempFirstPart = escape(unescape(urlTemp[0]));
theNewURL = urlTempFirstPart+"?"+urlTemp[1];
x = window.open(theNewURL,winName,features);
x.focus();
}]]>
</xsl:template>


<!--Bildertausch bei Mouseover-->
<xsl:template match="mouseover">
 <![CDATA[ 
function mouseover(imageName,newImageSrc){
eval("oldImageSrc = document."+imageName+".src;");
eval("document."+imageName+".src = '"+newImageSrc+"';");
}]]>
</xsl:template>

<xsl:template match="mouseout">
 <![CDATA[ 
function mouseout(imageName){
eval("document."+imageName+".src = '"+oldImageSrc+"';");
}]]>
</xsl:template>

<xsl:template match="dropdown">
 <![CDATA[ 
function dropdown(value){
         //wenn kein externer Link
         if(value.indexOf('www')==-1){
             self.location.href=value;
         }
         else{
              window.open(value,'','');
         }
}	
]]>
</xsl:template>


<xsl:template match="fastsearch">
<![CDATA[function fastSearch (path) {
    //if wenn kein Suchbegriff, dann keine Suche
    if (document.fastsearch.query.value=="") {
       return;
    }
    else {
        // Suchstring basteln
        document.fastsearch.action ="/deutsch/"+path+"?conquest-searchquery-is-query=true&format=long&conquest-searchquery="
            + "metadata:(" + document.fastsearch.query.value + ")^4 "
            + "url:(" + document.fastsearch.query.value + ")^3 "
            + "title:(" + document.fastsearch.query.value + ")^2 "
            + "contents:(" + document.fastsearch.query.value +")";
       // abschickern
       document.fastsearch.submit();
    }
}
]]>
</xsl:template>


<xsl:template match="search">
 <![CDATA[ 
	
function checkOnSearch() {
             //if wenn kein Suchbegriff, dann keine Suche
            if (document.search.words.value=="") {
       	   return;
            }
            else {
                      // Suchstring basteln
                      document.search.action ="/deutsch/Universit%E4tsklinikum/searchresult.html?format=" +
                        document.search.format.options[document.search.format.selectedIndex].value +
                       "&conquest-searchquery=url:(" + document.search.words.value + ")^3 title:(" +
                        document.search.words.value + ")^2 contents:(" + document.search.words.value +")" +
                        " AND language:" + document.search.s_language.options[document.search.s_language.selectedIndex].value;
                        // wenn Suche auf Seiten mit bestimmtem Template eingeschränkt werden soll, Suchstring ergänzen
        if  (document.search.template.options[document.search.template.selectedIndex].value !="") {
              document.search.action += " AND template:" + document.search.template.options[document.search.template.selectedIndex].value;
           }
            // abschickern
            document.search.submit();
            }
}
]]>

</xsl:template>

<xsl:template match="unitsearch">
 <![CDATA[ 

function checkOnUnitSearch() {
     // wenn kein Suchbegriff, dann keine Suche
      if (document.search.words.value=="") {
   return;
      }
      else {
            // Suchstring basteln
             document.search.action ="/deutsch/Universit%E4tsklinikum/searchresult.html?format=" +
          document.search.format.options[document.search.format.selectedIndex].value +
          "&conquest-searchquery=url:(" + document.search.words.value + ")^3 title:(" +
          document.search.words.value + ")^2 contents:(" + document.search.words.value +")" +
          " AND language:" + document.search.s_language.options[document.search.s_language.selectedIndex].value;
		// bei Suche nur in dieser Einrichtung Suchstring ergänzen und damit Suche einschränken
              if  (document.search.scope.options[document.search.scope.selectedIndex].value =="unit")
            {
                        document.search.action += " AND unitId:" + document.search.unitId.value;
            }
    // abschickern
    document.search.submit();
}
}
]]>
</xsl:template>



<xsl:template match="menueConfig">

<![CDATA[ 
	CLICKEDCOLOR = "#AE1C1E";
	COLOR = "#FFFFBF";
	EXTRADELAY = 750; //Timedelay for the Extra Layers after mouseoff
	LEFTPOS = 202; // Abstand des ersten Extralayers vom linken Rand
	TOPPOS = 3; //Ausgleich der Topposition zueinander
	TOPPOSNS = 6; //Ausgleich der Topposition zueinander in NS
	EXTRADIST = 152; // Abstand zwischen den Aufpoppenden Extralayern (sollte Layerbreite entsprechen)
	SHOWSUBLAYERTILLDEPTH = 2; //Navigationstiefe bis zu der die Unterlayer im linken Menue angezeigt werden sollen
	SHOWEXTRALAYERFROMDEPTH = 2;//Die Ebene, ab der ein Extralayer mit der Navigaiton angezeigt werden soll.

]]>
</xsl:template>


<xsl:template match="menue">
<![CDATA[ 

		switchOff =0;
		lastClicked=0;
		lastClickedParentLayerId=0;
		lastOpenSubLayerId='0_sub';
		lastClickedMainParentLayerId=0;

		isSubLayerOpen = -1;//Schalter: -1 ist false und +1 ist true

		//================Dieses Array ist der Info Stack =====================
		//Es enthaelt alle aktuell geklickten layerIds in chronologischer Reihenfolge
		info = new Array;
		//info[0] = 0;
		//===============Methoden zum Zugriff auf den Info Stack
		//Eine neue geklickte id dem Info Stack hinzufuegen
		function setNewClicked(layer){
		        //////alert('info.length before = '+info.length);
		         posInStack = 0;
		         if (info.length)posInStack = info.length;
		        //////alert('layer.id in setNewClicked = '+layer.id);
		        info[posInStack]=layer.id;
		        ////alert('info after inserting new id: '+info);
		}

		//den letzten geklickten Layer liefern
		function getLastClicked(){
		//        alert(info);
		        if (info[info.length-1]){
		           return document.getElementById(info[info.length-1]);
		        }
		        else return false;
		}

		//ueberpruefen ob ein Layer geklickt wurde und zur Zeit sich noch im geklickten Zustand befindet
		function infoContains(layer){
		      idToCheck = layer.id;
		      //////alert(idToCheck);
		        contains = false;
		        for (ii=0;ii<info.length;ii++){
		            if (info[ii]==idToCheck){
		               contains = true;
		               break;
		            }
		        }
		        return contains;
		}

		//ueberpruefen ob der Layer der letzte geklickte ist
		function isLastClicked(layer){
		        //alert(layer);
		        if (layer.id == info[info.length-1]) return true;
		        else return false;
		}

		//Den letzten Info Eintrag loeschen
		function deleteLastOutOfInfo(){
		         delete info[info.length-1];
		         info.length = info.length-1;
		}

		function clearInfo(){
		//          alert('Info lehren');
		         if (getLastClicked()!=''){
		            deleteLastOutOfInfo()
		//            alert('Info gelehrt');
		            clearInfo();
		         }
		}

		//================ Ende Methoden Info Stack


		//=============== Start Hilfsmethoden
		// Den Elternknoten liefern, wenn es einen gibt, wenn nicht false liefern
		function getParentLayer(layer){
		         //////alert('in Funktion getParentLayer'+layer.parentNode.nodeName);
		         if (layer.parentNode && layer.parentNode.nodeName == "DIV"){
		            //Wenn der Parentlayer der oberste ist:
		              if (layer.parentNode.getAttribute("id").indexOf('_sub')==-1){
		                this.id = 0;
		               return false;
		            }
		            else{parentLayerId = layer.parentNode.getAttribute("id").split("_sub")[0];
		                 parentLayer = document.getElementById(parentLayerId);
		                 this.id = parentLayerId;
		                 return parentLayer;
		            }
		         }
		         else return false;
		}

		// Die Ebenentiefe liefern
		function getDepth (layer){
		         //alert('layer.id = '+layer.id);
		         while (layer.id.indexOf('extra-')!=-1){
		            newId = layer.id.substring(6,layer.id.length);
		            layer = document.getElementById(newId);
		            layer.id = newId;
		         }
		         //alert('neu layer.id = '+layer.id);
		         depth=1;
		         while (getParentLayer(layer)){
		               depth +=1;
		               layer = getParentLayer(layer);
		               //////alert(depth);
		         }
		         return depth;
		}

		//Gibt den zu einem Layer gehoerenden, darunterliegenden Sublayer, wenn es einen gibt
		//wenn nicht wird false zurueckgegeben
		function getSubLayer(layer){
		//alert(layer);
		  /*     if (!layer.nextSibling){
		          return false;
		       }
		  */
		       if (layer.nextSibling!=null){
		                 subLayer = layer.nextSibling;
		                 //Da NS Whitespace zwischen zwei Knoten als zusaetzlichen Textknoten erkennt, wird hier ggf. zum naechsten Knoten gesprungen, der dann der gewuenschte DIV Sublayer ist.
		                 if (subLayer.nodeName != "DIV") subLayer = subLayer.nextSibling;
		                 if (subLayer==null) return false;
		                 //////alert('subLayer.id = '+ subLayer.id);
		                 if (subLayer.getAttribute('id').indexOf('_sub')!=-1){
		                    this.id = subLayer.getAttribute('id');
		                    //////alert('SUblayerID '+subLayer.getAttribute('id'));
		                    return subLayer;
		                 }
		                 else return false;
		       }
		       else return false;
		}

		//Gibt den Sublayer des Elternelements,also den Layer mit einem _sub in der id, indem sich der Unterpunkt befindet. WEnn es den nicht gibt, gibt die MEthode false zurueck
		function getParentSubLayer(layer){
		         if (layer.parentNode.nodeName == "div"){
		                 parentSubLayerId = layer.parentNode.getAttribute("id");
		                 parentSubLayer = document.getElementById(parentSubLayerId);
		                 //Wenn es nur den root Layer darueber gibt, false zurueckgeben
		                 if (parentSubLayerId == 0) return false;
		                 //Sonst den ParentSublayer zurueckgeben
		                 this.id = parentSubLayerId;
		                 return parentSubLayer;
		         }
		         //Wenn das ELternelement kein div Layer ist, false zurueckgeben
		         else return false;
		}

		function isMainLayer(layer){
		         if (layer.parentNode.nodeName == "DIV"  &&  layer.parentNode.getAttribute("id")==0)
		         return true;
		         //Wenn das ELternelement nicht der root Layer ist, false zurueckgeben
		         else return false;
		}


		function getLayer(layerId){
		         //if (!document.getElementById(layerId)) return false;
		         layer = document.getElementById(layerId);
		         this.id = layerId;
		         return layer;
		}

		//==================Ende Hilfsmethoden


		//==================Anfang Haupt Methode=======================================================

		function preOpener(layerIds){
		         ////alert ('in preOpener: '+layerIds);
		         toOpen = layerIds.split(",");
		         //////alert('array toOpen = '+toOpen);
		         //////alert('toOpen.length ='+toOpen.length);
		         for (n=toOpen.length-1;n>-1;n--){
		             //////alert(toOpen[n]);
		             highLightLeft(toOpen[n]);
		         }
		}

		function preOpenChecker(layer){
		         ////alert('in preOpenChecker');
		         //layerToCheck = getParentLayer(layer);
		         layerToCheck = layer;
		         layerIds = layerToCheck.id;
		         while (!infoContains(layerToCheck) && getParentLayer(layerToCheck)){
		         //      ////alert('In while Schleife preOpenChecker');
		               layerToCheck = getParentLayer(layerToCheck);
		               layerIds += ",";
		               layerIds += layerToCheck.id;
		         //      ////alert('layerIds = '+layerIds);
		         }
		         //////alert('layerIds after While Schleife = '+layerIds);
		         ////alert('info im preOpenChecker: ' +info);
		         if (layerIds!=''){
		            preOpener(layerIds);
		         }
		}

		function highLightLeft(layerId){
		         //Falls ein ExtraLAyer geoeffnet ist schliessen
		         if (switchOff!=0)close();
		         //alert('START highLightLeft: '+layerId);
		         //Wenn der gleiche Menuepunkt wie beim letzten Aufruf geklickt wird, gar nichts tun.
		         //if(layerId==lastClicked) return;
		         clickedLayer = getLayer(layerId);
		         if (isLastClicked(clickedLayer)) return;
		         //Wenn es die layerId in der HTML Seite nicht gibt, abbrechen
		         if (!document.getElementById(layerId)) return;
		         //parentLayerId detectieren
		         //parentLayerId = document.getElementById(layerId).parentNode.getAttribute("id");
		         parentLayer = getParentLayer(clickedLayer);
		         ////alert('parentLayer= '+parentLayer);
		         //Wenn ein Sublayer geoeffnet werden soll, zu dem es einen Mainlayer gibt, der noch nicht geoeffnet ist,                        preOpener oeffnen. Welche dabei geoeffnet werden sollen, wird im preOpenChecker festgestellt
		         if (parentLayer && !infoContains(parentLayer)){
		              preOpenChecker(clickedLayer);
		              //Das oeffnen des letzten Links wird ebenfalls vom preOpenChecker geloest
		              return;
		         }
		        /* if (parentLayer){
		                    if(!infoContains(parentLayer)){
		                    ////alert('Rekursiv');
		                    highLightLeft (parentLayer.id);
		                    }
		         }*/
		         //Funktionen aufrufen

		         closeSubLayers(clickedLayer);
		         openSubLayer(clickedLayer);
		         //alert('info: '+info);

		}

		function closeSubLayers(layer){
		//alert('In closeSubLayers');
		parentLayer = getParentLayer(layer);
		//Wenn die Funktion das erstemal ausgefuehrt wird, nichts machen
		if (!getLastClicked()) return;
		//Solange der Elternlayer nicht der letzte geoeffnete im Stack ist, die jeweiligen geoeffneten Layer schliessen
		//alert('Der Parent Layer vom geklickten = '+getParentLayer(layer).id);
		//alert('Der letzte geklickte Layer ist = '+getLastClicked().id);
		    lastClickedLayer = getLastClicked();
		    while((getParentLayer(layer) != lastClickedLayer)&&(layer!= lastClickedLayer)){
		             //alert('der letzte geklickte Layer ist nicht der gleiche wie der, der jetzt geklickt wurde und der ELternLayer ist auch nicht der letzte geklickte. Der Layer geschlossen');
		             //Wenn es einen Sublayer gibt, den schliessen
		             if (getSubLayer(getLastClicked())){
		                     //alert(getLastClicked());
		                     subLayer = getSubLayer(getLastClicked());
		                     subLayer.style.display='none';
		             }
		             removeOldStyle(getLastClicked());
		             deleteLastOutOfInfo();
		             lastClickedLayer = getLastClicked();
		    }

		}

		function openSubLayer(layer){
		        ////alert('in Funktion openSubLayer');
		        //Solange es ELternelemente gibt oder es sich um einen Hauptmenuepunkt handelt, den Unterlayer oeffnen
		        //////alert('layer.id ='+layer.id);
		        //////alert('getLastClicked().id = '+getLastClicked().id);
		        if (layer==getLastClicked()){
		        // ////alert('stop openSubLayer');
		         return;
		        }
		         if (getSubLayer(layer)){
		            subLayer = getSubLayer(layer);
		                     //alert('getDepth(layer)= '+getDepth(layer));
		                     //alert('SHOWSUBLAYERTILLDEPTH= '+SHOWSUBLAYERTILLDEPTH);
		                if (getDepth(layer)<SHOWSUBLAYERTILLDEPTH){
		                   subLayer.style.display='block';
		                }
		         }
		         getNewStyle(layer);
		         setNewClicked(layer);
		}



		//================ Methoden fuer den rechts aufpoppenden Layer
		//Methoden fuer Time Delay
		function noShowExtra(clickedLink){

		         if (infoContains(clickedLink.parentNode)) return;
		         switchOff = window.setTimeout("close()",750);
		//         alert('noShowExtra');
		}

		function clearSwitchOff(){
		         window.clearTimeout(switchOff);
		         //alert('clearSwitchOff');
		}

		function close(link){
		         for(i=1;i<=5;i++){
		                extraLayer = document.getElementById('extra_'+i);
		                with(extraLayer.style) {
		                        display="none";
		                        visibility="hidden";
		                }
		         }
		}
		
		function closeExtra() {
            extraLayer = document.getElementById('extra_2');
            with(extraLayer.style) {
                    display="none";
                    visibility="hidden";
            }		
		}
		
		//Ende Methoden Time Delay

                function viewExtra(topPos,leftPos,extraLayer){
                       //alert('in viewExtra');
                       //alert('extraLayer = '+extraLayer);
                       //alert('extraLayer.leftPos = '+extraLayer.leftPos);
                       //alert('topPos = '+topPos);

                       with(extraLayer.style) {
                                display="block";
                                visibility="visible";
                                position="absolute";
                              top  = topPos;
                               left  = leftPos;
                        }
                }

	
		function getNewExtraLayer(layerDepth,touchedLayer){
		         //alert('in getNewExtraLayer, layerDepth = '+layerDepth);
		         extraLayerId = "extra_"+layerDepth;
		         //alert(extraLayerId);
		         extraLayer = document.getElementById(extraLayerId);
		         //Die linke Position bestimmen, je nachdem, ob es von einem extra Layer oder einem Hauptlayer aufgerufen wird
		         if (touchedLayer.id.indexOf('extra-')==-1)
		         extraLayer.leftPos = LEFTPOS;
		         else
		         extraLayer.leftPos = LEFTPOS + EXTRADIST*(layerDepth-2);
		         return extraLayer;
		}



		// Replacer Methode fuer den HTML Text, der in den Extra Layer geschrieben werden soll
		function replacer(text){
		//In diesem Array wird als regexp gespeichert, was im HTML Text ersetzt werden soll
		//Der zu ersetzende Wert steht zwischen den beiden /
		//In ie werden die Attribute des innerHTML textes ohne " und in NS6 mit erfasst
		//Deshalb muss man die Attribute doppelt ersetzen
		       temp = new Array;
		       temp[0]= 'id=';
		       temp[1]= 'id=extra-"';
		       temp[2]= 'class=sub-link ';
		       temp[3]= 'class=sub-link-extra "';
		       temp[4]= 'class="sub-link"';
		       temp[5]= 'noShowExtra';
		       //temp[2]= /%/;

		//In diesem Array wird gespeichert mit was die ersetzten Textstuecke aus dem temp Array ersetzt werden soll
		       tempReplacer = new Array;
		       tempReplacer[0] ='id=extra-';
		       tempReplacer[1]= 'id="extra-';
		       tempReplacer[2] ='class=sub-link-extra ';
		       tempReplacer[3]= 'class="sub-link-extra';
		       tempReplacer[4]= 'class="sub-link-extra"';
		       tempReplacer[5]= 'extraNoShowExtra';

		       //tempReplacer[2]= "%25";

		        for (i=0;i<temp.length;i++){
		            textTempArray = text.split(temp[i]);
		            newText = textTempArray[0];
		            for (ii=1;ii<textTempArray.length;ii++){
		                    textTempArray[ii].slice(1);
		                    newText += tempReplacer[i]+textTempArray[ii];
		            }
		            text = newText;
		            //alert(text);
		        }
		 //alert(newText);
		return newText;
		}
		//==================Ende Methoden fuer den rechts aufpoppenden Layer

		//Leermethode fuer die extralayer
			function extraNoShowExtra(clickedExtraLink){
			return;
		}


]]>

</xsl:template>


<xsl:template match="checkHighLight">
 <![CDATA[ 
//================= Check ob das Menu richtig gehighlichted ist ==================
function checkHighLight(layerId){
         //alert (getLayer(layerId));
         if (!document.getElementById(layerId)) return;
         layerForLoadedPage = getLayer(layerId);;
         if (getLastClicked()== layerForLoadedPage)return;
         else highLightLeft(layerId);
}
]]>			
</xsl:template>

<xsl:template match="getUrlParam">
 <![CDATA[ 

//=================highlight Parameter in der URL auslesen und dahin springen

function getUrlParam(paramName){
        if (self.location.href.indexOf('highLightLeft=')!=-1){
             theUrlParams = self.location.href.split('?')[1].split('&');
             for (i=0;i<theUrlParams.length;i++){
                 if (theUrlParams[i].indexOf(paramName+'=')!=-1) {
                    paramValue = theUrlParams[i].split('paramName+'=')[1];
                    return paramValue;
                 }
             }
        }
}
]]>			
</xsl:template>

<xsl:template match="removeOldStyle">
 <![CDATA[ 
//==================== Anfang Methoden fuer den Style Wechsel
function removeOldStyle(layer){
         ////alert('in funktion removeOldStyle');
         //Wenn es sich um einen Hauptmenuepunkt handelt
         if (isMainLayer(layer)){
            layer.getElementsByTagName("a")[0].style.color='';
//            layer.style.backgroundImage='url(/esgh/img/kachel_gruen_6px.gif)';
         }
         //Wenn es sich um einen Untermenuepunkt handelt
         else {
            layer.getElementsByTagName("a")[0].style.color='';
             if (layer.id.indexOf('extra-')==-1){
//                 layer.style.backgroundImage='url(/esgh/img/kachel_gruen_2px.gif)';
             }
         }
}
]]>			
</xsl:template>

<xsl:template match="getNewStyle">
 <![CDATA[ 
function getNewStyle(layer){
         ////alert('in getNewStyle');
         //Wenn es sich um einen Hauptmenuepunkt handelt
         if (isMainLayer(layer)){
            layer.getElementsByTagName("a")[0].style.color=CLICKEDCOLOR;
//            layer.style.backgroundImage='url(/esgh/img/kachel_rot_6px.gif)';
         }
         //Wenn es sich um einen Untermenuepunkt handelt
         else {
             layer.getElementsByTagName("a")[0].style.color=CLICKEDCOLOR;
             if (layer.id.indexOf('extra-')==-1){
//                 layer.style.backgroundImage='url(/esgh/img/kachel_rot_2px.gif)';
             }
         }
}
]]>			
</xsl:template>

<!--
<xsl:template match="showExtra">
<![CDATA[ 
	function showExtra(link,e){
		        //alert('in showExtra');

		         if (switchOff!=0)clearSwitchOff();
		        layerId = link.parentNode.getAttribute('id');
		        //alert(layerId);
		        touchedLayer = document.getElementById(layerId);
		        touchedLayer.id = layerId;
		        //if (infoContains(touchedLayer)) return;
		        touchedLayerDepth = getDepth(touchedLayer);
		        //alert('touchedLayerDepth'+touchedLayerDepth);
		        if (document.getElementById(layerId+'_sub')){
		                touchedSublayer = document.getElementById(layerId+'_sub');
		        }
		        else {
		             return;
		        }
		        //Wenn die Sublayer im LeftFrame geoeffnet sind, dann keinen extralayer anzeigen
		        if (touchedSublayer.style.display=='block') return;
		        //Extralayer erst ab der definierten Ebene zeigen
		        if (touchedLayerDepth<SHOWEXTRALAYERFROMDEPTH) return;
		        text = touchedSublayer.innerHTML;
		        extraLayer = getNewExtraLayer(touchedLayerDepth,touchedLayer);
		        newText=replacer(text);
		       // alert(newText);
		        extraLayer.innerHTML = newText;
		        //alert('extralayer newText: '+newText);
		        topPos = getPosition(e);
		        viewExtra(topPos,extraLayer);
		}
]]>			
</xsl:template>-->


<xsl:template match="showExtra" priority="1">

<![CDATA[ 	
function showExtra(link,e){
        closeExtra();
        // alert('in showExtra');
         if (switchOff!=0)clearSwitchOff();
        layerId = link.parentNode.getAttribute('id');
        //alert(layerId);
        touchedLayer = document.getElementById(layerId);
        touchedLayer.id = layerId;
        //if (infoContains(touchedLayer)) return;
        touchedLayerDepth = getDepth(touchedLayer);
        touchedSublayer = document.getElementById(layerId+'_sub');
        //Wenn die Sublayer im LeftFrame geöffnet sind, dann keinen extralayer anzeigen
        if (touchedSublayer.style.display=='block') return;
        //Extralayer erst ab der definierten Ebene zeigen
        if (touchedLayerDepth<SHOWEXTRALAYERFROMDEPTH) return;
        text = touchedSublayer.innerHTML;
        extraLayer = getNewExtraLayer(touchedLayerDepth,touchedLayer);
        newText=replacer(text);
       // alert(newText);
        extraLayer.innerHTML = newText;
        //alert('extralayer newText: '+newText);
		depth = getDepth(touchedLayer);
        topPos = getTopPosition(e,depth);
        leftPos = getLeftPosition(e,depth);
        viewExtra(topPos,leftPos,extraLayer);
}
]]>
</xsl:template>

<xsl:template match="getPosition">
<![CDATA[ 

		//Position fuer den Extra Layer bestimmen,
		function getPosition (e){
		 //Fuer ie
		 if (document.all){
		         posToWindow = window.event.clientY;
		         posToElement = window.event.offsetY;
		         posAbsolute = posToWindow-posToElement+document.body.scrollTop-TOPPOS;
		 }

		 //Fuer NS/Mozilla
		 if (!document.all){
		         posToWindow = e.pageY;
		         posToElement = e.layerY;
		         //alert('e.pageY='+e.pageY+' self.pageYOffset='+self.pageYOffset+' e.layerY = '+e.layerY);
		         posAbsolute = posToWindow-TOPPOSNS;
		 }
		 //alert('posAbsolute='+posAbsolute);
		 return posAbsolute;
		}
		//Leermethode fuer die extralayer
		function extraNoShowExtra(clickedExtraLink){
		return;
		}

]]>			
</xsl:template>



<xsl:template match="getTopPosition" priority="1">
<![CDATA[ 
function getTopPosition (e,depth){
 //Fuer ie
 if (document.all){
         posToWindow = window.event.clientY;
         if (depth>1){		 
         	posToElement = window.event.offsetY;
			TOPPOSCORRECT3 =  TOPPOSCORRECT2;
		 }
		 else {
			posToElement = window.event.y;
			TOPPOSCORRECT3 =  TOPPOSCORRECT;
		 }
         posAbsolute = posToWindow-posToElement+document.body.scrollTop-TOPPOSCORRECT3;
		 //posAbsolute = posToWindow+document.body.scrollTop;
 }

  //Fuer NS/Mozilla
  if (!document.all) {

    current = e.currentTarget;
    
    posToElement = current.offsetTop;
    if (depth > 1) {
        posToWindow = e.clientY-e.layerY+window.pageYOffset;
        correction = 0;
    } else {
        posToWindow = current.offsetHeight;
        correction = 5;
    }
    
    posAbsolute = posToWindow+posToElement+correction;
  }

  return posAbsolute;
}
]]>
</xsl:template>

<xsl:template match="getLeftPosition" priority="1">
<![CDATA[ 
 function getLeftPosition (e,depth){
 //Fuer ie
 if (document.all){
         posToWindow = window.event.clientX;
   if (depth>1){		
		posToElement = window.event.offsetX;
		LEFTPOSCORRECT3 = LEFTPOSCORRECT2;
	}
	else {
       posToElement = window.event.x;
	   LEFTPOSCORRECT3 = LEFTPOSCORRECT;
	}

		// alert('window.event.x'+window.event.x);
		// alert('window.event.offsetX '+window.event.offsetX);
     	posAbsolute = posToWindow-posToElement+document.body.scrollLeft-LEFTPOSCORRECT3;
	    //posAbsolute = posToWindow+document.body.scrollLeft;
 }

  //Fuer NS/Mozilla
  if (!document.all) {
    current = e.currentTarget;
    
    if (depth > 1) {
        posToWindow = (e.clientX-e.layerX)+(current.offsetWidth+current.offsetLeft)+(window.pageXOffset);
        correction = 11;
    } else {
        posToWindow = current.offsetLeft;
        correction = 0;
    }
    posAbsolute = posToWindow+correction;
 }

 return posAbsolute;
}

]]>
</xsl:template>

<!-- sitemap -->
<xsl:template match="sitemap">
<![CDATA[

//============Funktion fuer Sitemap=============================
function showNoShow(layerId){
 x = document.getElementById('sitemap'+layerId).style.display;
 picName = "image_"+layerId;
 plus = new Image(); plus.src = "/httpd/img/sitemap/sitemap_plus.gif";
 down = new Image(); down.src = "/httpd/img/sitemap/sitemap_down.gif";
 if (x =='none'){
         if (document.getElementById('sitemap'+layerId).hasChildNodes() == false) {
           reloadSitemap(layerId);
         }
         document.getElementById('sitemap'+layerId).style.display='block';
         eval("document."+picName+".src = down.src");
 }
 else {
         document.getElementById('sitemap'+layerId).style.display='none';
         eval("document."+picName+".src = plus.src");
 }
}

//============ Funktionen fuer das dynamische Nachladen ===========
function reloadSitemap(layerId){
         if (document.getElementById('sitemap'+layerId).innerHTML==''){
                  reference = '/sitemapReloader/'+layerId+'/part.html';
                  window.reloader.location.href = reference;
         }
         else return;
}

function changeContent(targetLayer){
         textOrig = self.reloader.document.getElementById('reloadLayer').innerHTML;
         if (textOrig.indexOf('<!--')!=-1){
            text = textOrig.substring(4,textOrig.length-3);
         }
         else
         text = textOrig;
         document.getElementById('sitemap'+targetLayer).innerHTML = text;
}
//Nachladen mit navmodule

function reloadNavresource(layerId,language){
         if (document.getElementById('sitemap'+layerId).innerHTML==''){
                  //reference = '/sitemapReloader/'+layerId+'/part.html';
                  reference = '/navresource-'+layerId+'/'+language+'//sitemapPart.html';                      
                  window.reloader.location.href = reference;
         }
         else return;
}


]]>
</xsl:template>


 <!-- sitemap mit navressource reloader, neu 10.8.2005, Hato-->
 <xsl:template match="sitemapNav">
  <![CDATA[ 
 //============Funktion fuer Sitemap=============================
 function showNoShow(layerId,language){
 x = document.getElementById('sitemap'+layerId).style.display;
 picName = "image_"+layerId;
 plus = new Image(); plus.src = "/httpd/img/sitemap/sitemap_plus.gif";
 down = new Image(); down.src = "/httpd/img/sitemap/sitemap_down.gif";
 if (x =='none'){
 if (document.getElementById('sitemap'+layerId).hasChildNodes() == false) {
 reloadSitemap(layerId,language);
 }
 document.getElementById('sitemap'+layerId).style.display='block';
 eval("document."+picName+".src = down.src");
 }
 else {
 document.getElementById('sitemap'+layerId).style.display='none';
 eval("document."+picName+".src = plus.src");
 }
 }
 
 //============ Funktionen fuer das dynamische Nachladen ===========
function reloadSitemap(layerId,language){
         if (document.getElementById('sitemap'+layerId).innerHTML==''){
                  //reference = '/sitemapReloader/'+layerId+'/part.html';
                  reference = '/navresource-'+layerId+'/'+language+'/url/sitemapPart.html';                 
                  window.reloader.location.href = reference;
         }
         else return;
}
 
 function changeContent(targetLayer){
 textOrig = self.reloader.document.getElementById('reloadLayer').innerHTML;
 if (textOrig.indexOf('<!--')!=-1){
            text = textOrig.substring(4,textOrig.length-3);
         }
         else
         text = textOrig;
         document.getElementById('sitemap'+targetLayer).innerHTML = text;
}



]]>
</xsl:template>

<xsl:template match="toggle">
<![CDATA[
    function toggle(id) {
      if (document.getElementById(id).style.display == "none") {
        document.getElementById(id).style.display="";
      } else {
        document.getElementById(id).style.display="none";
      }
    }
]]>
</xsl:template>

<xsl:template match="extToggle">
<![CDATA[
    function extToggle(id, styleLo, styleHi) {
      if (document.getElementById(id).style.display == "none") {
        document.getElementById(id).style.display="";
        document.getElementById('container-'+id).setAttribute("class",styleHi);
      } else {
        document.getElementById(id).style.display="none";
        document.getElementById('container-'+id).setAttribute("class",styleLo);
      }
    }
]]>

</xsl:template>
    
<!-- Für toggle Popups die über Dropdowns auftauchen -->
<xsl:template match="toggleWithIframe">
<![CDATA[
    function toggleWithIframe(id, iframeId) {
        var layerObj = document.getElementById(id);
        var iframeObj = document.getElementById(iframeId);
        
        if(layerObj && iframeObj) {
            if (layerObj.style.display == "none") {
				setObjVisible(iframeObj, true);
				setObjVisible(layerObj, true);
                var layerObjSize = getObjSize(layerObj);
                var layerObjPos = getObjPosition(layerObj);
				setObjSize(iframeObj, layerObjSize);
				setObjPosition(iframeObj, layerObjPos);
            } else {
				setObjVisible(layerObj, false);
				setObjVisible(iframeObj, false);
            }
        } else {
            return false;
        }
    }
    
	function getObjPosition(obj) {
		var objPosition = new Array();
		objPosition["top"] = obj.offsetTop;
		objPosition["left"] = obj.offsetLeft; 
		return objPosition;
	}
    
	function setObjPosition(obj, position) {
		obj.style.left = position["left"];
		obj.style.top = position["top"];
	}
	
	function setObjVisible(obj, visible) {
		if (visible) {
			obj.style.display = 'block';
		} else {
			obj.style.display = 'none';
		}
	}
	
	function getObjSize(obj) {
		var objSize = new Array();
		objSize["width"] = obj.offsetWidth;
		objSize["height"] = obj.offsetHeight; 
		return objSize;
	}
	
	function setObjSize(obj, size) {
		obj.style.width = size["width"];
		obj.style.height = size["height"];
	}
	
	function mergeToPxValue(obj) {
		return obj + 'px';
	}
]]>
</xsl:template>
<!--
 <xsl:template match="iframe">
  <![CDATA[
  function resize_me(n)() {
  d=5;ifobj=document.getElementsByName(n)[0];p=(document.all)?'scroll':'offset;
  eval("ifobj.style.width=window.frames[n].document.getElementsByTagName('body')[0]."+p+"width+"+d);
  eval("ifobj.style.height=window.frames[n].document.getElementsByTagName('body')[0]."+p+"width+"+d);
  }
]]>
 </xsl:template>
 -->
 
 <xsl:template match="diashow">
  <![CDATA[  
//Diashow Funktionen

var shownImgPos = 0; 

function changeImg(imagePos,infos) {
    src = imageArray[imagePos];
	newSrc = "/img/ejbimage/image.jpg?id=" + src + "&typ=s";		
	bild = new Image();
	bild = document.getElementById('diaImg');
	bild.src = newSrc;
	if(infos=='') { // falls bu nicht vorhanden, leerzeichen
	    document.getElementById('diaInfos').innerHTML = "&nbsp;";
	}else {
	    document.getElementById('diaInfos').innerHTML = infos;
	}
	stopSlideShow();
	slideShow();
	
}

function changenumbers(width){
    numbersWidth = width;
    document.getElementById('numbers').style.width = numbersWidth;
}   
function showImg(imagePos){    
    //Image Informationen aus den hidden fields des imageProp Formulars holen
    infos = document.getElementById("infos_"+imagePos).innerHTML;
    //Image darstellen
    changeImg(imagePos,infos);
    //link highlighten
	thumbNail = document.getElementById(imagePos);
	thumbNail.style.borderColor=linkColor;
	//alten border wieder auf weiss
	document.getElementById(shownImgPos).style.borderColor="white";
    //diaHighlight(id);
    //speichern, welches Image gerade angezeigt wird. 
    shownImgPos = parseFloat(imagePos);
}

function diaHighlight(id){
    if (shownImgPos!=0){
        linkalt = document.getElementById(shownImgId);
        linkalt.style.color=linkColorDefault;
    }    
    link = document.getElementById(id);
    link.style.color=linkColor;
}
  
function getMaxCount(){
    maxCount = imageArray.length-1;
    return maxCount;
}
function nextpic() {
    maxCount = getMaxCount();
    imagePos = shownImgPos + 1;
    if (imagePos>maxCount) imagePos =0;
    showImg(imagePos);
}

function prevpic(){
    maxCount = getMaxCount();
    imagePos = shownImgPos - 1;
    if (imagePos==-1) imagePos = maxCount;
    showImg(imagePos);
}



var aktiv ;

function slideShow(){
    aktiv = window.setInterval("nextpic()",hiddeninterval);
}
function stopSlideShow(){
  window.clearInterval(aktiv);
    try{
        window.clearInterval(aktiv);
    }
    catch(e){}    
}
  
  ]]>  
  
 </xsl:template>
 
 <xsl:template match="diashow2">
  <![CDATA[  
//Diashow Funktionen

var shownImgPos = 0; 
var aktiv ;

function changeImg(imagePos) {
    src = imageArray[imagePos];
	newSrc = "/img/ejbimage/image.jpg?id=" + src + "&typ=s";		
	bild = new Image();
	bild = document.getElementById('diaImg');
	bild.src = newSrc;
	if(infos=='') { // falls bu nicht vorhanden, leerzeichen
	    document.getElementById('diaInfos').innerHTML = "&nbsp;";
	}else {
	    document.getElementById('diaInfos').innerHTML = infos;
	}
	stopSlideShow();	
	slideShow();
}

function changenumbers(width){
    numbersWidth = width;
    document.getElementById('numbers').style.width = numbersWidth;
}   
function showImg(imagePos){
    
    //Image Informationen aus den hidden fields des imageProp Formulars holen
    infos = document.getElementById("infos_"+imagePos).innerHTML;
   
    //Image darstellen
    changeImg(imagePos,infos);
    
    //speichern, welches Image gerade angezeigt wird.
    shownImgPos = parseInt(imagePos);
    
    //link highlighten
	//thumbNail = document.getElementById(imagePos);
	//thumbNail.style.borderColor=linkColor;
	
	//alten border wieder auf weiss
	//document.getElementById(shownImgPos).style.borderColor="white";
	
    //diaHighlight(id);
}

function diaHighlight(id){
    if (shownImgPos!=0){
        linkalt = document.getElementById(shownImgId);
        linkalt.style.color=linkColorDefault;
    }    
    link = document.getElementById(id);
    link.style.color=linkColor;
}
  
function getMaxCount(){
    maxCount = imageArray.length-1;
    return maxCount;
}
function nextpic() {
   
    maxCount = getMaxCount();
    imagePos = shownImgPos + 1;
    
    if (imagePos > maxCount) {
     imagePos =0;
    }
    showImg(imagePos);  
}

function prevpic(){
    maxCount = getMaxCount();
    imagePos = shownImgPos - 1;
    if (imagePos==-1) imagePos = maxCount;
    showImg(imagePos);
}

function slideShow(){
    aktiv = window.setInterval("nextpic()",parseInt(hiddeninterval));
}

function startSlideShow() {
    showImg(0);
}

function stopSlideShow(){
  window.clearInterval(aktiv);
    /*try{
        window.clearInterval(aktiv);
    }
    catch(e){
    } */   
}

function firstpic() {
    showImg(0);
}

function lastpic() {
    showImg(getMaxCount());
}
  
  ]]>  
  
 </xsl:template>
 
 
 <xsl:template match="ajax" priority="1.5">
  <![CDATA[
        var http_request = false;
        
        
        function makeRequest(url, func) {
            
            http_request = false;
        
            if (window.XMLHttpRequest) { // Mozilla, Safari,...
                http_request = new XMLHttpRequest();
                if (http_request.overrideMimeType) {
                    http_request.overrideMimeType('text/xml');
                    // zu dieser Zeile siehe weiter unten
                }
            } else if (window.ActiveXObject) { // IE
                try {
                    http_request = new ActiveXObject("Msxml2.XMLHTTP");
                } catch (e) {
                    try {
                        http_request = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e) {}
                }
            }
            
            if (!http_request) {
                alert('Ende :( Kann keine XMLHTTP-Instanz erzeugen');
                return false;
            }
            http_request.onreadystatechange = func;
            http_request.open('GET', url, true);
            http_request.send(null);
            
        }
        
        function response() {
            if (http_request.readyState == 4) {
                if (http_request.status == 200) {
                    changeContent(http_request.responseText);
                } else {
                    alert('Bei dem Request ist ein Problem aufgetreten.');
                }
            }
        }
        
        
        
         ]]>
 </xsl:template>
    <xsl:template match="ajaxsitemap">
        <![CDATA[
        
        var id = 0;
        
        function showNoShow(myId, language) {
            id=myId;
            makeRequest('/navresource-'+id+'/'+language+'/url/sitemapPartAjax.xhtml', response);
        }
        
        function changeContent(xml){
            document.getElementById('sitemap'+id).innerHTML = xml;
            toggleVisibilityAndImage(id);
        }
        
        function toggleVisibilityAndImage(id) {
            divId = 'sitemap' + id;
            imageName = 'image_' + id;
            if (document.getElementById(divId).style.display == "none") {
                document.getElementById(divId).style.display="block";
                document.images[imageName].src = "/httpd/img/sitemap/sitemap_down.gif";
                document.images[imageName].alt = "open";
            } else {
                document.getElementById(divId).style.display="none";
                document.images[imageName].src= "/httpd/img/sitemap/sitemap_plus.gif";
                document.images[imageName].alt="closed";
            }
        }
        ]]>
    </xsl:template>
 
 
</xsl:stylesheet>