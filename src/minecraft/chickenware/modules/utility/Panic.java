package chickenware.modules.utility;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.modules.Module;
import chickenware.notification.Notification;
import chickenware.notification.NotificationManager;
import chickenware.notification.NotificationType;

public class Panic extends Module
{
	public Panic()
	{
		super("Panic", Keyboard.KEY_NONE, Category.UTILITY, false, "Disables all currently enabled modules");
	}
	public void onEnable() 
	{
		int s = 0;
		for(Module m: Client.getModule())
		{
			if(m.getName() == "Animations" || m.getName() == "ClickGui" || m.getName() == "TextGUI" || m.getName() == "AutoDisable") 
			{
				continue;
			}
			if(m.isToggled())
			{
				s++;
      			m.toggled = false;
      			m.onDisable();
			}
		}
		if(s != 0)
		{
			NotificationManager.show(new Notification(NotificationType.INFO, "¡±l" + s + " module(s) ¡±ldisabled", 2));
		}
	}
}