package me.ThaH3lper.com.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonPlayer;
import me.ThaH3lper.com.Crehop.DungeonFloor;
import me.ThaH3lper.com.Portals.Portal;
import me.ThaH3lper.com.party.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Tower {
	
	public Location origo;
	private Dungeon dun;
	
	public boolean hasTower;
	public boolean generating;
	public Party party;
	public DungeonFloor df;
	
	int g = 0;
	int bg = 100;
	int level = -1;
	int scedual;
	
	public List<Floor> floors;
	Random r;
	
	public Tower(Location origo, Dungeon dun)
	{
		this.origo = origo;
		this.dun = dun;
		floors = new ArrayList<Floor>();
		r = new Random();
	}
	
	public void setTower(int levels)
	{
		generating =  true;
		Location pefOrigo = origo;
		for(int i = 0; i < levels - 1; i++)
		{
			TempletFloor tf = dun.templetFloorList.get(r.nextInt(dun.templetFloorList.size()));
			if(tf.topfloor)
				i--;
			else
			{
				pefOrigo = new Location(pefOrigo.getWorld(), pefOrigo.getBlockX(), pefOrigo.getBlockY() + (tf.getLower() + 1), pefOrigo.getBlockZ());
				Cuboid cube = tf.getNewCuboid(pefOrigo);
				pefOrigo = new Location(pefOrigo.getWorld(), pefOrigo.getBlockX(), pefOrigo.getBlockY() + tf.getHeight(), pefOrigo.getBlockZ());
				Floor floor = new Floor(cube, this, i, tf, dun);
				floors.add(floor);
			}
		}
		
		if(getTop()!= null)
		{
			TempletFloor tf = getTop();
			pefOrigo = new Location(pefOrigo.getWorld(), pefOrigo.getBlockX(), pefOrigo.getBlockY() + (tf.getLower() + 1), pefOrigo.getBlockZ());
			Cuboid cube = tf.getNewCuboid(pefOrigo);
			pefOrigo = new Location(pefOrigo.getWorld(), pefOrigo.getBlockX(), pefOrigo.getBlockY() + tf.getHeight(), pefOrigo.getBlockZ());
			Floor floor = new Floor(cube, this, levels, tf, dun);
			floors.add(floor);
		}
		Generate(1, 1);
		hasTower = true;
	}
	
	public void Generate(final int mode, final int pastemode)
	{
		if(level == -1)
		{
			final int i = dun.options.getCustomConfig().getInt("Numberofblocks");
			g = 0;
			level = 0;
			final float per = 50/floors.size();
			scedual = dun.getServer().getScheduler().scheduleSyncRepeatingTask(dun, new Runnable() {
			    @Override  
			    public void run() {
					for(Floor f : floors.subList(level, floors.size()))
					{
						boolean generate = true;
						if(mode == 1)
							generate = f.gen(g, bg, pastemode);
						else
							generate = f.clear(g, bg, pastemode);
						if(!generate)
						{
							g = bg;
							bg += i;
							return;
						}
						else
						{
							g = 0;
							bg = i;
							level++;
							if(party != null && mode == 1 && pastemode == 1)
								party.sendMsg(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + (level * per) + "%");
							if(party != null && mode == 1 && pastemode == 2)
								party.sendMsg(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + ((level * per) + 50) + "%");
							if(level == floors.size())
								stop(mode, pastemode);
						}
					}
			    }
			}, 0L, dun.options.getCustomConfig().getLong("Delay"));
		}
	}
	public void stop(int mode, int pastemode)
	{
		dun.getServer().getScheduler().cancelTask(scedual);
		level = -1;
		if(mode == 0 && pastemode == 2)
		{
			floors.clear();
			hasTower = false;
		}
		if(pastemode == 1 && mode == 1)
			Generate(1, 2);
		if(pastemode == 1 && mode == 0)
			Generate(0, 2);
		if(pastemode == 2)
		{
			generating = false;
			start(this);
			//Done
		}
	}
	
	public TempletFloor getTop()
	{
		for(TempletFloor tf : dun.templetFloorList)
		{
			if(tf.topfloor)
			{
				return tf;
			}
		}
		return null;
	}
	
	public void start(Tower t)
	{
		if(party != null)
		{
			if(party.isFull())
			{
				Generate(0, 1);
				party.sendMsg(ChatColor.RED + "Party is not full anymore! Party needs to be full to start dungeon!");
				party.indungeon = false;
				party = null;
				end();
				return;
			}
			Location spawn = new Location(t.origo.getWorld(), t.origo.getX(), t.origo.getY() - 4, t.origo.getZ());
			if(getPortal().state == 1)
			{
				df = new DungeonFloor(this, party, dun, spawn, getMulti(getPortal()), dun.graEasy);
				df.setLives(dun.graEasy.lives);
			}
			else if(getPortal().state == 2)
			{
				df = new DungeonFloor(this, party, dun, spawn, getMulti(getPortal()), dun.graNormal);
				df.setLives(dun.graNormal.lives);
			}
			else if(getPortal().state == 3)
			{
				df = new DungeonFloor(this, party, dun, spawn, getMulti(getPortal()), dun.graHard);
				df.setLives(dun.graHard.lives);
			}
			df.setLives(3);
			df.setTotalFloors(floors.size());
			for(DungeonPlayer p : party.getMembers())
			{
				dun.playerTimes.addPlayer(p.getPlayer());
			}
		}
		else
		{
			getPortal().Open();
		}
	}
	
	public void end()
	{
		df = null;
	}
	public Portal getPortal()
	{
		int i = dun.towerList.indexOf(this);
		for(Portal p : dun.portalList)
		{
			if(p.tower == i)
			{
				return p;
			}
		}
		return null;
	}
	public float getMulti(Portal p)
	{
		float f = 1;
		if(p.state == 3)
		{
			f = dun.graEasy.boost;
		}
		else if(p.state == 2)
		{
			f = dun.graNormal.boost;
		}
		else if(p.state == 1)
		{
			f = dun.graHard.boost;
		}
		return f;
	}
}
