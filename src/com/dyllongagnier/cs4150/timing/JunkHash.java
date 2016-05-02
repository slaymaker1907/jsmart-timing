package com.dyllongagnier.cs4150.timing;

public class JunkHash implements JunkState
{
	private int nonDetHash;
	
	public int changeHash(int toAdd)
	{
		this.nonDetHash += toAdd;
		return this.nonDetHash;
	}
	
	@Override
	public int getJunkState()
	{
		return this.nonDetHash;
	}
}
