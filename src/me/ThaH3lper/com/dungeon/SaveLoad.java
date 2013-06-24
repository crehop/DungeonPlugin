package me.ThaH3lper.com.dungeon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.Grades.Wave;
import me.ThaH3lper.com.Portals.Portal;

public class SaveLoad {
	
	private Dungeon dun;
	
	public SaveLoad(Dungeon dun)
	{
		this.dun = dun;
		
		LoadTempletFloor();
		loadTower();
		loadExit();
		loadPortals();
		loadWaves();
	}
	
	public void SaveAll()
	{
		SaveTempletFloor();
		saveTower();
		savePortals();
	}	
	public void LoadTempletFloor()
	{
		List<String> loadlist = dun.save.getCustomConfig().getStringList("TempletFloor");
		for(String s : loadlist)
		{
			boolean top = false;
			if(s.contains("%top"))
			{
				s = s.replace("%top", "");
				top = true;
			}
			String[] parts = s.split(":");
			
			Cuboid cuboid = new Cuboid(stringToLocation(parts[0]), stringToLocation(parts[1]), dun);
			Location origo = stringToLocation(parts[2]);
			
			TempletFloor tf = new TempletFloor(cuboid, origo, dun);
			tf.topfloor = top;
			dun.templetFloorList.add(tf);
		}
	}
	public void SaveTempletFloor()
	{
		List<String> savelist = new ArrayList<String>();
		for(TempletFloor tf : dun.templetFloorList)
		{
			String save = locationToString(tf.cubeId.getMinLoc()) + ":" + locationToString(tf.cubeId.getMaxLoc()) + ":" + locationToString(tf.origo);
			if(tf.topfloor)
				save += "%top";
			savelist.add(save);		
		}
		dun.save.getCustomConfig().set("TempletFloor", savelist);
		dun.save.saveCustomConfig();
	}
	
	//---------------------------------------
	public void saveTower()
	{
		List<String> savelist = new ArrayList<String>();
		for(Tower t : dun.towerList)
		{
			String s = locationToString(t.origo);
			for(Floor f : t.floors)
			{
				s += ":" + locationToString(f.cuboID.getMinLoc()) + ";" + locationToString(f.cuboID.getMaxLoc());
			}
			savelist.add(s);			
		}
		dun.save.getCustomConfig().set("Tower", savelist);
		dun.save.saveCustomConfig();
	}
	
	public void loadTower()
	{
		List<String> loadlist = dun.save.getCustomConfig().getStringList("Tower");
		{
			for(String s : loadlist)
			{
				String main = "";
				String[] split = null;
				
				boolean tower = false;
				if(s.contains(":"))
				{
					split = s.split(":");
					main = split[0];
					//dun.logger.warning("[Dungeon] Got Tower");
					tower = true;
				}
				else
				{
					main = s;
			    	 //dun.logger.warning("[Dungeon] No Tower");
				}
				
				Location loc = stringToLocation(main);
				Tower t = new Tower(loc, dun);
				t.hasTower = tower;
				dun.towerList.add(t);
				
				if(split != null)
				{
					int i = 0;
					for(String str : split)
					{
						if(str.contains(";"))
						{
							String[] dub = str.split(";");
							Floor f = new Floor(new Cuboid(stringToLocation(dub[0]), stringToLocation(dub[1]), dun), t, i, null, dun);
							t.floors.add(f);
							i++;
						}
					}
				}
			}
		}
	}
	
	public void saveExit(Location loc)
	{
		String s = locationToString(loc);
		dun.exit = loc;
		dun.save.getCustomConfig().set("ExitLoc", s);
		dun.save.saveCustomConfig();		
	}
	
	public void loadExit()
	{
		if(dun.save.getCustomConfig().contains("ExitLoc"))
		{
			String s = dun.save.getCustomConfig().getString("ExitLoc");
			Location l = stringToLocation(s);
			dun.exit = l;
		}	
	}
	
	public void savePortals()
	{
		String s = "";
		List<String> saved = new ArrayList<String>();
		
		for(Portal portal : dun.portalList)
		{
			s = "";
			s += locationToString(portal.cuboid.getMaxLoc()) + ":";
			s += locationToString(portal.cuboid.getMinLoc()) + ":";
			s += locationToString(portal.locSign) + ":";
			s += portal.tower;
			saved.add(s);
		}
		
		dun.save.getCustomConfig().set("Portals", saved);
		dun.save.saveCustomConfig();
	}
	
	public void loadPortals()
	{
		List<String> loadlist = dun.save.getCustomConfig().getStringList("Portals");
		
		for(String s : loadlist)
		{
			String[] parts = s.split(":");
			Location l1 = stringToLocation(parts[0]);
			Location l2 = stringToLocation(parts[1]);
			Location l3 = stringToLocation(parts[2]);
			int i = Integer.parseInt(parts[3]);
			Portal portal = new Portal(new Cuboid(l1, l2, dun), i, l3);
			dun.portalList.add(portal);
		}
	}
	
	public void loadWaves()
	{
		for(String s : dun.waves.getCustomConfig().getConfigurationSection("").getKeys(false))
		{
			List<String> wave = dun.waves.getCustomConfig().getStringList(s);
			dun.waveList.add(new Wave(wave, s));
		}
	}
	
	//Locations
	public String locationToString(Location loc)
	{
		String save = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
		return save;
	}
	
	public Location stringToLocation(String s)
	{
		String[] parts = s.split(",");
		World world = dun.getServer().getWorld(parts[0]);
		float x = Float.parseFloat(parts[1]);
		float y = Float.parseFloat(parts[2]);
		float z = Float.parseFloat(parts[3]);
		
		return new Location(world, x, y, z);
	}

}
