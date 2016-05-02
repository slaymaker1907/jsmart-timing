package com.dyllongagnier.cs4150.timing;

import java.time.Duration;
import java.util.ArrayList;

public class FixedSampleSize extends CodeTimer
{
	private int sampleSize;
	
	public FixedSampleSize(int sampleSize)
	{
		this.sampleSize = sampleSize;
	}
	
	@Override
	public boolean isSignificant(ArrayList<Duration> durations)
	{
		return durations.size() >= sampleSize;
	}
}
