package me.ThaH3lper.com.Crehop;

import me.ThaH3lper.com.DungeonPlayer;
import me.ThaH3lper.com.party.Party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ScoreboardMaker
{
	//static ScoreboardManager manager = Bukkit.getScoreboardManager();
	//static Scoreboard board = manager.getNewScoreboard();
	//static Objective objective = board.getObjective("showhealth");
	
	//public static void newObjective()
	//{
    //	board.registerNewObjective("kills", "number");
	//}

	public static void createScoreboard(Player player)
	{
		String message = "SIDEBAR,Health,Dungeon,0";
    	Bukkit.getMessenger().dispatchIncomingMessage(player, "Scoreboard", message.getBytes());
    	//objective = board.getObjective("kills");
    	//objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    	//objective.setDisplayName(ChatColor.RED + "=== Mobs Killed ===");
    	//player.setScoreboard(board);
    }
	
    public static void setScore(Player player, int score)
    {
    	
    	
    	//Score scores = objective.getScore(player);
    	//scores.setScore(currentScore + score); 
    	String message = "SIDEBAR,Health," + ChatColor.RED + "=== Mobs Killed ===" + ",-" + score;
    	Bukkit.getMessenger().dispatchIncomingMessage(player, "Scoreboard", message.getBytes());
    }
    
    public static void setDungeonFloor(Party party, int floor, int totalFloors)
    {
    	//Score theFloor = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "FLOOR"));
    	//Score totalFloor = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "TOTAL_FLOORS"));
    	//theFloor.setScore(floor);
    	//totalFloor.setScore(totalFloors);
    	for(DungeonPlayer dp : party.getMembers())
    	{
    		String message = "SIDEBAR,Health," + ChatColor.GREEN + "FLOOR" + "," + floor;
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    		message = "SIDEBAR,Health," + ChatColor.YELLOW + "TOTAL_FLOORS" + "," + totalFloors;
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    	}
    	
    }
    
    public static void removeScoreboards(Party party)
    {
    	String message;
    	for(DungeonPlayer dp : party.getMembers())
    	{
    		message = "REMOVE,Dungeon";
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    		
    		message = "REMOVE," + ChatColor.RED + "=== Mobs Killed ===";
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    		
    		message = "REMOVE," + ChatColor.GREEN + "FLOOR";
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    		
    		message = "REMOVE," + ChatColor.YELLOW + "TOTAL_FLOORS";
    		Bukkit.getMessenger().dispatchIncomingMessage(dp.getPlayer(), "Scoreboard", message.getBytes());
    	}
    	//board.clearSlot(DisplaySlot.SIDEBAR);
    	//objective.unregister();
    }
    
}