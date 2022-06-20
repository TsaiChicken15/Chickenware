package chickenware.modules.utility;

import org.lwjgl.input.Keyboard;

import chickenware.events.Event;
import chickenware.events.listeners.EventPacket;
import chickenware.modules.Module;
import chickenware.utils.KeybindUtil;
import chickenware.utils.MCHook;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class AutoTool extends Module implements MCHook
{
	public AutoTool() 
	{
		super("AutoTool", Keyboard.KEY_NONE, Category.UTILITY, false, "");
	}
	public void onEvent(Event e) 
	{
		if (e instanceof EventPacket && e.isOUTGOING())
		{
			Packet p = ((EventPacket)e).packet;
			if (true)
			{
				try 
				{
					if (p instanceof C07PacketPlayerDigging && mc.gameSettings.keyBindAttack.getIsKeyPressed() && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) 
					{
						autotool(mc.objectMouseOver.func_178782_a());
					}
				} 
				catch (Exception e2) 
				{
					
				}
			}
		}
	}
    private static void autotool(BlockPos position) 
    {
        Block block = mc.theWorld.getBlockState(position).getBlock();
        int item = AutoTool.getStrongestItem(block);
        if (item < 0)
        {
            return;
        }
        float strength = AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.inventory.mainInventory[item]);
        if (mc.thePlayer.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, mc.thePlayer.getHeldItem()) >= strength) 
        {
            return;
        }
        if (mc.thePlayer.inventory.currentItem == item) 
        {
        	return;
        }
        KeybindUtil.onTick(_gs.keyBindsHotbar.clone()[item].getKeyCode());
    }
    private static int getStrongestItem(Block block) 
    {
        float strength = Float.NEGATIVE_INFINITY;
        int strongest = -1;
        for (int i = 0; i < 9; i++) 
        {
            float itemStrength;
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f)
            {
                strongest = i;
                strength = itemStrength;
            }
        }
        return strongest;
    }
    public static float getStrengthAgainstBlock(Block block, ItemStack item) 
    {
        float strength = item.getStrVsBlock(block);
        if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) 
        {
            return strength;
        }
        int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
        return strength + (float)(enchantLevel * enchantLevel + 1);
    }
}