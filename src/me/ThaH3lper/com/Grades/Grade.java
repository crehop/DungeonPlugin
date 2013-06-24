package me.ThaH3lper.com.Grades;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.ThaH3lper.com.Dungeon;

public class Grade {
	
	public float boost;
	public int levels;
	public int lives;
	Dungeon dun;
	public List<String> waves= new ArrayList<String>();
	Random r = new Random();
	
	public Grade(Float boost, int levels, List<String> waves, int lives, Dungeon dun)
	{
		this.dun = dun;
		this.boost = boost;
		this.levels = levels;
		this.waves = waves;
		this.lives = lives;
	}
	
	public Wave GetWave(String name)
	{
		for(Wave w : dun.waveList)
		{
			if(w.Name.equals(name))
				return w;				
		}
		return null;
	}
	
	public Wave getWaveForLevel(int lvl)
	{
		String[] parts = waves.get(lvl - 1).split(",");
		String s = parts[r.nextInt(parts.length)];
		return GetWave(s);		
	}

}
