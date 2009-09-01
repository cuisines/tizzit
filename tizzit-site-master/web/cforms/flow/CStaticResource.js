function CStaticResource() {
  this.resource = new CHashMap();
  this.getResource = function(key) {
  	return this.resource.get(key);
  }
}