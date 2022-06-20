package chickenware.modules.render;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventFire;
import chickenware.modules.Module;
import chickenware.settings.NumberSetting;

public class LowFire extends Module{
	public LowFire() {
		super("LowFire", Keyboard.KEY_NONE, Category.RENDER, false, "");
		this.addSettings(height);
	}
	public static NumberSetting height = new NumberSetting("Height", -0.7D, -1.0D, 0.0D, 0.05D);
	public void onEvent(Event e) {
		if(e instanceof EventFire) e.cancelEvent();
	}
}