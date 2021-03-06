package chickenware.ui.clickgui.button;

import java.text.DecimalFormat;

import chickenware.settings.NumberSetting;
import chickenware.utils.MCHook;
import net.minecraft.client.gui.Gui;

public class NumberButton extends Button implements MCHook
{
    protected float w;
    protected float h;
    public float x;
    public float y;
    public NumberSetting m;
    
    public boolean hovered;
    public boolean clicked;

    public NumberButton(NumberSetting m, float x, float y, float widthIn, float heightIn)
    {
    	this.m= m;
        this.x = x;
        this.y = y;
        this.w = widthIn;
        this.h = heightIn;
        this.clicked = false;
    }
    public void draw(int mouseX, int mouseY) {
    	//this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.w + 4 && mouseY < this.y + this.h + 4;
    	this.hovered = mouseX >= this.x + 4 && mouseY >= this.y - 2 && mouseX < this.x + this.w - 4 && mouseY < this.y + this.h + 2;
    	//Gui.drawRect(x, y, x + w + 4, y + h + 4, hovered ? 0x50000000 : 0x00000000);
    	Gui.drawRect(x + 4, y - 2, x + w - 4, y + h + 2, hovered ? 0x64000000 : 0x00000000);
    	
    	/*Gui.drawRect(x - 0.1f, y - 1.1f, x + w + 4.1f, y - 0.1f, color);
    	Gui.drawRect(x - 0.1f, y + h + 4.1f, x + w + 4.1f, y + h + 5.1f, color);
    	Gui.drawRect(x - 1.1f, y - 1.1f, x - 0.1f, y + h + 5.1f, color);
    	Gui.drawRect(x + w + 4.1f, y - 1.1f, x + w + 5.1f, y + h + 5.1f, color);*/
    	DecimalFormat frmt = new DecimalFormat("#.##");
    	mc.fontRendererObj.drawStringWithShadow(m.name + ": " + frmt.format(m.get()), (x + x + w + 4) / 2 - mc.fontRendererObj.getStringWidth(m.name + ": " + frmt.format(m.get())) / 2, (y + h / 2) - _fr.FONT_HEIGHT / 2, color);
    }
}