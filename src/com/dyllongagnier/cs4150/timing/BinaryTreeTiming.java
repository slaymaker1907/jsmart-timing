package com.dyllongagnier.cs4150.timing;

import java.util.TreeSet;

public class BinaryTreeTiming extends JunkHash implements ComplexibleCode
{
	private RandomTreeGenerator gen = new RandomTreeGenerator(8675309);
	private TreeSet<Integer> currentTree = null;
	
	private RandomTreeGenerator getGen()
	{
		return this.gen;
	}

	@Override
	public void run(int N)
	{
		TreeSet<Integer> toSearch = this.getCurrentTree();
		for(Integer toFind : toSearch)
			this.changeHash(toSearch.contains(toFind)?1:0);
	}
	
	private TreeSet<Integer> getCurrentTree()
	{
		return this.currentTree;
	}

	@Override
	public void setup(int n)
	{
		this.currentTree = this.getGen().nextTree(n);
	}
	
	public static void main(String[] args)
	{
		Iterable<Integer> range = new ComplexNGen().getTwoPowRange(10, 20);
		ComplexityTimer timer = new ComplexityTimer();
		BinaryTreeTiming experiment = new BinaryTreeTiming();
		timer.getTimer().warmup(experiment.getCode(1024));
		for(Integer n : range)
		{
			System.out.println(timer.timeCode(experiment, n));
		}
		System.out.println(timer.getJunkState());
	}
}
