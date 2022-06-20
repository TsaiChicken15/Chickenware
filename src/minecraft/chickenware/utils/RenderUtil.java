package chickenware.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtil implements MCHook
{
	public static void drawPlayerBox(double posX, double posY, double posZ, int color) 
	{
		double x =
			posX - 0.5
				- mc.getRenderManager().renderPosX;
		double y =
			posY
				- mc.getRenderManager().renderPosY;
		double z =
			posZ - 0.5
				- mc.getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(1, 1, 1, 0.5F);
		RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x + 0.2, y, z + 0.2,
			x + 0.8, y + 1.8, z + 0.8), color);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);	
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
    }
	
	public static int getRainbow(float second, float saturation, float brightness) 
	{
		float hue = (System.currentTimeMillis() % (int)(second * 1000)) / (float)(second * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
}