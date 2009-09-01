/* CMail */
function CMail(mailctx, logname) {
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	this.mailCtx = mailctx;
	
	this.encoding = "UTF-8";
	this.subject = "";
	this.from = "";
	this.body = "";
	this.to = new Array();
	this.cc = new Array();
	this.bcc = new Array();

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
				msg.setContent(new Packages.java.lang.String(this.body.toString().getBytes(),this.encoding),"text/plain; charset="+this.encoding);
				Transport.send(msg);
			} catch(e) {
				this.log.error("sending mail failed.\n"+
				               e.getMessage()+"\n"+
				               "configuration was:\n"+
				               "from: "+this.from+"\n"+
				               "to: "+this.to.join(";")+"\n"+
				               "cc: "+this.cc.join(";")+"\n"+
				               "bcc: "+this.bcc.join(";"));
				result = false;
			}
		}
		return result;
	}
}