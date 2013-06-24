package me.ThaH3lper.com.Crehop;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.ThaH3lper.com.Dungeon;
import me.ThaH3lper.com.DungeonPlayer;
import me.ThaH3lper.com.Grades.Grade;
import me.ThaH3lper.com.Grades.Wave;
import me.ThaH3lper.com.Portals.Portal;
import me.ThaH3lper.com.Reward.Rewards;
import me.ThaH3lper.com.dungeon.Floor;
import me.ThaH3lper.com.dungeon.Tower;
import me.ThaH3lper.com.party.Party;
import net.minecraft.server.v1_5_R3.ItemSaddle;
import net.minecraft.server.v1_5_R3.PlayerInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


public class DungeonFloor {
	private Random random = new Random();
	private int mobsPerFloor;
	private int mobsLeft;
	private int teamLives;
	private int floor = 1;
	private int totalFloors;
	private int wave = 0;
	private int timeLeft = 45;
	static ScoreboardManager manager;
	private List<Player> dungeonTeam = new ArrayList<Player>();
	private List<List<String>> mobs = new ArrayList<List<String>>();
	private Party party;
	public Tower tower;
	public Location spawn;
	public float multi;
    public List<LivingEntity> inThisDungeon = new ArrayList<LivingEntity>();
    int scedual;
    Grade grade;
    Dungeon dun;
    String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon" + ChatColor.DARK_GRAY + "]";
    String prefixf = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon " + ChatColor.DARK_GRAY + "|" + ChatColor.DARK_RED + " Floor: " + floor + ChatColor.DARK_GRAY + "]";
	
		
	public DungeonFloor(Tower t, Party p, Dungeon dun, Location spawn, float multi, Grade grade) {
		this.party = p;
		this.tower = t;
		this.dun = dun;
		this.multi = multi;
		this.spawn = spawn;
		this.grade = grade;
		
		for(DungeonPlayer dp : p.getMembers())
		{
			dp.getPlayer().teleport(spawn);
			dungeonTeam.add(dp.getPlayer());
		}
		
		ScoreboardMaker.newObjective();
		for(Player all:dungeonTeam){
			ScoreboardMaker.createScoreboard(all);
			ScoreboardMaker.setScore(all,1);
		}
		floor = 1;
		wave = 0;
		ScoreboardMaker.setDungeonFloor(floor, totalFloors);
		SendMessage(prefix + ChatColor.GREEN + " Good luck!");
		
		scedual = dun.getServer().getScheduler().scheduleSyncRepeatingTask(dun, new Runnable() {
		    @Override  
		    public void run() {
				if(party != null)
					timeLeft -= 5;
					party.sendMsg(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Time left until dungeon close: " + ChatColor.GREEN + timeLeft + " Mins");
					if(timeLeft == 0)
					{
						endDungeon(false);
					}
				}
		}, 0L, 20*60*5L);
		
		Wave mobWave = grade.getWaveForLevel(floor);
		mobs = mobWave.getList();
		setMobsPerFloor(getMobsPerFloor());
		setMobsLeft(getMobsPerFloor());
		nextWave(0);
		
		
	}
	public void nextWave(int w){
		for(String s : getList(w))
		{
			dun.spawnEntitys.SpawnMob(tower.floors.get(floor - 1).cuboID.getSpawnBlock(), this, multi, s);
		}
	}
	public void setTotalFloors(int floors){
		totalFloors = floors;
	}
	public void setMobsPerFloor(int mobs){
		mobsPerFloor = mobs;
	}
	public void setMobsLeft(int left){
		mobsLeft = left;
	}
	public void setLives(int lifes){
		teamLives = lifes;
	}
	public boolean isPlayerInDungeon(Player player){
		for(Player all:dungeonTeam){
			if(player == all){
				return true;
			}
		}
		return false;
	}
	public void endDungeon(Boolean prize){
		for(Player all: dungeonTeam){
				all.teleport(dun.exit);
			}
			ScoreboardMaker.removeScoreboards();
			if(prize)
			{
				prize();
			}
			for(LivingEntity e : inThisDungeon)
			{
				e.remove();
			}
			dun.getServer().getScheduler().cancelTask(scedual);
			tower.Generate(0, 1);
			party.indungeon = false;
			tower.party = null;
			tower.end();
	}
	private void prize() {
		Rewards reward = null;
		if(tower.getPortal().state == 3)
			reward = dun.rewHard;
		else if(tower.getPortal().state == 2)
			reward = dun.rewNormal;
		else
			reward = dun.rewEasy;
		for(Player all: dungeonTeam){
			RewardPlayer(all, reward);
		}
	}
	private void addPoint(Player player) {
		ScoreboardMaker.setScore(player, 1);
	}
	public void minusLife(Player player){
		if(!player.hasPermission("Dungeon.Nolife"))
			teamLives--;
		SendMessage(prefixf + " " + ChatColor.DARK_RED + player.getName() + ChatColor.GRAY + " has died! Lives left: " + ChatColor.RED + teamLives);
		if(teamLives <= 0){
			SendMessage(prefixf + ChatColor.DARK_RED + " Dungeon Over! ");
			endDungeon(false);
		}
		
		
	}
	public void creatureDeath(Entity theCreature, Player player){
		if(inThisDungeon.contains(theCreature)){
			//Particle.playParticle("lavaDrip", theCreature.getLocation(), 0.3f, 0.3f, 300);
			if(player != null)
				addPoint(player);
			mobsLeft--;
			SendMessage(prefixf + ChatColor.GRAY +" Mobs remaining: " + ChatColor.DARK_RED + mobsLeft + ChatColor.GRAY + "/" + ChatColor.DARK_RED + mobsPerFloor);
		}
		if(totalFloors == floor && mobsLeft <= 0){
			SendMessage(prefixf + ChatColor.GREEN + "Dungeon Completed! Congratulations!");
			endDungeon(true);
			dungeonTeam = null;
			return;			
		}
		if(mobsLeft <= 0){
			SendMessage(prefixf + ChatColor.GREEN + " Floor Completed!");
			tower.floors.get(floor - 1).open(); //Open Gate
			floor++;
			ScoreboardMaker.setDungeonFloor(floor, totalFloors);

			Wave mobWave = grade.getWaveForLevel(floor);
			mobs = mobWave.getList();
			setMobsPerFloor(getMobsPerFloor());
			setMobsLeft(getMobsPerFloor());
			wave = 0;
			nextWave(wave);
		}
		if(mobsLeft == (mobsPerFloor-getCurrent(wave))){
			SendMessage(prefix + ChatColor.GREEN + " Wave " + wave + ChatColor.GRAY + " Complete! " + ChatColor.GREEN + "Wave " + (wave+1) + ChatColor.GRAY +  " spawning!");
			wave++;
			nextWave(wave);
		}
	}
	
	public void SendMessage(String msg)
	{
		prefixf = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Dungeon " + ChatColor.DARK_GRAY + "|" + ChatColor.DARK_RED + " Floor: " + floor + ChatColor.DARK_GRAY + "]";
		for(Player all: dungeonTeam){
			all.sendMessage(msg);
		}
	}
	
	public void RewardPlayer(Player p, Rewards reward)
	{
		Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST);
		for(ItemStack stack : reward.getStacks())
			inv.addItem(stack);
		p.openInventory(inv);
	}
	
	public List<String> getList(int i)
	{
		return mobs.get(i);
	}
	
	public int getMobsPerFloor()
	{
		int i = 0;
		for(List<String> l : mobs)
		{
			i += l.size();
		}
		return i;
	}
	public int getCurrent(int s)
	{
		int amount = 0;
		for(int i = 0; i <= s; i++)
		{
			amount += getList(i).size();
		}
		return amount;
	}
	
	
}
