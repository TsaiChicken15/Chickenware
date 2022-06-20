package chickenware.modules.other;

import org.lwjgl.input.Keyboard;

import chickenware.modules.Module;
import chickenware.notification.Notification;
import chickenware.notification.NotificationManager;
import chickenware.notification.NotificationType;
import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class Damage extends Module implements MCHook
{
	public Damage() 
	{
		super("Damage", Keyboard.KEY_NONE, Category.OTHER, true, "");
		this.addSettings(damageValue);
	}
	public static NumberSetting damageValue = new NumberSetting("Damage", 1, 1, 20, 1);
	public void onEnable() 
	{
		if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 4, 0).expand(0, 0, 0)).isEmpty()) 
		{
			mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.35 + damageValue.get(), mc.thePlayer.posZ, false));
		    mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		    mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		}
		else 
		{
			NotificationManager.show(new Notification(NotificationType.WARNING, "¡±lDamage failed", 2));
		}
		this.toggle();
	}
}