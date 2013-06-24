 package me.ThaH3lper.com;

 import java.util.Date;

import me.ThaH3lper.com.party.Invite;
import me.ThaH3lper.com.party.Party;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
 
 public class DungeonPlayer
 {
   private Player player;
   private long defencePotionTimer = 0L;
   private long offencePotionTimer = 0L;
   private long pvpTimer = 0L;
   private long portalTimer = 0L;
   private Party party;
   private Invite invite;
   private Party ghostParty;
   private boolean partyChat;
   private Dungeon dun;
 
   public DungeonPlayer(Player player, Dungeon dun)
   {
	   this.dun = dun;
	   this.player = player;
   }
 
   public Player getPlayer()
   {
     return this.player;
   }
 
   public void setDefencePotionTimer(long time)
   {
     this.defencePotionTimer = time;
   }
 
   public void setOffencePotionTimer(long time)
   {
     this.offencePotionTimer = time;
   }
   
   public void setPortalTimer(long time)
   {
     this.portalTimer= time;
   }
   
   public long getPortalTimer()
   {
     return this.portalTimer;
   }
 
   public long getOffencePotionTimer()
   {
     return this.offencePotionTimer;
   }
 
   public long getDefencePotionTimer()
   {
     return this.defencePotionTimer;
   }
 
   public String getName()
   {
     return this.player.getName();
   }
 
   public long getPvpTimer()
   {
     return this.pvpTimer;
   }
 
   public void setPvpTimer(long timer)
   {
     this.pvpTimer = timer;
   }
 
   public boolean isInPVP()
   {
     Date d = new Date();
     if ((d.getTime() - this.pvpTimer) / 1000L <= DungeonHandler.getPvpTimer()) {
       return true;
     }
     return false;
   }
 
   public boolean isInParty()
   {
     if (this.party == null) {
       return false;
     }
     return true;
   }
 
   public boolean isLeader()
   {
     if (this.party == null)
       return false;
     return this.party.isLeader(this);
   }
 
   public void sendMessage(String s)
   {
     this.player.sendMessage(s);
   }
 
   public Party getParty()
   {
     return this.party;
   }
 
   public void setInvite(Invite invite)
   {
     this.invite = invite;
   }
 
   public void setParty(Party p)
   {
     this.party = p;
   }
 
   public boolean hasInvite()
   {
     return this.invite != null;
   }
 
   public void sendInvite(DungeonPlayer target)
   {
     Invite invite = new Invite(this, dun);
     target.setInvite(invite);
     target.sendMessage(ChatColor.AQUA + this.player.getName() + ChatColor.GOLD + " Has Invited You To His Party");
     target.sendMessage(ChatColor.GOLD + "Type " + ChatColor.AQUA + "/party accept " + ChatColor.GOLD + " To Accept Or " + ChatColor.AQUA + "/party decline" + ChatColor.GOLD + " To Decline");
   }
 
   public void accept()
   {
     if (this.invite != null)
     {
       this.invite.accept(this);
       this.invite = null;
     }
   }
 
   public void decline()
   {
     if (this.invite != null)
     {
       this.invite.decline(this);
       this.invite = null;
     }
   }
 
   public boolean canAccept()
   {
     if (this.invite.getParty() == null)
       return true;
     if (this.invite.isPartyFull())
       return false;
     return true;
   }
 
   public boolean hasGhostParty()
   {
     return this.ghostParty != null;
   }
 
   public void setGhostParty(Party p)
   {
     this.ghostParty = p;
   }
 
   public boolean justLeft()
   {
     return this.ghostParty != null;
   }
 
   public Party getGhostParty()
   {
     return this.ghostParty;
   }
 
   public void setPartyChat(boolean value)
   {
     this.partyChat = value;
   }
 
   public boolean usesPartyChat()
   {
     return this.partyChat;
   }
 }




