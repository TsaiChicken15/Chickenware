package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;

public class Timer extends Module implements MCHook
{
	public Timer() 
	{
		super("Timer", Keyboard.KEY_NONE, Category.BLATANT, true, "Increases game timer speed");
		this.addSettings(timerValue);
	}
	public static NumberSetting timerValue = new NumberSetting("Speed", 2f, 0f, 10f, 0.01);
	public void onEnable() 
	{
		mc.timer.timerSpeed = (float) timerValue.get();
	}
	public void onDisable() 
	{
		mc.timer.timerSpeed = 1.0f;
	}
	public void onEvent2(Event e) 
	{
		if(e instanceof EventUpdate) 
		{
			this.additionalInfo = String.valueOf(timerValue.get());
		}
	}
}