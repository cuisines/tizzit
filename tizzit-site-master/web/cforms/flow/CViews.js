/* CViews */
function CViews() {
	this.views = new CHashMap();
	this.actualView = null;
	
	this.add = function(key, value) { this.views.add(key, value); }
	this.get = function(key) { return this.views.get(key); }
	
	this.setActualView = function(key) {
		var result = false
		if (this.views.entryExists(key)) {
			this.actualView = this.views.get(key);
			result = true;
		}

		return result;
	}
	this.getActualView = function() { return this.actualView; }
	
	this.toString = function() {
		var result = "";
	
		result = this.views.toString();
		result += (this.actualView == null) ? "null" : this.actualView.toString();
		result += "\n";
		
		return result;
	}
}