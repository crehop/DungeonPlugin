package me.ThaH3lper.com.dungeon;

import me.ThaH3lper.com.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TempletFloor {
	
	public Location origo;
	
	public Cuboid cubeId;
	private Dungeon dun;
	public boolean topfloor = false;
	
	public TempletFloor(Cuboid cuboId, Location origo, Dungeon dun)
	{
		this.dun = dun;
		this.cubeId = cuboId;
		this.origo = origo;
	}
	
	public Cuboid getNewCuboid(Location origo)
	{
		int x1 = origo.getBlockX() + (cubeId.getMinLoc().getBlockX() - this.origo.getBlockX());
		int y1 = origo.getBlockY() + (cubeId.getMinLoc().getBlockY() - this.origo.getBlockY());
		int z1 = origo.getBlockZ() + (cubeId.getMinLoc().getBlockZ() - this.origo.getBlockZ());
		
		int x2 = origo.getBlockX() + (cubeId.getMaxLoc().getBlockX() - this.origo.getBlockX());
		int y2 = origo.getBlockY() + (cubeId.getMaxLoc().getBlockY() - this.origo.getBlockY());
		int z2 = origo.getBlockZ() + (cubeId.getMaxLoc().getBlockZ() - this.origo.getBlockZ());
		
		Cuboid cubo = new Cuboid(new Location(origo.getWorld(), x1, y1, z1), new Location(origo.getWorld(), x2, y2, z2), dun);
		return cubo;
	}
	
	public int getLower()
	{
		int y1 = origo.getBlockY() + (cubeId.getMinLoc().getBlockY() - origo.getBlockY());
		return origo.getBlockY() - y1;
	}
	public int getTop()
	{
		int y2 = origo.getBlockY() + (cubeId.getMaxLoc().getBlockY() - origo.getBlockY());
		return y2 - origo.getBlockY();
	}
	
	public int getHeight()
	{
		int height = (cubeId.getMaxLoc().getBlockY() - cubeId.getMinLoc().getBlockY());
		return height;
	}
}

