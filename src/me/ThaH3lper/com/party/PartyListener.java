package me.ThaH3lper.com.party;

import me.ThaH3lper.com.DungeonHandler;
import me.ThaH3lper.com.DungeonPlayer;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PartyListener
  implements Listener
{
  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
  {
    if (!(event.getEntity() instanceof Player))
      return;
    if ((event.getDamager() instanceof Player))
    {
      Player player = (Player)event.getEntity();
      Player damager = (Player)event.getDamager();
      DungeonPlayer dpDamager = DungeonHandler.getPlayer(damager);
      if (dpDamager.isInParty())
      {
        DungeonPlayer dp = DungeonHandler.getPlayer(player);
        if (dpDamager.getParty().isMember(dp))
        {
          event.setCancelled(true);
        }
        else if (dp.justLeft())
        {
          if (dp.getGhostParty().isMember(dpDamager))
          {
            event.setCancelled(true);
          }
        }
      }
      else if (dpDamager.justLeft())
      {
        DungeonPlayer dp = DungeonHandler.getPlayer(player);
        if (dpDamager.getGhostParty().isMember(dp))
        {
          event.setCancelled(true);
        }
        else if (dp.justLeft())
        {
          if (dpDamager.getGhostParty().equals(dp.getGhostParty()))
          {
            event.setCancelled(true);
          }
        }
      }
    }
    else if ((event.getDamager() instanceof Arrow))
    {
      Arrow a = (Arrow)event.getDamager();
      if ((a.getShooter() instanceof Player))
      {
        Player player = (Player)event.getEntity();
        Player damager = (Player)a.getShooter();
        DungeonPlayer dpDamager = DungeonHandler.getPlayer(damager);
        if (dpDamager.isInParty())
        {
          DungeonPlayer dp = DungeonHandler.getPlayer(player);
          if (dpDamager.getParty().isMember(dp))
          {
            a.remove();
            event.setCancelled(true);
          }
          else if (dp.justLeft())
          {
            if (dp.getGhostParty().isMember(dpDamager))
            {
              a.remove();
              event.setCancelled(true);
            }
          }
        }
        else if (dpDamager.justLeft())
        {
          DungeonPlayer dp = DungeonHandler.getPlayer(player);
          if (dpDamager.getGhostParty().isMember(dp))
          {
            a.remove();
            event.setCancelled(true);
          }
          else if (dp.justLeft())
          {
            if (dpDamager.getGhostParty().equals(dp.getGhostParty()))
            {
              a.remove();
              event.setCancelled(true);
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onPotionSplashEvent(PotionSplashEvent event)
  {
    if (!isOffencive(event.getEntity()))
      return;
    if (!(event.getEntity().getShooter() instanceof Player))
      return;
    DungeonPlayer dp = DungeonHandler.getPlayer((Player)event.getEntity().getShooter());
    if (dp.isInParty())
    {
      for (LivingEntity le : event.getAffectedEntities())
      {
        if ((le instanceof Player))
        {
          DungeonPlayer temp = DungeonHandler.getPlayer((Player)le);
          if (dp.getParty().isMember(temp))
            event.setIntensity(le, 0.0D);
          else if (temp.justLeft())
          {
            if (temp.getGhostParty().isMember(dp))
              event.setIntensity(le, 0.0D); 
          }
        }
      }
    }
    else if (dp.justLeft())
    {
      for (LivingEntity le : event.getAffectedEntities())
      {
        if ((le instanceof Player))
        {
          DungeonPlayer temp = DungeonHandler.getPlayer((Player)le);
          if (dp.getGhostParty().isMember(temp))
            event.setIntensity(le, 0.0D);
          else if (temp.justLeft())
          {
            if (temp.getGhostParty().equals(dp.getGhostParty()))
              event.setIntensity(le, 0.0D);
          }
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
    DungeonPlayer dp = DungeonHandler.getPlayer(event.getPlayer());
    if (!dp.usesPartyChat())
      return;
    event.setCancelled(true);
    dp.getParty().sendMessage(dp.getName(), event.getMessage());
  }

  private boolean isOffencive(ThrownPotion tp)
  {
    for (PotionEffect pe : tp.getEffects())
    {
      if (pe.getType().equals(PotionEffectType.POISON))
        return true;
      if (pe.getType().equals(PotionEffectType.WEAKNESS))
        return true;
      if (pe.getType().equals(PotionEffectType.SLOW))
        return true;
      if (pe.getType().equals(PotionEffectType.HARM))
        return true;
    }
    return false;
  }
}




