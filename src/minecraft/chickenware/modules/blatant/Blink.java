package chickenware.modules.blatant;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.modules.Module;
import chickenware.modules.Module.Category;
import chickenware.settings.BooleanSetting;
import chickenware.settings.ModeSetting;
import chickenware.utils.MCHook;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;

public class Blink extends Module implements MCHook
{
	public Blink() 
	{
		super("Blink", Keyboard.KEY_NONE, Category.BLATANT, true, "");
		this.addSettings(modeValue,fakePlayerValue);
	}
	private ModeSetting modeValue = new ModeSetting("Mode","INCOMING","INCOMING","OUTGOING","BOTH-DIR");
	public static BooleanSetting fakePlayerValue = new BooleanSetting("FakePlayer", true);
	private ArrayList<Packet> packetsIn = new ArrayList<Packet>();
	private ArrayList<Packet> packetsOut = new ArrayList<Packet>();
	private INetHandler packetListener;
	private EntityOtherPlayerMP fakePlayer;
	public void onEnable()
	{
		if(mc.currentScreen != null) 
		{
			this.toggle();
		}
		if(fakePlayerValue.get())
		{
			fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.gameProfile);
			fakePlayer.clonePlayer(mc.thePlayer, true);
			fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
			fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
			mc.theWorld.addEntityToWorld(-1337, fakePlayer);
		}
		
		packetsIn.clear();
		packetsOut.clear();
	}
	public void onDisable()
	{
		for(Packet p: packetsIn)
		{
			if(!packetsIn.isEmpty()) {
				if (mc.getNetHandler().getNetworkManager().channel.isOpen())
		        {
		            try
		            {
		                mc.getNetHandler().getNetworkManager().channelRead1(p);
		            }
		            catch (ThreadQuickExitException var4)
		            {
		                ;
		            }
		        }
			}
		}
		packetsIn.clear();
		if(!packetsOut.isEmpty()) {
			for(Packet p: packetsOut)
			{
				mc.getNetHandler().addToSendQueueNoEvent(p);
			}
		}
		packetsOut.clear();
		if (fakePlayer != null) 
		{
            mc.theWorld.removeEntityFromWorld(fakePlayer.entityId);
            fakePlayer = null;
        }		
	}
	public void onEvent(Event e) 
	{
		this.additionalInfo = String.valueOf(packetsIn.size()) + ", " + String.valueOf(packetsOut.size());
		if(e instanceof EventPacket && Client.nullCheck() && ((EventPacket)e).packet instanceof Packet && mc.thePlayer != null) 
		{
			if(e.isINCOMING() && 
					(modeValue.is("INCOMING") || modeValue.is("BOTH-DIR")))
			{
				e.cancelEvent();
				packetsIn.add(((EventPacket)e).packet);
			}
			if(e.isOUTGOING() && 
					(modeValue.is("OUTGOING") || modeValue.is("BOTH-DIR")))
			{
				e.cancelEvent();
				packetsOut.add(((EventPacket)e).packet);
			}
		}
	}
}