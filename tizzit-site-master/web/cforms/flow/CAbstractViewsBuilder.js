/*CResultViewsBuilder*/
/*work with lazybinding*/
function CAbstractViewsBuilder() {
	this.views = null;
	this.getViews = function() {
		if (this.views == null)
			this.views = new CViews();
	
		return this.views;
	}
}