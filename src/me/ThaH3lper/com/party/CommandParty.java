package me.ThaH3lper.com.party;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonHandler;
import me.ThaH3lper.com.DungeonPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandParty implements CommandExecutor{
	
	private Dungeon dun;
	
	public CommandParty(Dungeon dun)
	{
		this.dun = dun;
	}
	
   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
   {
     Player player = (Player)sender;
     if ((commandLabel.equalsIgnoreCase("party")) || (commandLabel.equalsIgnoreCase("p")))
     {
       if (args.length == 0)
       {
         DungeonPlayer dp = DungeonHandler.getPlayer(player);
         if (!dp.isInParty())
         {
           dp.sendMessage(ChatColor.RED + "You Need To Be In A Party To Use The Party Chat");
           return false;
         }
         if (dp.usesPartyChat())
         {
           dp.setPartyChat(false);
           dp.sendMessage(ChatColor.DARK_AQUA + "Party Chat Disabled");
         }
         else
         {
           dp.setPartyChat(true);
           dp.sendMessage(ChatColor.DARK_AQUA + "Party Chat Enabled");
         }
       }
       else if (args.length == 1)
       {
         if (args[0].equalsIgnoreCase("accept"))
         {
           DungeonPlayer dp = DungeonHandler.getPlayer(player);
           if (!dp.hasInvite())
           {
             dp.sendMessage(ChatColor.RED + "You Dont Have An Invitation Right Now!");
             return false;
           }
           if (!dp.canAccept())
           {
             dp.sendMessage(ChatColor.RED + "That Party Is Full!");
             dp.setInvite(null);
             return false;
           }
           dp.accept();
           dp.sendMessage(ChatColor.GOLD + "You Joined The Party, Type " + ChatColor.AQUA + "/party leave" + ChatColor.GOLD + " To Leave The Party");
         }
         else if (args[0].equalsIgnoreCase("decline"))
         {
           DungeonPlayer dp = DungeonHandler.getPlayer(player);
           if (!dp.hasInvite())
           {
             dp.sendMessage(ChatColor.RED + "You Dont Have An Invitation Right Now!");
             return false;
           }
           dp.decline();
           dp.sendMessage(ChatColor.GOLD + "You Declined The Invitation");
         }
         else if (args[0].endsWith("leave"))
         {
           final DungeonPlayer dp = DungeonHandler.getPlayer(player);
           if (!dp.isInParty())
           {
             dp.sendMessage(ChatColor.RED + "You Are Not In A Party!");
             return false;
           }
           dp.getParty().leave(dp);
           dp.setGhostParty(dp.getParty());
           dp.setParty(null);
           if (dp.usesPartyChat())
           {
             dp.setPartyChat(false);
             dp.sendMessage(ChatColor.DARK_AQUA + "Party Chat Disabled");
           }
 
           Bukkit.getScheduler().scheduleSyncDelayedTask(dun, new Runnable()
           {
             public void run()
             {
               dp.setGhostParty(null);
             }
           }
           , 200L);
         }
       }
       else if (args.length == 2)
       {
         if ((args[0].equalsIgnoreCase("invite")) || (args[0].equalsIgnoreCase("inv")))
         {
           Player target = dun.getServer().getPlayer(args[1]);
           if (target == null)
           {
             player.sendMessage(ChatColor.RED + "Player Not Found!");
             return false;
           }
           DungeonPlayer dpTarget = DungeonHandler.getPlayer(target);
           if (dpTarget.isInParty())
           {
             player.sendMessage(ChatColor.RED + "That Player Is Already In A Party!");
             return false;
           }
           DungeonPlayer dp = DungeonHandler.getPlayer(player);
           if (dp.isInParty())
           {
             if (dp.isLeader())
             {
               if (dp.getParty().isFull())
               {
                 dp.sendMessage(ChatColor.RED + "You Cannot Invite More People To Your Party It Is Full!");
                 return false;
               }
               if(dp.getParty().indungeon)
               {
            	   dp.sendMessage(ChatColor.RED + "You Can't Invite In Dungeon!");
            	   return false;
               }
               dp.sendInvite(dpTarget);
               dp.sendMessage(ChatColor.AQUA + "Invitation Sent");
             }
             else
             {
               player.sendMessage(ChatColor.RED + "Only The Party Leader Can Invite People!");
               return false;
             }
           }
           else
           {
             dp.sendInvite(dpTarget);
             dp.sendMessage(ChatColor.AQUA + "Invitation Sent");
           }
 
         }
         else if (args[0].equalsIgnoreCase("kick"))
         {
           DungeonPlayer dp = DungeonHandler.getPlayer(player);
           if (!dp.isInParty())
           {
             dp.sendMessage(ChatColor.RED + "You Can Only Use This Command While You're Part Of A Party!");
             return false;
           }
           if (!dp.isLeader())
           {
             dp.sendMessage(ChatColor.RED + "Only The Party Leader Can Use This Command!");
             return false;
           }
           Player target = dun.getServer().getPlayer(args[1]);
           if (target == null)
           {
             player.sendMessage(ChatColor.RED + "Player Not Found!");
             return false;
           }
           final DungeonPlayer dpTarget = DungeonHandler.getPlayer(target);
           if (!dp.getParty().isMember(dpTarget))
           {
             player.sendMessage(ChatColor.RED + "That Player Is Not Part Of Your Party!");
             return false;
           }
           if (dp.equals(dpTarget))
           {
             player.sendMessage(ChatColor.RED + "You Cant Kick Yourself!");
             return false;
           }
           dpTarget.setGhostParty(dp.getParty());
           dpTarget.setParty(null);
           dp.getParty().kick(dpTarget);
           if (dpTarget.usesPartyChat())
           {
             dpTarget.setPartyChat(false);
             dpTarget.sendMessage(ChatColor.DARK_AQUA + "Party Chat Disabled");
           }
           Bukkit.getScheduler().scheduleSyncDelayedTask(dun, new Runnable()
           {
             public void run()
             {
               dpTarget.setGhostParty(null);
             }
           }
           , 200L);
         }
       }
     }
     return true;
   }

}
