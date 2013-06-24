package me.ThaH3lper.com.Crehop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardMaker {
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard board = manager.getNewScoreboard();
	static Objective objective = board.getObjective("showhealth");
	public static void newObjective(){
    	board.registerNewObjective("kills", "number");
	}

	public static void createScoreboard(Player player) {
    	objective = board.getObjective("kills");
    	objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    	objective.setDisplayName(ChatColor.RED + "=== Mobs Killed ===");
    	player.setScoreboard(board);
    	}
    public static void setScore(Player player, int score){
    	Score scores = objective.getScore(player);
    	int currentScore = scores.getScore();
    	scores.setScore(currentScore + score); 
    }
    public static void setDungeonFloor(int floor, int totalFloors){
    	Score theFloor = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "FLOOR"));
    	Score totalFloor = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "TOTAL_FLOORS"));
    	theFloor.setScore(floor);
    	totalFloor.setScore(totalFloors);
    }
    public static void removeScoreboards(){
    	board.clearSlot(DisplaySlot.SIDEBAR);
    	objective.unregister();
    }
    
}