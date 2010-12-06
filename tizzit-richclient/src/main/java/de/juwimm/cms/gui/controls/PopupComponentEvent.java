package de.juwimm.cms.gui.controls;

public class PopupComponentEvent
{
	private PopupComponent source;

	public PopupComponentEvent(PopupComponent source)
	{
		this.source = source;
	}

	public PopupComponent getSource()
	{
		return source;
	}
}
