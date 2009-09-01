/* CHashMapToMailString */
function CHashMapToMailString(map,excludes) {
	this.map = map.getMap();
	this.excludes = excludes;
	
	this.getString = function() {
		var result = "";
		for(var i in this.map)
            if(this.excludes.indexOf(i.toString())==-1){		
			    result += i.toString() + "=" + this.map[i].toString() + "\n";
			}
		return result;
		//return this.map.toString();
	}
}