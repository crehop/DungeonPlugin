package me.ThaH3lper.com.dungeon.commands;

import java.util.Date;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonHandler;
import me.ThaH3lper.com.DungeonPlayer;
import me.ThaH3lper.com.Portals.Portal;
import me.ThaH3lper.com.dungeon.Cuboid;
import me.ThaH3lper.com.dungeon.TempletFloor;
import me.ThaH3lper.com.dungeon.Tower;
import me.ThaH3lper.com.party.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener{
	
	private Dungeon dun;
	
	Player p = null;
	String pef = "";
	
	int i1 = 0;
	Location l1 = null;
	Location l2 = null;
	Location l3 = null;
	
	public BlockListener(Dungeon dun)
	{
		this.dun = dun;
	}

	  @EventHandler
	  public void onPlayerInteractEvent(PlayerInteractEvent event)
	  {	  
		    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		    	return;
		    if(event.getClickedBlock().getState() instanceof Sign)
		    {
		    	for(Portal portal : dun.portalList)
		    	{
		    		if(portal.locSign.equals(event.getClickedBlock().getLocation()))
		    		{
		    			if(portal.on)
		    			{
		    				return;
		    			}
		    			if(portal.state == 3)
		    				portal.setSign(1);
		    			else
		    				portal.setSign(portal.state + 1);
		    		}
		    	}
		    }
		    ItemStack is = event.getPlayer().getItemInHand();
		    if (!is.getType().equals(Material.GOLD_AXE))
		    	return;
		    if (!event.getPlayer().equals(p))
		    	return;
		    if(pef.equals("floor"))
		    {
			    if(l1 == null)
			    {
			    	l1 = event.getClickedBlock().getLocation();
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select " + ChatColor.RED + "second Corner" + ChatColor.GREEN +  " of templet");
				}
			    else if(l2 == null)
				{
					l2 = event.getClickedBlock().getLocation();
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select the " + ChatColor.RED + "Origo" + ChatColor.GREEN +  " of templet");
				}
			    else if(l3 == null)
				{
					l3 = event.getClickedBlock().getLocation();
					TempletFloor tf = new TempletFloor(new Cuboid(l1, l2, dun), l3, dun);
					dun.templetFloorList.add(tf);
					p.sendMessage(ChatColor.GREEN + "Templet has been " + ChatColor.GOLD + "Created!");
					p = null;
					pef = "";
			    }
		    }
		    else if(pef.equals("tower"))
		    {
			    if(l1 == null)
				{
					l1 = event.getClickedBlock().getLocation();
					Tower tower = new Tower(l1, dun);
					dun.towerList.add(tower);
					p.sendMessage(ChatColor.GREEN + "Tower has been " + ChatColor.GOLD + "Created!");
					p = null;
					pef = "";
			    }
		    }
		    else if(pef.equals("portal"))
		    {
			    if(l1 == null)
			    {
			    	l1 = event.getClickedBlock().getLocation();
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select " + ChatColor.RED + "second Corner" + ChatColor.GREEN +  " of Portal");
				}
			    else if(l2 == null)
				{
					l2 = event.getClickedBlock().getLocation();
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select the " + ChatColor.RED + "Sign" + ChatColor.GREEN +  " of Portal");
				}
			    else if(l3 == null)
				{
					l3 = event.getClickedBlock().getLocation();
					Portal portal = new Portal(new Cuboid(l1, l2, dun), i1, l3);
					dun.portalList.add(portal);
					portal.Open();
					p.sendMessage(ChatColor.GREEN + "Portal has been " + ChatColor.GOLD + "Created!");
					p = null;
					pef = "";
			    }
		    }
	  }
	  @EventHandler
	  public void onBlockPhysics(BlockPhysicsEvent event)
	  {		
		  if(event.getBlock().getType() == Material.PORTAL)
		  {
			  for(Portal portal : dun.portalList)
			  {
				  for(Block block : portal.cuboid.getBlocks())
				  {
					  if(block.equals(event.getBlock()))
					  {
						  event.setCancelled(true);
					  }
				  }
			  }
		  }
	  }
	  @EventHandler
	  public void onPortalEnter(EntityPortalEnterEvent event)
	  {		
		  for(Portal portal : dun.portalList)
		  {
			  for(Block block:portal.cuboid.getBlocks())
			  {
				  if(block.getLocation().equals(event.getLocation()))
				  {
					  if(event.getEntity() instanceof Player)
					  {
						  Player player = (Player) event.getEntity();
						  if(player.getGameMode() != GameMode.CREATIVE)
						  {
							  //EnterPortal
							  if(!enterPortal(player, portal))
							  {
								  player.teleport(dun.exit);
							  }
						  }
					  }
				  }
			  }
		  }
	  }
	  
	  public boolean enterPortal(Player player, Portal portal)
	  {
			DungeonPlayer dp = DungeonHandler.getPlayer(player);
			long time = new Date().getTime();
			if((time - dp.getPortalTimer()) / 1000L < 3)
			{
				return true;
			}
			dp.setPortalTimer(time);
			if(!player.hasPermission("Dungeon.Use"))
			{
				player.sendMessage(ChatColor.RED + "No permision for that!");
				return false;
			}
			if(dp.isInParty())
			{
				Party party = dp.getParty();
				if(!party.indungeon)
				{
					if(dp.isLeader())
					{
						if(party.isFull() || dp.getPlayer().hasPermission("Dungeon.Passby"))
						{
							if(!dun.towerList.get(portal.tower).generating)
							{
								boolean Members = false;
								for(DungeonPlayer p : party.getMembers())
								{
									if(dun.playerTimes.getTime(p.getPlayer()) != null)
									{
										party.sendMsg(dun.playerTimes.getTime(p.getPlayer()));
										Members = true;
									}
								}
								if(!Members)
								{
									Tower t = dun.towerList.get(portal.tower);
									if(portal.state == 1)
									{
										party.sendOfficialstart("Easy");
										t.setTower(dun.graEasy.levels);
									}
									else if(portal.state == 2)
									{
										party.sendOfficialstart("Normal");
										t.setTower(dun.graNormal.levels);
									}
									else if(portal.state == 3)
									{
										party.sendOfficialstart("Hard");
										t.setTower(dun.graHard.levels);
									}
									t.party = party;
									party.indungeon = true;
									portal.Close();
									return true;
								}
								
							}
							else
								player.sendMessage(ChatColor.RED + "Tower is generating");
						}
						else
							p.sendMessage(ChatColor.RED + "You need to be 5 members in party");
					}
					else
						player.sendMessage(ChatColor.RED + "You are not leader of party");
				}
				else
					player.sendMessage(ChatColor.RED + "Party is already in Dungeon");
			}
			else
				player.sendMessage(ChatColor.RED + "You are not in a party"); 
			
			return false;
	  }

}
