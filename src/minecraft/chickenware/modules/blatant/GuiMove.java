package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.BooleanSetting;
import chickenware.utils.MCHook;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

public class GuiMove extends Module implements MCHook
{
	public GuiMove() 
	{
		super("GuiMove", Keyboard.KEY_NONE, Category.BLATANT, false, "Allows you to walk while in gui");
		this.addSettings(noSprintValue,bypassValue);
	}
    public static BooleanSetting bypassValue = new BooleanSetting("NoOpenPacket", false);
    public static BooleanSetting noSprintValue = new BooleanSetting("NoSprint", false);
    boolean lastInvOpen = false, invOpen = false;
	public void onEnable()
	{
		lastInvOpen = false;
        invOpen = false;
	}
	public void onDisable() 
	{
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null) 
		{
           	mc.gameSettings.keyBindForward.pressed = false;
		}
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null) 
		{
		    mc.gameSettings.keyBindBack.pressed = false;
		}
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null) 
		{
		    mc.gameSettings.keyBindRight.pressed = false;
		}
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null) 
		{
		    mc.gameSettings.keyBindLeft.pressed = false;
		}
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null) 
		{
		    mc.gameSettings.keyBindJump.pressed = false;
		}
		if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null) 
		{
		    mc.gameSettings.keyBindSprint.pressed = false;
		}		
		lastInvOpen = false;
		invOpen = false;
	}
	public void onEvent(Event e) 
	{
		if (e instanceof EventUpdate && e.isPRE()) 
		{
			if (mc.currentScreen instanceof GuiChat) 
			{
				return;
			}
			if (mc.currentScreen != null || mc.currentScreen == Client.ClickGui) 
			{	
				KeyBinding[] moveKeys = new KeyBinding[]
				{
    				mc.gameSettings.keyBindForward,
    				mc.gameSettings.keyBindBack,
    				mc.gameSettings.keyBindLeft,
    				mc.gameSettings.keyBindRight,
    				mc.gameSettings.keyBindJump					
    			};
    			for (KeyBinding bind : moveKeys)
    			{
    				KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
    			}
			}	
		}	
		if(e instanceof EventPacket) 
		{
			Packet packet = ((EventPacket)e).packet;

	        lastInvOpen = invOpen;
	        
	        if (packet instanceof S2DPacketOpenWindow || (packet instanceof C16PacketClientStatus && 
	        		((C16PacketClientStatus)packet).status == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)) 
	        {
	            invOpen = true;
	            if (noSprintValue.enabled) 
	            {
	                if (mc.thePlayer.isSprinting())
	                {
	                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
	                }
	                if (mc.thePlayer.isSneaking()) 
	                {
	                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
	                }
	            }
	        }
	        
	        if (packet instanceof S2EPacketCloseWindow || packet instanceof C0DPacketCloseWindow) 
	        {
	            invOpen = false;
	            if (noSprintValue.enabled) 
	            {
	                if (mc.thePlayer.isSprinting()) 
	                {
	                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
	                }
	                if (mc.thePlayer.isSneaking()) 
	                {
	                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
	                }
	            }
	        }
	        
	        if(bypassValue.enabled) 
	        {
	        	if (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).status == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) 
	        	{
                    e.cancelEvent();
                }
	        }
		}
	}
}
