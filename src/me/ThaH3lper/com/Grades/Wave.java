package me.ThaH3lper.com.Grades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wave {
	
	public String Name;
	List<List<String>> Mobs = new ArrayList<List<String>>();
	public Wave(List<String> wave, String Name)
	{
		this.Name = Name;
		for(String s : wave)
		{
			List<String> waveMobs = new ArrayList<String>();
			String[] parts = s.split(",");
			for(String str : parts)
			{
				String[] one = str.split(":");
				int amount = Integer.parseInt(one[1]);
				for(int i = 0; i<amount; i++)
				{
					waveMobs.add(one[0]);
				}
			}
			Mobs.add(waveMobs);
		}
	}
	
	public List<List<String>> getList()
	{
		return Mobs;
	}

}
