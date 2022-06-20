package chickenware.modules.render;

import chickenware.events.Event;
import chickenware.events.listeners.EventViewModel;
import chickenware.modules.Module;
import chickenware.settings.NumberSetting;

public class ViewModel extends Module 
{
	public ViewModel() 
	{
	    super("ViewModel", 0, Module.Category.RENDER, false,"");
	    addSettings(x,y,z,swingSpeed);
	  }
	public static NumberSetting x = new NumberSetting("ItemX", 1.12D, 0.0D, 2.0D, 0.01D);
	public static NumberSetting y = new NumberSetting("ItemY", -1.04D, -2.0D, 0.0D, 0.01D);
	public static NumberSetting z = new NumberSetting("ItemZ", -1.42D, -2.0D, -1.0D, 0.01D);
	public static NumberSetting swingSpeed = new NumberSetting("SwingSpeed", 18, 0, 50, 1);
	public void onEvent(Event e) 
	{
		if(e instanceof EventViewModel) 
		{
			e.cancelEvent();
		}
	}
}