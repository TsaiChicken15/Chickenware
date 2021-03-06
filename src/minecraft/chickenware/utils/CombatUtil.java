package chickenware.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import chickenware.Client;
import chickenware.events.listeners.EventUpdate;
import chickenware.modules.blatant.Killaura;
import chickenware.modules.other.AntiBot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class CombatUtil implements MCHook 
{
	public static void rotate(EventUpdate e, float yaw, float pitch) 
	{
		e.yaw = yaw;
		e.pitch = pitch;
		mc.thePlayer.prevRotationYawHead = yaw;
		mc.thePlayer.prevRenderYawOffset = yaw;
		mc.thePlayer.rotationYawHead = yaw;
		mc.thePlayer.renderYawOffset = yaw;
		mc.thePlayer.rotationPitchHead = pitch;
	}
	public static float updateRotation(float current, float intended, float factor) 
	{
		float var4 = MathHelper.wrapAngleTo180_float(intended - current);

		if (var4 > factor) 
		{
			var4 = factor;
		}

		if (var4 < -factor) 
		{
			var4 = -factor;
		}

		return var4;
	}
	public static float[] getAngleHead(Entity e) 
	{
		float yaw, pitch;
		Vec3 targetPos = new Vec3(e.posX, e.posY + e.getEyeHeight(), e.posZ);
		return getAngle(targetPos);
	}	 
	public static float[] getAngleBody(Entity e) 
	{
		float yaw, pitch;
		Vec3 targetPos = new Vec3(e.posX, e.posY + (e.boundingBox.maxY - e.boundingBox.minY) / 2, e.posZ);
		return getAngle(targetPos);
	}	
	public static float[] getAngleFeet(Entity e) 
	{
		float yaw, pitch;
		Vec3 targetPos = new Vec3(e.posX, e.posY, e.posZ);
		return getAngle(targetPos);
	}	
	public static float[] getAngle(BlockPos b) 
	{
		float yaw, pitch;
		Vec3 targetPos = new Vec3(b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5);
		return getAngle(targetPos);
	}	
	public static float[] getAngle(double x,double y,double z) 
	{
		float yaw, pitch;
		Vec3 targetPos = new Vec3(x, y, z);
	    return getAngle(targetPos);
	}	
	public static float[] getAngle(Vec3 v) 
	{
		float yaw, pitch;
		Vec3 playerPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
	    double d0 = v.xCoord - playerPos.xCoord;
	    double d1 = v.yCoord - playerPos.yCoord;
	    double d2 = v.zCoord - playerPos.zCoord;
	    double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
	    float f = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
	    float f1 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
	    yaw = f;
	    pitch = f1;
	    return new float[] {yaw, pitch};
	}	
	public static EntityLivingBase getEntity(double d) 
	{
    	Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase target = null;
		List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class :: isInstance).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= d && entity.getName() != mc.thePlayer.getName() && entity != mc.thePlayer && entity.isEntityAlive() /*&& mc.thePlayer.canEntityBeSeen(entity)*/).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> 
				(entity instanceof EntityPlayer && Killaura.playerValue.get()) || 
        		(entity instanceof EntityAnimal && Killaura.animalValue.get()) || 
        		(entity instanceof EntityGolem && Killaura.golemValue.get()) || 
        		(entity instanceof EntityMob && Killaura.mobValue.get()) || 
        		(entity instanceof EntityVillager && Killaura.villagerValue.get())
        		).collect(Collectors.toList());
		targets = targets.stream().filter(e -> 
				((!e.isInvisibleToPlayer(mc.thePlayer) || !e.isInvisible()) ||
				((e.isInvisibleToPlayer(mc.thePlayer) || e.isInvisible()) && Killaura.invisibleValue.get()))
				).collect(Collectors.toList());
		if(Client.isEnabled("AntiBot") != null)
		{
			targets = targets.stream().filter(e -> 
					!AntiBot.isBot(e)
					).collect(Collectors.toList());
		}
		if(Killaura.priorityValue.is("Distance"))
		{
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
		}
		else if(Killaura.priorityValue.is("Angle"))
		{
			targets.sort(Comparator.comparingDouble(entity -> Math.abs(MathHelper.wrapAngleTo180_float(getAngleHead(entity)[0] - mc.thePlayer.rotationYaw))));
		}
		else if(Killaura.priorityValue.is("Health"))
		{
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getHealth()));
		}
		else if(Killaura.priorityValue.is("Armor"))
		{
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getTotalArmorValue()));
		}
		//targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		if(!targets.isEmpty()) 
		{
	        target = (EntityLivingBase) targets.get(0);
		}
		return target;
    }
}