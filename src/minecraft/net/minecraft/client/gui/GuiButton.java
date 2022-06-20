package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
    //protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    protected int hoverTime;
    private static final String __OBFID = "CL_00000668";

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.hoverTime = 0;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        byte var2 = 1;

        if (!this.enabled)
        {
            var2 = 0;
        }
        else if (mouseOver)
        {
            var2 = 2;
        }

        return var2;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	FontRenderer var4 = mc.fontRendererObj;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = this.getHoverState(this.hovered);
            if(var5 == 2)
            {
            	if(hoverTime < 15)
            	{
            		this.hoverTime++;
            	}
            }
            else
            {
            	if(hoverTime >= 1)
            	{
            		this.hoverTime--;
            	}
            }
            this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, hovered ? 0x90000000 : 0x60000000);
            this.drawRect(this.xPosition + 1, this.yPosition, this.xPosition, this.yPosition + this.height, 0xffffffff);
            this.drawRect(this.xPosition + this.width, this.yPosition, this.xPosition + this.width - 1, this.yPosition + this.height, 0xffffffff);
            this.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition, 0xffffffff);
            this.drawRect(this.xPosition + 1, this.yPosition  + this.height, this.xPosition + this.width - 1, this.yPosition + this.height - 1, 0xffffffff);
            //this.drawRect(this.xPosition + this.width / 2, this.yPosition, this.xPosition + this.width / 2, this.yPosition + this.height, 0x90000000);
            this.mouseDragged(mc, mouseX, mouseY);
            int var6 = 14737632;

            if (!this.enabled)
            {
                var6 = 10526880;
            }
            else if (this.hovered)
            {
                var6 = 16777120;
            }
            
            var4.drawStringWithShadow(this.displayString, (float)(this.xPosition + this.width / 2 - var4.getStringWidth(this.displayString) / 2), (float)this.yPosition + (this.height - 8) / 2, var6);
            this.drawRect((float) ((this.xPosition + this.width / 2) - (this.width / 1.998) / 15 * this.hoverTime), this.yPosition + (float)(this.height - 1.5), (float) ((this.xPosition + this.width / 2) + (this.width / 1.998) / 15 * this.hoverTime), this.yPosition + this.height, -16748374);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {}

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void func_175211_a(int p_175211_1_)
    {
        this.width = p_175211_1_;
    }
}
