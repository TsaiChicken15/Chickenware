package chickenware.modules.blatant;

import org.lwjgl.input.Keyboard;

import chickenware.Client;
import chickenware.events.Event;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.Module;
import chickenware.settings.BooleanSetting;
import chickenware.settings.Description;
import chickenware.settings.ModeSetting;
import chickenware.settings.NewLine;
import chickenware.settings.NumberSetting;
import chickenware.utils.CombatUtil;
import chickenware.utils.RandomUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MathHelper;

public class Killaura extends Module
{
	public Killaura() 
	{
		super("Killaura", Keyboard.KEY_R, Category.BLATANT, true, "Attacks players around you");
		this.addSettings(noInvValue,smartAttackValue,autoBlockValue,minCPSValue,maxCPSValue,fovValue,attackRangeValue,aimRangeValue,discoverRangeValue,autoBlockRangeValue,minTurnValue,maxTurnValue,a,
				attackTimingValue,aimModeValue,priorityValue,jitterModeValue,targetDes,playerValue,animalValue,golemValue,mobValue,villagerValue,invisibleValue,cantBeSeenValue,cantBeSeenRangeValue);
	}
	public static NumberSetting minCPSValue = new NumberSetting("MinCPS", 20, 1 , 50, 1);
	public static NumberSetting maxCPSValue = new NumberSetting("MaxCPS", 20, 1 , 50, 1);
	public static NumberSetting fovValue = new NumberSetting("FOV", 180 , 1 , 180, 1);
	public static NumberSetting attackRangeValue = new NumberSetting("AttackRange", 3.5 , 1 , 6 , 0.1);
	public static NumberSetting aimRangeValue = new NumberSetting("AimRange", 5 , 1 , 10 , 0.1);
	public static NumberSetting discoverRangeValue = new NumberSetting("DiscoverRange", 7 , 1 , 10 , 0.1);
	public static ModeSetting jitterModeValue = new ModeSetting("JitterDir", "Both-Dir", "None", "H-Dir", "V-Dir", "Both-Dir");
	public static ModeSetting priorityValue = new ModeSetting("Priority","Angle","Distance","Angle","Health","Armor");
	public static ModeSetting aimModeValue = new ModeSetting("AimMode","Body","Head","Body","Feet");
	public static NumberSetting minTurnValue = new NumberSetting("MinTurn", 180, 1, 180, 1);
	public static NumberSetting maxTurnValue = new NumberSetting("MaxTurn", 180, 1, 180, 1);
	public static BooleanSetting noInvValue = new BooleanSetting("NoInvAttack", true);
	public static BooleanSetting smartAttackValue = new BooleanSetting("SmartAttack", true);
	public static BooleanSetting autoBlockValue = new BooleanSetting("FakeAutoBlock",false);
	public static NumberSetting autoBlockRangeValue = new NumberSetting("AutoBlockRange", 5, 1, 10, 0.1);
	public static ModeSetting attackTimingValue = new ModeSetting("AttackTiming", "Pre", "Pre", "Post", "Both");
	public static Description targetDes = new Description("Target");
	public static BooleanSetting playerValue = new BooleanSetting("Player", true);
	public static BooleanSetting animalValue = new BooleanSetting("Animal", false);
	public static BooleanSetting golemValue = new BooleanSetting("Golem", false);
	public static BooleanSetting mobValue = new BooleanSetting("Mob", false);
	public static BooleanSetting villagerValue = new BooleanSetting("Villager", false);
	public static BooleanSetting invisibleValue = new BooleanSetting("Invisible", false);
	public static BooleanSetting cantBeSeenValue = new BooleanSetting("WallHit", false);
	public static NumberSetting cantBeSeenRangeValue = new NumberSetting("WallRange", 2, 1, 6, 0.1);
	public static NewLine a = new NewLine();
	public static EntityLivingBase target = null;
	private long lastSwing;
	private long delay = 1000 / RandomUtil.randomInt((int)minCPSValue.get(), (int)maxCPSValue.get());
	private boolean needSpoof = false;
	private int oldSlot = -1;
    public void onDisable() 
    {
    	target = null;
    }
    public void onEvent2(Event e) 
    {
    	this.additionalInfo = String.valueOf(minCPSValue.get()) + ", " + String.valueOf(maxCPSValue.get());
		if (minTurnValue.get() > maxTurnValue.get()) 
		{
	        double temp = minTurnValue.get();
	        minTurnValue.setValue(maxTurnValue.get());
	        maxTurnValue.setValue(temp);
	    } 
		if (minCPSValue.get() > maxCPSValue.get()) 
		{
	        double temp = minCPSValue.get();
	        minCPSValue.setValue(maxCPSValue.get());
	        maxCPSValue.setValue(temp);
	    } 
		if (aimRangeValue.get() > discoverRangeValue.get()) 
		{
	        double temp = aimRangeValue.get();
	        aimRangeValue.setValue(discoverRangeValue.get());
	        discoverRangeValue.setValue(temp);
	    } 
		if (attackRangeValue.get() > aimRangeValue.get()) 
		{
	        double temp = attackRangeValue.get();
	        attackRangeValue.setValue(aimRangeValue.get());
	        aimRangeValue.setValue(temp);
	    } 
    }
	public void onEvent(Event e) 
	{
		if (e instanceof EventUpdate) 
		{
			EventUpdate event = ((EventUpdate)e);
			if((attackTimingValue.is("Pre") && !e.isPRE()) || (attackTimingValue.is("Post") && !e.isPOST())) return;
			if(mc.currentScreen != null && mc.currentScreen != Client.ClickGui && noInvValue.get()) return;
			target = null;
	        target = CombatUtil.getEntity(discoverRangeValue.get());
	        if (target != null) 
	        {
	        	if(target.getDistanceToEntity(mc.thePlayer) <= aimRangeValue.get())
	        	{
			        float yawDiff = Math.abs(MathHelper.wrapAngleTo180_float(delay));
			        if(yawDiff > fovValue.get() && !(target.getDistanceToEntity(mc.thePlayer) <= 0.689))
			        {
			        	target = null;
			        	return;
			        }
	        		if(System.currentTimeMillis() - lastSwing >= delay && !mc.thePlayer.isBlocking() && target.getDistanceToEntity(mc.thePlayer) <= attackRangeValue.get())
	        		{
	        			if(smartAttackValue.get() && (BedNuker.nuking || Scaffold.scaffolding)) return;
	        			
	        			mc.thePlayer.swingItem();
	        			mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
	        			
	    				lastSwing = System.currentTimeMillis();
	    				delay = 1000 / RandomUtil.randomInt((int)minCPSValue.get(), (int)maxCPSValue.get());
	    				
	    				//target = null;
	        		}
	        		
	        		if (target.getDistanceToEntity(mc.thePlayer) <= aimRangeValue.get()) 
	    			{
	    				float jitterYaw = jitterModeValue.is("Both-Dir") ? shake(1) : jitterModeValue.is("H-Dir") ? shake(1) : 0;
	    				float jitterPitch = jitterModeValue.is("Both-Dir") ? shake(1) : jitterModeValue.is("V-Dir") ? shake(1) : 0;

	    				if(aimModeValue.is("Head"))
	    				{
	    					if (target == null) return;
	    					CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw,
	    										(float) (CombatUtil.getAngleHead(target)[0] + jitterYaw),
	    										RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), 
	    										event.pitch + CombatUtil.updateRotation(event.pitch,
	    	    								(float) (CombatUtil.getAngleHead(target)[1] + jitterPitch),
	    	    								RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
	    					
	    					
	    				}
	    				else if(aimModeValue.is("Body"))
	    				{
	    					if (target == null) return;
	    					CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw,
									(float) (CombatUtil.getAngleBody(target)[0] + jitterYaw),
									RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), 
									event.pitch + CombatUtil.updateRotation(event.pitch,
    								(float) (CombatUtil.getAngleBody(target)[1] + jitterPitch),
    								RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
	    				}
	    				else if(aimModeValue.is("Feet"))
	    				{
	    					if (target == null) return;
	    					CombatUtil.rotate(event, event.yaw + CombatUtil.updateRotation(event.yaw,
									(float) (CombatUtil.getAngleFeet(target)[0] + jitterYaw),
									RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())), 
									event.pitch + CombatUtil.updateRotation(event.pitch,
    								(float) (CombatUtil.getAngleFeet(target)[1] + jitterPitch),
    								RandomUtil.randomInt((int)minTurnValue.get(), (int)maxTurnValue.get())));
	    				}
	    			}
	        	}
	        }
		}
    }
	public float shake(int n) 
	{
        return (float) (Math.random() * n * 2 - 2);
    }
}
