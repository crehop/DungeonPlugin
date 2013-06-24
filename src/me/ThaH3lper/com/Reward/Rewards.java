package me.ThaH3lper.com.Reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.MobLibrary;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class Rewards {
	
	List<ItemStack> items = new ArrayList<ItemStack>();
	MobLibrary mb;
	String locs;
	Dungeon dun;
	Random r = new Random();
	
	public Rewards(String s, Dungeon dun)
	{
		this.locs = s;
		this.dun = dun;
		mb = (MobLibrary) Bukkit.getPluginManager().getPlugin("MobLibrary");
	}
	
	public ItemStack TextToItem(String s)
	{
		String[] big = s.split(" ");
		String[] parts = big[0].split(":");
		ItemStack stack = new ItemStack(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]), (short)Integer.parseInt(parts[1]));
		
		float chance = Float.parseFloat(big[1]);
		if(chance > r.nextFloat())
			return stack;
		return null;
	}
	
	public ItemStack ItemToItemstack(String s)
	{
		String[] big = s.split(" ");
		ItemStack stack = mb.loadItems.getItem(big[0]);
		
		float chance = Float.parseFloat(big[1]);
		if(chance > r.nextFloat())
			return stack;
		return null;
	}
	
	public List<ItemStack> getStacks()
	{
		items.clear();
		for(String str: dun.options.getCustomConfig().getStringList(locs))
		{
			ItemStack stack = null;
			if(str.contains(":"))
			{
				stack = TextToItem(str);
			}
			else
			{
				stack = ItemToItemstack(str);
			}
			if(stack != null)
			{
				items.add(stack);
			}
		}
		return items;
	}

}
