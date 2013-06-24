package me.ThaH3lper.com.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

public class Party
{
   private List<DungeonPlayer> members = new ArrayList<DungeonPlayer>(5);
   private DungeonPlayer leader;
   public boolean indungeon = false;
   Dungeon dun;
 
   public Party(DungeonPlayer dp, Dungeon dun)
   {
	 this.dun = dun;
     this.leader = dp;
     this.members.add(dp);
   }
 
   public boolean isMember(DungeonPlayer dp)
   {
     return this.members.contains(dp);
   }
   
   public List<DungeonPlayer>getMembers()
   {
     return members;
   }
   
   public boolean isLeader(DungeonPlayer dp)
   {
     return this.leader.equals(dp);
   }
 
   public boolean addPlayer(DungeonPlayer dp)
   {
     dp.setParty(this);
     if (this.members.add(dp))
     {
       for (DungeonPlayer m : this.members)
       {
         if (!m.equals(dp))
         {
           m.sendMessage(ChatColor.AQUA + dp.getName() + ChatColor.GOLD + " Joined The Party");
         }
       }
       return true;
     }
     return false;
   }
 
   public boolean isFull()
   {
     return this.members.size() >= 5;
   }
 
   public void checkDisband()
   {
     if (this.members.size() <= 1)
     {
       this.leader.sendMessage(ChatColor.GOLD + "Your Party Got Disbanded, Friendly Fire Will Stay Off For 10 More Seconds");
       this.leader.setParty(null);
       this.leader.setGhostParty(this);
       if (this.leader.usesPartyChat())
       {
         this.leader.setPartyChat(false);
         this.leader.sendMessage(ChatColor.DARK_AQUA + "Party Chat Disabled");
       }
 
       Bukkit.getScheduler().scheduleSyncDelayedTask(dun, new Runnable()
       {
         public void run()
         {
           Party.this.leader.setGhostParty(null);
         }
       }
       , 200L);
     }
   }
 
   public void leave(DungeonPlayer dp)
   {
     this.members.remove(dp);
     dp.sendMessage(ChatColor.GOLD + "You Left The Party, Friendly Fire Will Stay Off For 10 More Seconds");
     for (DungeonPlayer temp : this.members)
     {
       temp.sendMessage(ChatColor.AQUA + dp.getName() + ChatColor.GOLD + " Has Left The Party, Friendly Fire Will Stay Off For 10 More Seconds");
     }
     if (isLeader(dp))
     {
       Random object = new Random();
       this.leader = ((DungeonPlayer)this.members.get(object.nextInt(this.members.size())));
       this.leader.sendMessage(ChatColor.GOLD + "You Are Now Leader Of The Party");
       for (DungeonPlayer temp : this.members)
       {
         if (!isLeader(temp))
         {
           temp.sendMessage(ChatColor.AQUA + this.leader.getName() + ChatColor.GOLD + " Is Now Leader Of The Party");
         }
       }
     }
     checkDisband();
   }
 
   public void kick(DungeonPlayer dp)
   {
     this.members.remove(dp);
     dp.sendMessage(ChatColor.GOLD + "You Got Kicked From The Party, Friendly Fire Will Stay Off For 10 More Seconds");
     for (DungeonPlayer temp : this.members)
     {
       temp.sendMessage(ChatColor.AQUA + dp.getName() + ChatColor.GOLD + " Got Kicked From The Party, Friendly Fire Will Stay Off For 10 More Seconds");
     }
     checkDisband();
   }
 
   public void sendMessage(String sender, String msg)
   {
     for (DungeonPlayer temp : this.members)
     {
       temp.sendMessage(ChatColor.DARK_AQUA + "[Party] " + ChatColor.GREEN + sender + ChatColor.DARK_AQUA + " " + msg);
     }
   }
   public void sendOfficialstart(String msg)
   {
     for (DungeonPlayer temp : this.members)
     {
       temp.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + leader.getName() + ChatColor.GRAY + " has started a dungeon! " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + msg + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Your dungeon is being created please wait. when progress reaches " + ChatColor.GREEN + "100%" + ChatColor.GRAY + " you will be teleported");
     }
   }
   public void sendMsg(String msg)
   {
     for (DungeonPlayer temp : this.members)
     {
       temp.sendMessage(msg);
     }
   }
 }




