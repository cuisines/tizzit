/* CHashMap */
function CHashMap() {
	this.map = new Array();
	
	this.add = function(key, value) {
		this.map[key] = value;
	}
	
	this.get = function(key) {
		return (this.map[key] != undefined) ? this.map[key] : null;
	}

	this.remove = function(key) {
		this.map[key] = undefined;
	}
	
	this.entryExists = function(key) {
		return (this.map[key] != undefined) ? true : false;
	}
	
	this.clear = function() {
		this.map = new Array();
	}
	
	this.getMap = function() {
		return this.map;
	}
	
	this.toString = function() {
		var result = "";
		for(var i in this.map)
			result += i.toString() + "=" + this.map[i].toString() + " (" + typeof(this.map[i]) + ")\n";
		return result;
	}
}