package me.ThaH3lper.com.dungeon.commands;

import java.util.Random;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonHandler;
import me.ThaH3lper.com.DungeonPlayer;
import me.ThaH3lper.com.dungeon.Floor;
import me.ThaH3lper.com.dungeon.TempletFloor;
import me.ThaH3lper.com.dungeon.Tower;
import me.ThaH3lper.com.party.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandHandle implements CommandExecutor
{
	private Dungeon dun;
	Random r = new Random();
	
	public CommandHandle(Dungeon dun)
	{
		this.dun = dun;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(!p.hasPermission("Dungeon.Admin"))
			{
				p.sendMessage(ChatColor.RED + "No permision for that!");
				return true;
			}
			if(args.length == 0)
			{
				p.sendMessage(ChatColor.DARK_RED + "vVv----------[" + ChatColor.GOLD + ChatColor.BOLD + " Dungeon " + ChatColor.RESET + ChatColor.DARK_RED + "]----------vVv");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d new templet" + ChatColor.RED + " setup new templet");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d new tower" + ChatColor.RED + " setup new tower");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d new portal" + ChatColor.RED + " setup new portal");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d set top" + ChatColor.RED + " set latest templet to top");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d set exit" + ChatColor.RED + " set exit and error location");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d reward [easy/normal/hard]" + ChatColor.RED + " see reward");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d tower clear" + ChatColor.RED + " remove all buildings");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d portal [open/close] [id]" + ChatColor.RED + " toggle portal");
				p.sendMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/d save" + ChatColor.RED + " save all");
			}
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("save"))
				{
					dun.saveload.SaveAll();
					p.sendMessage(ChatColor.GREEN + "Saved.yml Saved!");
				}
			}
			if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("new") && args[1].equalsIgnoreCase("templet"))
				{
					dun.blocklistener.l1 = null;
					dun.blocklistener.l2 = null;
					dun.blocklistener.l3 = null;
					dun.blocklistener.p = p;
					dun.blocklistener.pef = "floor";
					
					p.sendMessage(ChatColor.RED + "New Templet setup has started!");
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select " + ChatColor.RED +  "first Corner" + ChatColor.GREEN + " of templet");
				}
				if(args[0].equalsIgnoreCase("new") && args[1].equalsIgnoreCase("tower"))
				{
					dun.blocklistener.l1 = null;
					dun.blocklistener.l2 = null;
					dun.blocklistener.l3 = null;
					dun.blocklistener.p = p;
					dun.blocklistener.pef = "tower";
					
					p.sendMessage(ChatColor.RED + "New Tower setup has started!");
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select " + ChatColor.RED +  "Tower origo");
				}
				
				if(args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("top"))
				{
					TempletFloor tf = dun.templetFloorList.get(dun.templetFloorList.size() - 1);
					tf.topfloor = true;
					p.sendMessage(ChatColor.GREEN + "TopFloor is now set! " + ChatColor.GRAY + "and what are you going to do about it?");
				}
				if(args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("exit"))
				{
					dun.saveload.saveExit(p.getLocation());
					p.sendMessage(ChatColor.GREEN + "Exit Location Set!");
				}
				if(args[0].equalsIgnoreCase("reward"))
				{
					Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST);
					if(args[1].equals("easy"))
					{
						for(ItemStack stack : dun.rewEasy.getStacks())
							inv.addItem(stack);
					}
					else if(args[1].equals("normal"))
					{
						for(ItemStack stack : dun.rewNormal.getStacks())
							inv.addItem(stack);
					}
					else if(args[1].equals("hard"))
					{
						for(ItemStack stack : dun.rewHard.getStacks())
							inv.addItem(stack);
					}
					p.openInventory(inv);
				}
			}
			if(args.length == 3)
			{
				if(args[0].equalsIgnoreCase("new") && args[1].equalsIgnoreCase("portal"))
				{
					dun.blocklistener.i1 = Integer.parseInt(args[2]);
					dun.blocklistener.l1 = null;
					dun.blocklistener.l2 = null;
					dun.blocklistener.l3 = null;
					dun.blocklistener.p = p;
					dun.blocklistener.pef = "portal";
					p.sendMessage(ChatColor.RED + "New Portal setup has started!");
					p.sendMessage(ChatColor.GREEN + "Right-Click with a " + ChatColor.GOLD + "Golden Axe" + ChatColor.GREEN + " to select " + ChatColor.RED +  "first Corner" + ChatColor.GREEN + " of Portal");
				}
				if(args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("open"))
				{
					dun.portalList.get(Integer.parseInt(args[2])).Open();
				}
				if(args[0].equalsIgnoreCase("portal") && args[1].equalsIgnoreCase("close"))
				{
					dun.portalList.get(Integer.parseInt(args[2])).Close();
				}
				if(args[0].equalsIgnoreCase("gen"))
				{
					DungeonPlayer dp = DungeonHandler.getPlayer(p);
					if(dp.isInParty())
					{
						Party party = dp.getParty();
						if(dp.isLeader())
						{
							//if(party.isFull())
							//{
								if(!dun.towerList.get(Integer.parseInt(args[1])).generating)
								{
									p.sendMessage(ChatColor.GREEN + "Generating!");
									Tower t = dun.towerList.get(Integer.parseInt(args[1]));
									t.setTower(5);
									t.party = party;
									
								}
								else
									p.sendMessage(ChatColor.RED + "Tower is generating");
							//}
							//else
								//p.sendMessage(ChatColor.RED + "You need to be 5 members in party");
						}
						else
							p.sendMessage(ChatColor.RED + "You are not leader of party");
					}
					else
						p.sendMessage(ChatColor.RED + "You are not in a party");
				}
			}
		}
		else
		{

		}
		return false;
	}
}
