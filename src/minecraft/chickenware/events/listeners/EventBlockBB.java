package chickenware.events.listeners;

import chickenware.events.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlockBB extends Event<EventBlockBB> 
{
	public double x, y, z;
	public AxisAlignedBB blockBB;
	public Block block;
	public EventBlockBB(BlockPos blockPos, Block block, AxisAlignedBB blockBB) 
	{
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
		this.blockBB = blockBB;
		this.block = block;
	}
	public AxisAlignedBB getBoundingBox() {
		return blockBB;
	}
}
