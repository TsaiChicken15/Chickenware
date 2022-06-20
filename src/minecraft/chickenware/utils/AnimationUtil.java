package chickenware.utils;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.MovingObjectPosition;

public class AnimationUtil implements MCHook
{
	public static void animNone(float partialTicks) 
	{
		ItemRenderer ir = mc.getItemRenderer();
		float f = 1.0F - (ir.prevEquippedProgress + (ir.equippedProgress - ir.prevEquippedProgress) * partialTicks);
		ir.func_178096_b(f, 0.0F);
        ir.func_178103_d();
	}
	
	public static void anim18(float partialTicks) 
	{
		ItemRenderer ir = mc.getItemRenderer();
		/*if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && _gs.keyBindAttack.getIsKeyPressed())
		{
			if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= mc.thePlayer.getArmSwingAnimationEnd() / 2 || mc.thePlayer.swingProgressInt < 0)
	        {
				mc.thePlayer.swingProgressInt = -1;
				mc.thePlayer.isSwingInProgress = true;
	        }
		}*/
		float f = 1.0F - (ir.prevEquippedProgress + (ir.equippedProgress - ir.prevEquippedProgress) * partialTicks);
		float swingProgress = mc.thePlayer.getSwingProgress(partialTicks);
		float swingProgressReversed = 1.0f - swingProgress;
		ir.func_178096_b(f, swingProgress);
		ir.func_178103_d();
	}
	
	public static void animTap(float partialTicks) 
	{
		ItemRenderer ir = mc.getItemRenderer();
		/*if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && _gs.keyBindAttack.getIsKeyPressed())
		{
			if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= mc.thePlayer.getArmSwingAnimationEnd() / 2 || mc.thePlayer.swingProgressInt < 0)
	        {
				mc.thePlayer.swingProgressInt = -1;
				mc.thePlayer.isSwingInProgress = true;
	        }
		}*/
		float swingProgress = mc.thePlayer.getSwingProgress(partialTicks);
		//GlStateManager.translate(-0.15f, 0.15f, -0.2f);
		ir.func_178096_b(0, swingProgress - 1);
		ir.func_178103_d();
	}
}