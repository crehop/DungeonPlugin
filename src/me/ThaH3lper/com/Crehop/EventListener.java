package me.ThaH3lper.com.Crehop;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.dungeon.Tower;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class EventListener implements Listener{

		public static Dungeon plugin;
		public EventListener(Dungeon instance){
			plugin = instance;
		}	

	@EventHandler	
	//event type replaces PlayerInteractEntityEvent
	//Remove this shit, Use  wave to add to dungeon list
	public void mobDeath(EntityDeathEvent event){
		Entity theCreature = event.getEntity();
		
		for(Tower t : plugin.towerList)
		{
			if(t.df != null)
			{
				if(t.df.inThisDungeon.contains(theCreature))
				{
					t.df.creatureDeath(theCreature, event.getEntity().getKiller());
				}
			}
		}
	}
	
	@EventHandler
	//EventHandler, Cheak if player is in Dungeon or not!
	public void PlayerDamage(PlayerDeathEvent e){
		Player player = (Player) e.getEntity();
		
		if(getPlayerTower(player) != null)
		{
			DungeonFloor df = getPlayerTower(player);
			player.setHealth(20);
			player.teleport(df.spawn);
			df.minusLife(player);
		}
	}
	@EventHandler
	public void playerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
	}
	
	public DungeonFloor getPlayerTower(Player p)
	{
		for(Tower t : plugin.towerList)
		{
			if(t.df != null)
			{
				if(t.df.isPlayerInDungeon(p))
				{
					return t.df;
				}
			}
		}
		
		return null;
	}
}
	
		
		