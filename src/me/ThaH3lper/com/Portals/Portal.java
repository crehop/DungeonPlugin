package me.ThaH3lper.com.Portals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import me.ThaH3lper.com.dungeon.Cuboid;
import me.ThaH3lper.com.dungeon.Tower;

public class Portal {
	
	public Cuboid cuboid;
	public int tower;
	public Location locSign;
	public int state = 1;
	public boolean on = false;
	
	public Portal(Cuboid cuboid, int tower, Location locSign)
	{
		this.cuboid = cuboid;
		this.tower = tower;
		this.locSign = locSign;
		setSign(1);
		Open();
	}
	
	public void setSign(int i)
	{
		Sign sign = (Sign) locSign.getBlock().getState();
		sign.setLine(0, "Right-click to");
		sign.setLine(1, "Set difficulty");
		sign.setLine(2, "---------------");
		if(i == 1)
			sign.setLine(3, ChatColor.GREEN + "EASY");
		else if(i == 2)
			sign.setLine(3, ChatColor.GOLD+ "NORMAL");
		else if(i == 3)
			sign.setLine(3, ChatColor.RED + "HARD");
		else if(i == 5)
			sign.setLine(3, ChatColor.RED + "In Progress");
		sign.update();
		if(i != 5)
			state = i;
	}
	
	public void Open()
	{
		for(Block block : cuboid.getBlocks())
		{
			block.setType(Material.PORTAL);
		}
		setSign(1);
		on = false;
	}
	
	public void Close()
	{
		for(Block block : cuboid.getBlocks())
		{
			block.setType(Material.AIR);
		}
		setSign(5);
		on = true;
	}
}
