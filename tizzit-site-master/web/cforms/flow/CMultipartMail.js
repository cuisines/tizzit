/* CMultipartMail */
function CMultipartMail(mailctx, logname) {

	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	this.mailCtx = mailctx;
	
	this.encoding = "UTF-8";
	this.subject = "";
	this.from = "";
	this.body = "";
	this.to = new Array();
	this.cc = new Array();
	this.bcc = new Array();
	this.attachments = new Array(); 

    this.addAttachment = function(attachment){
    	this.attachments.push(attachment);
    }
    
    this.setAttachments = function(attachments) {
    	this.attachments = attachments;
    }
    
    this.getAttachments = function(){
        return this.attachments;
    }    
	this.setEncoding = function(encoding) {
		this.encoding = encoding;
	}
	this.getEncoding = function() {
		return this.encoding;
	}

	this.setSubject = function(subject) {
		this.subject = subject;
	}
	this.getSubject = function() {
		return this.subject;
	}
	
	this.setFrom = function(from) {
		this.from = from;
	}
	this.getFrom = function() {
		return this.from;
	}	

	this.addTo = function(to) {
		this.to.push(to);
	}
	this.addCc = function(cc) {
		this.cc.push(cc);
	}
	this.addBcc = function(bcc) {
		this.bcc.push(bcc);
	}
	
	this.setBody = function(body) {
		this.body = body;
	}
	this.getBody = function() {
		return this.body;
	}
	this.appendBody = function(part) {
		this.body += part;
	}
	
	this.isMailSendable = function() {
		return (
				!("".toString().equals(this.mailCtx)) &&
				!("".toString().equals(this.subject)) &&
  				!("".toString().equals(this.from)) &&
  				(this.to.length > 0)
  			   );
	}
	
	this.sendMail = function() {
	    importPackage(Packages.javax.mail);
        importPackage(Packages.javax.naming);
        importPackage(Packages.javax.mail.internet);
        importPackage(Packages.javax.activation);
        importPackage(Packages.de.juwimm.util);
    
		var result = this.isMailSendable();
		
		if (this.log.isDebugEnabled()) {
			this.log.debug("prepare sending mail");
			this.log.debug("from: "+this.from);
			this.log.debug("to: "+this.to.join(";"));
			this.log.debug("cc: "+this.cc.join(";"));
			this.log.debug("bcc: "+this.bcc.join(";"));
			this.log.debug("subject: "+this.subject);
			this.log.debug("body: "+this.body);
			this.log.debug("encoding: "+this.encoding);
			this.log.debug("body-encoding: text/plain; charset="+this.encoding);
			this.log.debug("attachments: "+this.attachments.join(";"));
		}
		
		if (result) {
			try {
				var ctx = new InitialContext();
				var mailSession = ctx.lookup(this.mailCtx);
				
				var msg = new MimeMessage(mailSession);
		    	msg.setFrom(new InternetAddress(this.from));
				for (var i=0; i<this.to.length; i++) msg.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to[i]));
				for (var i=0; i<this.cc.length; i++) msg.addRecipient(Message.RecipientType.CC, new InternetAddress(this.cc[i]));
				for (var i=0; i<this.bcc.length; i++) msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(this.bcc[i]));
				msg.setSubject(new Packages.java.lang.String(this.subject.toString().getBytes(),this.encoding));
						
                //create MimeMultipart
                var mimemultipart = new MimeMultipart();
                //create body part with mailtext
			    var mbp = new MimeBodyPart();
                mbp.setText(this.body.toString(), this.encoding);
                mimemultipart.addBodyPart(mbp);
			    //add body part with attachment
			    for (var i=0; i<this.attachments.length; i++) {
			    	if (this.attachments[i].hasData) {
                    	var messageBodyPart = new MimeBodyPart();
                    	messageBodyPart.setDisposition("attachment");
                    	messageBodyPart.setFileName(this.attachments[i].getFilename());
                    	messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(this.attachments[i].getBytes(), this.attachments[i].getMimetype())));        
                    	mimemultipart.addBodyPart(messageBodyPart);
                	}
                }
                msg.setContent(mimemultipart);
				Transport.send(msg);
			} catch(e) {
				this.log.error("sending mail failed.\n"+ e);				                
				result = false;
			}
		}
		return result;
	}
}