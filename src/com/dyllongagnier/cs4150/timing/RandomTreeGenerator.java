package com.dyllongagnier.cs4150.timing;

import java.util.Random;
import java.util.TreeSet;

public class RandomTreeGenerator
{
	private final Random gen;
	
	public RandomTreeGenerator(long seed)
	{
		this.gen = new Random();
	}
	
	private Random getGen()
	{
		return this.gen;
	}

	public TreeSet<Integer> nextTree(int n)
	{
		TreeSet<Integer> result = new TreeSet<>();
		for(int i = 0; i < n; i++)
		{
			result.add(this.getGen().nextInt());
		}
		
		return result;
	}
}
