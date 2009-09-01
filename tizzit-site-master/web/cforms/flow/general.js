
importPackage(org.apache.commons.jxpath);
importClass(org.apache.xpath.XPathAPI);
importClass(javax.xml.parsers.DocumentBuilderFactory);
importClass(org.w3c.dom.Document);
importClass(org.w3c.dom.Node);
importClass(org.w3c.dom.Element);
importClass(org.w3c.dom.NodeList);
importClass(java.lang.String);

var log = Packages.org.apache.log4j.Logger.getLogger("general_flowscript");
var	hostname = cocoon.parameters["hostname"];
var msg = "";

// Load a document from an URI, absolute or relative to the current sitemap
function loadDocument(uri) {
	var resolver = cocoon.getComponent( "org.apache.excalibur.source.SourceResolver");
	var source = resolver.resolveURI(uri);
	try {
		var document = Packages.org.apache.cocoon.components.source.SourceUtil.toDOM(source);
	} finally {
		resolver.release(source);
		cocoon.releaseComponent(resolver);
	}
	return document;
}

function setUtf(){
	cocoon.request.setCharacterEncoding("utf-8");
}

function getRequestCharacterEncoding() {
  return cocoon.request.getCharacterEncoding();
}


function getMailMessage(bizdata){
			msg = "";
			if (hasValue(bizdata.anfrageart))    			msg +="Anfrage Art: "+bizdata.anfrageart+"\n";
			if (hasValue(bizdata.anrede))    				msg +="Anrede: "+bizdata.anrede+"\n";
			if (hasValue(bizdata.titel))    				msg +="Titel: "+bizdata.titel+"\n";
			if (hasValue(bizdata.surname))	        		msg +="Name: "+bizdata.surname+"\n";
			if (hasValue(bizdata.name))	        			msg +="Name: "+bizdata.name+"\n";
			if (hasValue(bizdata.firstname))	        	msg +="Name: "+bizdata.firstname+"\n";
			if (hasValue(bizdata.vorname))    				msg +="Vorname: "+bizdata.vorname+"\n";
			if (hasValue(bizdata.geschlecht))   			msg +="Geschlecht: "+bizdata.geschlecht+"\n";
			if (hasValue(bizdata.funktion ))    			msg +="Funktion: "+bizdata.funktion+"\n";
			if (hasValue(bizdata.company ))    				msg +="Firma: "+bizdata.company+"\n";
			if (hasValue(bizdata.firma ))    				msg +="Firma: "+bizdata.firma+"\n";
			if (hasValue(bizdata.strasse))    				msg +="Strasse: "+bizdata.strasse+"\n";
			if (hasValue(bizdata.plz))        				msg +="PLZ: "+bizdata.plz+"\n";
			if (hasValue(bizdata.ort))        				msg +="Ort: "+bizdata.ort+"\n";
			if (hasValue(bizdata.email))	    			msg +="E-Mail: "+bizdata.email+"\n";
			if (hasValue(bizdata.tel))	        			msg +="Telefon: "+bizdata.tel+"\n";
			if (hasValue(bizdata.kontakt))         			msg += "Kontakt: "+bizdata.kontakt+"\n";
			if (hasValue(bizdata.tel2))        				msg +="Telefon (gesch�ftlich): "+bizdata.tel2+"\n";
			if (hasValue(bizdata.fax))        				msg +="FAX: "+bizdata.fax+"\n";
			if (hasValue(bizdata.handy ))    				msg +="Handy: "+bizdata.handy+"\n";
			if (hasValue(bizdata.infotext))    				msg +="Nachricht: "+bizdata.infotext+"\n";
			if (hasValue(bizdata.bday))        				msg +="Geburtsdatum: "+bizdata.bday+"\n";
			if (hasValue(bizdata.blutgruppe ))				msg +="Blutgruppe: "+bizdata.blutgruppe+"\n";
			if (hasValue(bizdata.url ))        				msg +="URL: "+bizdata.url+"\n";
			if (hasValue(bizdata.unit ))    				msg +="Einrichtung: "+bizdata.unit+"\n";
			if (hasValue(bizdata.mitglied ))    			msg +="Art: Mitglied"+"\n";
			if (hasValue(bizdata.krankenhausname ))    		msg +="Krankenhaus: "+bizdata.krankenhausname+"\n";
			if (hasValue(bizdata.fachgebiet ))    			msg +="Abteilung: "+bizdata.fachgebiet+"\n";
			if (hasValue(bizdata.spende ))    				msg +="Art: Spende"+"\n";
			if (hasValue(bizdata.betrag ))    				msg +="Hoehe der Spende: "+bizdata.betrag+" Euro\n";
			if (hasValue(bizdata.jahrestext ))    			msg +="Jahresbeitrag: "+bizdata.jahrestext+" Euro\n";
			if (hasValue(bizdata.zahlungsart ))    			msg +="Zahlungsart: "+bizdata.zahlungsart+"\n";
			if (hasValue(bizdata.institutsbezeichnung ))   	msg +="Institut: "+bizdata.institutsbezeichnung+"\n";
			if (hasValue(bizdata.bankleitzahl ))    		msg +="Institut: "+bizdata.bankleitzahl+"\n";
			if (hasValue(bizdata.kontobezeichnung ))    	msg +="Institut: "+bizdata.kontobezeichnung+"\n";
			if (hasValue(bizdata.ffwoher ))    				msg +="Ich kenne Sie von: "+bizdata.ffwoher+"\n";
			if (hasValue(bizdata.anzahl ))    				msg +="Ich kenne Sie von: "+bizdata.anzahl+"\n";
            if (hasValue(bizdata.order))                    msg += "Bestellung von: "+bizdata.order+"\n";
			if (hasValue(bizdata.schilderanzahl))           msg += "Bestellung von "+bizdata.schilderanzahl+" Schild/Schildern\n";
			if (hasValue(bizdata.zuweiserpraxis))           msg += "Bestellung (Zuweiserpraxis): "+bizdata.zuweiserpraxis+"\n";
            if (hasValue(bizdata.pin))                      msg += "Memberno: "+bizdata.pin+"\n";
            if (hasValue(bizdata.diagnose))                 msg += "Diagnose: "+bizdata.diagnose+"\n";
            if (hasValue(bizdata.briefingDoc))              msg += "Einweisender Arzt: "+bizdata.briefingDoc+"\n";
            if (hasValue(bizdata.confirmDoc))               msg += "Der einweisende Arzt soll benachrichtigt werden. \n";
            if (hasValue(bizdata.rpbestell))                msg += "Bestellung: Schuppenflechte - Die Erkrankung verstehen\n";
			if (hasValue(bizdata.rpbestell2))               msg += "Bestellung: Schuppenflechte - Die Erkrankung behandeln\n";
			if (hasValue(bizdata.rpbestell3))               msg += "Bestellung: Schuppenflechte - Die Therapie mit Biologics\n";
			if (hasValue(bizdata.rpbestell4))               msg += "Bestellung: Schuppenflechte - Mit der Erkrankung leben\n";
			if (hasValue(bizdata.rpbestell5))               msg += "Bestellung: Broschüre Spritzen mit Raptiva® leicht gemacht\n";
			if (hasValue(bizdata.rpbestell6))               msg += "Bestellung: DVD Die Therapie mit Raptiva®\n";
			if (hasValue(bizdata.rpbestell7))               msg += "Bestellung: Raptiva®-Aufbewahrungsbox\n";
			if (hasValue(bizdata.rpbestell8))               msg += "Bestellung: Spritzenentsorgungsbehälter\n";
			if (hasValue(bizdata.rpbestell9))               msg += "Bestellung: Buch 'Psoriasis - Mit Biologics der Haut helfen'\n";
			if (hasValue(bizdata.rpbestell10))              msg += "Bestellung: Heft 'Therapiebegleitheft für Raptiva® Patienten'\n";
			if (hasValue(bizdata.rpbestell11))              msg += "Bestellung: Therapie-Jahrbuch für Raptiva® Patienten\n";
			if (hasValue(bizdata.rpbestellall))             msg += "Bestellung: Alles bestellen\n";
			if (hasValue(bizdata.plakat))            		msg += "Plakat\n";
			if (hasValue(bizdata.alter))            		msg += "Alter\n";
			if (hasValue(bizdata.rpquest1))            		msg += "Antwort 1: "+bizdata.rpquest1+"\n";
			if (hasValue(bizdata.rpquest2))            		msg += "Antwort 2: "+bizdata.rpquest2+"\n";
			if (hasValue(bizdata.rpquest3))            		msg += "Antwort 3: "+bizdata.rpquest3+"\n";
			
			if (hasValue(bizdata.patientenname))    		msg +="Name des Patienten: "+bizdata.patientenname+"\n";
			if (hasValue(bizdata.anredearzt))    			msg +="Anrede des Arztes: "+bizdata.anredearzt+"\n";
			if (hasValue(bizdata.titelarzt))           		msg +="Titel des Arztes: "+bizdata.titelarzt+"\n";
			if (hasValue(bizdata.namearzt))	    	        msg +="Name des Arztes: "+bizdata.namearzt+"\n";
			if (hasValue(bizdata.vornamearzt))    			msg +="Vorname des Arztes: "+bizdata.vornamearzt+"\n";
			if (hasValue(bizdata.strassearzt))    			msg +="Strasse des Arztes: "+bizdata.strassearzt+"\n";
			if (hasValue(bizdata.plzarzt))        			msg +="PLZ des Arztes: "+bizdata.plzarzt+"\n";
			if (hasValue(bizdata.ortarzt))        		    msg +="Ort des Arztes: "+bizdata.ortarzt+"\n";
			
			//if (hasValue(bizdata.redaktion-field ))			msg +="Redaktion: "+bizdata.redaktion-field+"\n";
			if (hasValue(bizdata.ginsenginfo ))    			msg +="Infomaterial - FloraFarm Ginseng: Ja\n";
			if (hasValue(bizdata.ginsenginfo2 ))    		msg +="Infomaterial - Angebote fuer Gruppen: Ja\n";
			if (hasValue(bizdata.name2))    				msg +="Name des Patienten: "+bizdata.name+"\n";
			if (hasValue(bizdata.arztbezeichnung))	       	msg +="Hausarzt Name: "+bizdata.arztbezeichnung+"\n";
			if (hasValue(bizdata.arztstreet))    			msg +="Hausarzt Strasse: "+bizdata.arztstreet+"\n";
			if (hasValue(bizdata.arztplz))	    			msg +="Hausarzt PLZ: "+bizdata.arztplz+"\n";
			if (hasValue(bizdata.arztort)) 	   				msg +="Hausarzt Ort: "+bizdata.arztort+"\n";
			
			if (hasValue(bizdata.traeger))    				msg +="Kostentraeger: "+bizdata.traeger+"\n";
			if (hasValue(bizdata.Chefarztwahl))    			msg +="Wahlaerztliche Leistungen, Kostentraeger : "+bizdata.Chefarztwahl+"\n";
			if (hasValue(bizdata.OneRoom))    				msg +="Unterbringung in einem 1-Bett-Zimmer, Kostentraeger : "+bizdata.OneRoom+"\n";
			if (hasValue(bizdata.TwoRoom))    				msg +="Unterbringung in einem 2-Bett-Zimmer, Kostentraeger : "+bizdata.TwoRoom+"\n";
			if (hasValue(bizdata.anfragetext)) 				msg +="Anfrage: "+bizdata.anfragetext+"\n";
			if (hasValue(bizdata.neuroname))    			msg +="Neurologen Name: "+bizdata.neuroname+"\n";
			if (hasValue(bizdata.neurostreet))    			msg +="Neurologen Strasse: "+bizdata.neurostreet+"\n";
			if (hasValue(bizdata.neuroplz))	    			msg +="Neurologen PLZ: "+bizdata.neuroplz+"\n";
			if (hasValue(bizdata.neuroort)) 	   			msg +="Neurologen Ort: "+bizdata.neuroort+"\n";
			if (hasValue(bizdata.KHbezeichnung)) 	   		msg +="Wer ist der Kostentraeger: "+bizdata.KHbezeichnung+"\n";
			if (hasValue(bizdata.Voruntersuchungenbezeichnung))		msg +="Voruntersuchung: "+bizdata.Voruntersuchungenbezeichnung+"\n";
			//feedback
			if (hasValue(bizdata.feedback))            		msg += "Name: "+bizdata.feedback+"\n";
			if (hasValue(bizdata.feedback_name))            msg += "Name: "+bizdata.feedback_name+"\n";
			if (hasValue(bizdata.feedback_email))           msg += "E-Mail: "+bizdata.feedback_email+"\n";
			if (hasValue(bizdata.feedback_firstimpression)) msg += "Erster Eindruck: "+bizdata.feedback_firstimpression+"\n";
			if (hasValue(bizdata.feedback_design))          msg += "Design: "+bizdata.feedback_design+"\n";
			if (hasValue(bizdata.feedback_usability))       msg += "Benutzerfuehrung: "+bizdata.feedback_usability+"\n";
			if (hasValue(bizdata.feedback_information))     msg += "Informationsgehalt: "+bizdata.feedback_information+"\n";
			if (hasValue(bizdata.feedback_comment))         msg += "Kommentar: "+bizdata.feedback_comment+"\n";
			
			// Grusskarten
			if (hasValue(bizdata.grusskartenMotiv))        	msg += "Umschlagmotiv: "+bizdata.grusskartenMotiv+"\n";
			if (hasValue(bizdata.grusskarten7motiv))        msg += "Umschlagmotiv: "+bizdata.grusskarten7motiv+"\n";
			if (hasValue(bizdata.grusskartenAbsender))     	msg += "Absendername: "+bizdata.grusskartenAbsender+"\n";
			if (hasValue(bizdata.grusskartenEmail))        	msg += "Absenderadresse: "+bizdata.grusskartenEmail+"\n";
			if (hasValue(bizdata.grusskartenPatientenname))	msg += "Empfaenger: "+bizdata.grusskartenAnrede+" "+bizdata.grusskartenPatientenname+"\n";
			if (hasValue(bizdata.grusskartenStation))      	msg += "Abteilung: "+bizdata.grusskartenStation+"\n";
			if (hasValue(bizdata.grusskartenNachricht))    	msg += "Grusstext: "+bizdata.grusskartenNachricht+"\n";
			if (hasValue(bizdata.health_insurance))			msg += "Krankenkasse: "+bizdata.health_insurance+"\n";
			
 
			return msg;
}

function addMailText(txt){
    if(txt!=''){
        msg += txt+"\n";
    }
    return msg;
}


function hasValue(param){
	var	hasValue = false;
	try{
	if ((param != "null") && (param != "Null") &&	(param != "Undefined") &&	(param != "undefined") &&	(param != "") && (param)){
		hasValue=true;
		log.warn("hasValue = true, param= "+param);
	}
	}
	catch(e){
	    log.warn("cannot check param hasValue: "+e);
	}
	return hasValue;
}