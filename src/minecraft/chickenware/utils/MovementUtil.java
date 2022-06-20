package chickenware.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.stats.StatList;

public class MovementUtil implements MCHook
{
	public static boolean isMoving() 
	{
		if (Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) > 0.0D) 
		{
			return true;
		}
		return false;
	}
	public static boolean isMovementInputing() 
	{
		return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
	}
	public static boolean isOnGround(double height) {
	    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty())
	    	return true; 
	    return false;
	}
	public static double direction() 
	{
	    float rotationYaw = mc.thePlayer.rotationYaw;
	    if (mc.thePlayer.moveForward < 0.0F) 
	    {
	    	rotationYaw += 180.0F; 
	    }
	    float forward = 1.0F;
	    if (mc.thePlayer.moveForward < 0.0F)
	    {
	    	forward = -0.5F;
	    }
	    else if (mc.thePlayer.moveForward > 0.0F) 
	    {
	    	forward = 0.5F;
	    } 
	    if (mc.thePlayer.moveStrafing > 0.0F) 
	    {
	    	rotationYaw -= 90.0F * forward; 
	    }
	    if (mc.thePlayer.moveStrafing < 0.0F) 
	    {
	    	rotationYaw += 90.0F * forward; 
	    }
	    return Math.toRadians(rotationYaw);
	} 
	public static double getSpeed() 
	{
		return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
	}  
	public static void strafe() 
	{
	    strafe(getSpeed());
	}  
	public static void strafe(double d) 
	{
	    mc.thePlayer.motionX = -Math.sin(direction()) * d;
	    mc.thePlayer.motionZ = Math.cos(direction()) * d;
	}
	public static void fakeJump() 
	{
	    mc.thePlayer.isAirBorne = true;
	    mc.thePlayer.triggerAchievement(StatList.jumpStat);
	}
	public static void forward(double d)
	{
		final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
		mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(yaw) * d, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * d);
	}
	public static void move(float speed) {
        if (!isMoving()) return;
        double yaw = direction();
        mc.thePlayer.motionX += -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ += Math.cos(yaw) * speed;
    }
}