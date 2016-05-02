package com.dyllongagnier.cs4150.timing;

public interface JunkState
{
	// This just retrieves whatever junk state is in this object to prevent JIT.
	public int getJunkState();
}
