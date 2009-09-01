/* CTextTemplate */
function CTextTemplate(template, prefix, suffix, logname) {
	this.log = Packages.org.apache.log4j.Logger.getLogger(logname);

	this.prefix = prefix;
	this.suffix = suffix;
	this.template = template;
	
	this.assigned = new CHashMap();
	
	this.setTemplate = function(template) {
		this.template = template;
	}
	this.getTemplate = function() {
		return this.template;
	}
	this.assign = function (key, content) {
		this.assigned.add(key, (content != null) ? content : "");
	}
	
	this.getText = function() {
		var result = this.template;
		var map = this.assigned.getMap();
		
		for(var i in map) {
			result = result.toString().replaceAll(prefix+i.toString().toUpperCase()+suffix,map[i]);
		}
		result = result.toString().replaceAll(prefix+".*"+suffix,"");
	
		return result;
	}
}