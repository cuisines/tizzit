/* CAttachment */
function CAttachment(logname, uploadWidget) {

	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);
	this.log.debug ("new CAttachment Object instanciated");
	log.debug ("logname = "+logname);
		
	this.filename = "";
	this.bytes = "";
	this.mimetype = "";
	this.hasData = false;
	this.uploadWidget = uploadWidget;
		
    this.getMimetype = function(){
        return this.mimetype ;
    }
	this.getFilename = function(){
        return this.filename;
    }    
    this.getBytes = function(){
        return this.bytes;
    }
    
    
     
    this.readWidget = function(){
        if(this.uploadWidget.getValue() != null){
            this.readFilename();        
            this.readMimetype();
            this.readBytes();
            log.debug("header: "+this.uploadWidget.getValue().getHeaders().toString());
    	    log.debug("mimetype: "+this.mimetype);
    	    log.debug("uploadBytes: "+this.bytes);
	    }
    }    
    this.readFilename = function(){
       var filename = this.uploadWidget.getValue().getHeaders().get("filename");
	   filename = filename.substring(filename.lastIndexOf("\\")+1);
	   this.filename = filename;	  
    }   
    this.readMimetype = function(){
        this.mimetype = this.uploadWidget.getValue().getHeaders().get("content-type");	  
	}
    this.readBytes = function(){
          var mediaBytes = java.lang.reflect.Array.newInstance(java.lang.Byte.TYPE, 2048);
          var bos = new java.io.ByteArrayOutputStream(2048);
        
          try{         
              if (this.uploadWidget.getValue() != null) {
                var stream = uploadWidget.getValue().getInputStream();
                var inByteStream = new java.io.BufferedInputStream(stream);
                var len;
                while ((len = inByteStream.read(mediaBytes)) != -1) {
                    bos.write(mediaBytes, 0, len);
                }            
                this.bytes = bos.toByteArray();
                this.hasData = true;
              } else {
                  this.hasData = false;
              }
           }catch(e){
               log.error("Attachment Error "+e)
           }          
    }    
}