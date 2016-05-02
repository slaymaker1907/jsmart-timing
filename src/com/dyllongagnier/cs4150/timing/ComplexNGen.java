package com.dyllongagnier.cs4150.timing;

import java.util.ArrayList;

public class ComplexNGen
{
	public Iterable<Integer> getDoublingRange(int start, int stop)
	{
		ArrayList<Integer> range = new ArrayList<>();
		int current = start;
		while(current <= stop)
		{
			range.add(current);
			current *= 2;
		}
		
		return range;
	}
	
	public Iterable<Integer> getTwoPowRange(int expStart, int expStop)
	{
		Iterable<Integer> range = this.getDoublingRange(this.pow(2, expStart), this.pow(2, expStop));
		return range;
	}
	
	protected int pow(int base, int exp)
	{
		return (int)Math.pow(base, exp);
	}
}
