package chickenware.modules.render;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventHurtCam;
import chickenware.modules.Module;

public class NoHurtCam extends Module
{
	public NoHurtCam() 
	{
		super("NoHurtCam", Keyboard.KEY_NONE, Category.RENDER, false, "");
		}
	public void onEvent(Event e) 
	{
		if(e instanceof EventHurtCam) e.cancelEvent();
	}
}