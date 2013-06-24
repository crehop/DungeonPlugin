package me.ThaH3lper.com.dungeon;

import java.util.ArrayList;
import java.util.List;

import me.ThaH3lper.com.Dungeon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Cuboid {
	
	private Location minLoc;
	private Location maxLoc;
	
	private Dungeon dun;
	
	public Cuboid(Location l1, Location l2, Dungeon dun)
	{
		this.dun = dun;
		
		int x1, y1, z1;
		int x2, y2, z2;
		
		x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		
		x2 = Math.max(l1.getBlockX(), l2.getBlockX());
		y2 = Math.max(l1.getBlockY(), l2.getBlockY());
		z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		
		minLoc = new Location(l1.getWorld(), x1, y1, z1);
		maxLoc = new Location(l2.getWorld(), x2, y2, z2);
	}
	
	public List<Block> getBlocks()
	{
		List<Block> blocks = new ArrayList<Block>();
		for (int x = minLoc.getBlockX(); x < maxLoc.getBlockX() + 1; x++) 
		{
			for (int y = minLoc.getBlockY(); y < maxLoc.getBlockY() + 1; y++) 
			{
				for (int z = minLoc.getBlockZ(); z < maxLoc.getBlockZ() + 1; z++) 
				{					
					blocks.add(minLoc.getWorld().getBlockAt(new Location(minLoc.getWorld(), x, y, z)));
				}				
			}
		}
		return blocks;
	}
	public List<Block> getDoor()
	{
		List<Block> blocks = new ArrayList<Block>();
		for (int x = minLoc.getBlockX(); x < maxLoc.getBlockX() + 1; x++) 
		{
			for (int y = minLoc.getBlockY(); y < maxLoc.getBlockY() + 1; y++) 
			{
				for (int z = minLoc.getBlockZ(); z < maxLoc.getBlockZ() + 1; z++) 
				{	
					if(minLoc.getWorld().getBlockAt(new Location(minLoc.getWorld(), x, y, z)).getType() == Material.IRON_BLOCK)						
						blocks.add(minLoc.getWorld().getBlockAt(new Location(minLoc.getWorld(), x, y, z)));
				}				
			}
		}
		return blocks;
	}
	public List<Block> getSpawnBlock()
	{
		List<Block> blocks = new ArrayList<Block>();
		for (int x = minLoc.getBlockX(); x < maxLoc.getBlockX() + 1; x++) 
		{
			for (int y = minLoc.getBlockY(); y < maxLoc.getBlockY() + 1; y++) 
			{
				for (int z = minLoc.getBlockZ(); z < maxLoc.getBlockZ() + 1; z++) 
				{	
					if(minLoc.getWorld().getBlockAt(new Location(minLoc.getWorld(), x, y, z)).getType() == Material.GOLD_BLOCK)						
						blocks.add(minLoc.getWorld().getBlockAt(new Location(minLoc.getWorld(), x, y, z)));
				}				
			}
		}
		return blocks;
	}
	
	public Location getMinLoc()
	{
		return minLoc;
	}
	
	public Location getMaxLoc()
	{
		return maxLoc;
	}

}
