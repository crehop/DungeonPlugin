package me.ThaH3lper.com.dungeon;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.MobLibrary;
import me.ThaH3lper.com.Crehop.DungeonFloor;
import me.ThaH3lper.com.Entitys.MobTemplet;

public class SpawnEntitys {
	
	private Dungeon dun;
	MobLibrary mb;
	Random r = new Random();
	
	public SpawnEntitys(Dungeon dun)
	{
		this.dun = dun;
		mb = (MobLibrary) Bukkit.getPluginManager().getPlugin("MobLibrary");
	}
	
	//Easy way of spawning! Very random atm! Needs a wave system!
	public void SpawnMob(List<Block> list, DungeonFloor f, float multi, String name)
	{
		Location l = list.get(r.nextInt(list.size())).getLocation();
		l.setY(l.getY() + 1);
		LivingEntity live = mb.mobHandler.SpawnAPI(name, l, multi);
		f.inThisDungeon.add(live);
	}


}
