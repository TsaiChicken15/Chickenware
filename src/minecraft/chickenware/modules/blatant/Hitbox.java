package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventGetHitbox;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;

public class Hitbox extends Module implements MCHook{
	public Hitbox() {
		super("Hitbox", Keyboard.KEY_NONE, Category.BLATANT, false, "Expands players hitbox");
		this.addSettings(sizeValue);
	}
	public static NumberSetting sizeValue = new NumberSetting("Size", 0.1, 0, 1, 0.01);
	public void onEvent(Event e) {
	    if(e instanceof EventGetHitbox) 
	    {
	    	if(Client.nullCheck()) 
	    	{
	    		e.cancelEvent();
	    	}
	    }
	}
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(sizeValue.get());
		}
	}
}