 package me.ThaH3lper.com.party;
 
 import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonPlayer;

import org.bukkit.ChatColor;
 
 public class Invite
 {
   private DungeonPlayer sender;
   private Party party;
   Dungeon dun;
 
   public Invite(DungeonPlayer dp, Dungeon dun)
   {
	 this.dun = dun;
     this.sender = dp;
     this.party = dp.getParty();
   }
 
   public DungeonPlayer getSender()
   {
     return this.sender;
   }
 
   public void accept(DungeonPlayer dp)
   {
     if (this.party == null)
     {
       this.party = new Party(this.sender, dun);
       this.sender.setParty(this.party);
     }
     this.party.addPlayer(dp);
   }
 
   public void decline(DungeonPlayer dp)
   {
     this.sender.sendMessage(ChatColor.AQUA + dp.getName() + ChatColor.RED + " Declined Your Invitation!");
   }
 
   public boolean isPartyFull()
   {
     return this.party.isFull();
   }
 
   public Party getParty()
   {
     return this.party;
   }
 }




