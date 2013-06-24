package me.ThaH3lper.com.dungeon;

import java.util.List;

import javax.swing.text.DefaultEditorKit.PasteAction;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.Effects.ParticleEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Floor {

	public Cuboid cuboID;
	private Tower tower;
	private TempletFloor tf;
	Dungeon dun;
	List<Block> list2, list;
	
	private int Level;
	
	public Floor(Cuboid cuboid, Tower tower, int level, TempletFloor tf, Dungeon dun)
	{
		this.dun = dun;
		this.cuboID = cuboid;
		this.tower = tower;
		this.Level = level;
		this.tf = tf;
		list = cuboID.getBlocks();
		if(tf != null)
			list2 = this.tf.cubeId.getBlocks();
	}
	
	public TempletFloor getTempletFloor()
	{
		return tf;
	}
	
	public boolean gen(int min, int max, int pastemode)
	{
		//list2 = templet
		//list1 = floor
		if(max > list2.size())
			max = list2.size();
		int i = min - 1;
		for (Block b : list.subList(min, max)) 
		{
			i++;
			Block c = list2.get(i);
			if((pastemode == 1 && isBlock(c)) || (pastemode == 2 && !isBlock(c)))
			{
				cuboID.getMaxLoc().getWorld().getBlockAt(b.getLocation()).setTypeIdAndData(c.getTypeId(), c.getData(), true);
			}
		}
		if(max == list2.size())
			return true;
		return false;
		
	}
	public boolean clear(int min, int max, int pastemode)
	{
		List<Block> list = cuboID.getBlocks();
		if(max > cuboID.getBlocks().size())
			max = cuboID.getBlocks().size();
		for (Block b : list.subList(min, max)) 
		{
			if((pastemode == 1 && !isBlock(b)) || (pastemode == 2 && isBlock(b)) )
			{
				cuboID.getMaxLoc().getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
			}
		}
		if(max == cuboID.getBlocks().size())
			return true;
		return false;
		
	}
	
	public void open()
	{
		for (Block b : cuboID.getDoor()) 
		{
			cuboID.getMaxLoc().getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
			
			try {
				ParticleEffect.sendToLocation(ParticleEffect.FIREWORKS_SPARK, b.getLocation(), 0.3f, 0.3f, 0.3f, 0.1f, 50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isBlock(Block b)
	{
		if(b.getTypeId() >= 1 && 7 >= b.getTypeId())
			return true;
		if(b.getTypeId() >= 14 && 25 >= b.getTypeId())
			return true;
		if(b.getTypeId() == 121 || 35 == b.getTypeId())
			return true;
		if(b.getTypeId() >= 41 && 49 >= b.getTypeId())
			return true;
		if(b.getTypeId() >= 79 && 95 >= b.getTypeId())
			return true;
		if(b.getTypeId() >= 97 && 100 >= b.getTypeId())
			return true;
		return false;
	}
}
