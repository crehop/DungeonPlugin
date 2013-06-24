package me.ThaH3lper.com.Reward;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.ThaH3lper.com.Dungeon;

public class PlayerTimes {
	
	Dungeon dun;
	
	List<String> players = new ArrayList<String>();
	
	public PlayerTimes(Dungeon dun)
	{
		this.dun = dun;
		loadPlayers();
	}
	
	public void addPlayer(Player p)
	{
		if(!p.hasPermission("Dungeon.Passby"))
		{
			Date date = new Date();
			players.add(p.getName() + ":" + date.getTime());
			dun.player.getCustomConfig().set("Players", players);
			dun.player.saveCustomConfig();
		}
	}
	
	public void loadPlayers()
	{
		players = dun.player.getCustomConfig().getStringList("Players");
	}
	
	public String getTime(Player p)
	{
		for(String s : players)
		{
			String[] part = s.split(":");
			if(part[0].equals(p.getName()))
			{
				Date date = new Date();
				if(date.getTime() >= Long.parseLong(part[1]) + 43200000) // 12h
				{
					return null;
				}
				else
				{
					Long l = (43200000 - (date.getTime() - Long.parseLong(part[1])))/60000;
					return (ChatColor.RED + p.getName() +  ChatColor.GRAY + " Can't join dungeon until " + ChatColor.GREEN + l + " Mins");
				}
			}
		}
		return null;
	}
}
