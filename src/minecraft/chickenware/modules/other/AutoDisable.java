package chickenware.modules.other;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.notification.Notification;
import chickenware.notification.NotificationManager;
import chickenware.notification.NotificationType;
import chickenware.utils.MCHook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class AutoDisable extends Module implements MCHook
{
	public AutoDisable() 
	{
		super("AutoDisable", Keyboard.KEY_NONE, Category.OTHER, false, "");
		this.toggled = true;
	}
	public void onEvent(Event e) 
	{
		if(e instanceof EventPacket) 
		{
			Packet packet = ((EventPacket)e).packet;
			if(packet instanceof S2FPacketSetSlot)
			{
				ItemStack item = ((S2FPacketSetSlot) packet).func_149174_e();
				int windowId = ((S2FPacketSetSlot) packet).func_149175_c();
				int slot = ((S2FPacketSetSlot) packet).func_149173_d();
				if(item != null)
				{
		            String itemName = item.getUnlocalizedName();
		            String displayName = item.getDisplayName();
		            
		            if (windowId == 0 && slot == 43 && itemName.toLowerCase().contains("paper")) 
		            {
		            	int s = 0;
		              	for (Module m : Client.getModule()) 
		              	{
		              		if (m.autoDisable && m.isToggled()) 
		              		{
		              			s++;
		              			m.toggled = false;
		              			m.onDisable();
		              		} 
		              	} 
			            if (s != 0)
			            {
			                NotificationManager.show(new Notification(NotificationType.INFO, "¡±l" + s + " module(s) ¡±ldisabled", 2));
			            }
	                }
				}
			}
		}
	}
	public void onEvent2(Event e)
	{
		if(e instanceof EventUpdate)
		{
			int s = 0;
			for (Module m : Client.getModule()) 
          	{
          		if (m.autoDisable && m.isToggled() && mc.thePlayer.isDead) 
    			{
          			s++;
          			m.toggled = false;
          			m.onDisable();	
          		}
          	}
			if (s != 0)
            {
                NotificationManager.show(new Notification(NotificationType.INFO, "¡±l" + s + " module(s) ¡±ldisabled", 2));
            }
		}
	}
}