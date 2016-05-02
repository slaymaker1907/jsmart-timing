package com.dyllongagnier.cs4150.timing;

import java.time.Duration;
import java.time.Instant;

public class TimingLoop extends JunkHash
{	
	private Duration emptyLoopTime;
	
	public TimingLoop()
	{
		this(100_000_000);
	}
	
	public TimingLoop(int iterations)
	{
		this.calibrateLoop(iterations);
	}
	
	public void calibrateLoop(int iterations)
	{
		Runnable emptyLoop = () ->
		{	
			for(int i = 0; i < iterations; i++)
			{
				this.changeHash(i);
			}
		};
		
		this.emptyLoopTime = this.timeCode(emptyLoop).dividedBy(iterations);
	}
	
	private Duration getEmptyTime(int iterations)
	{
		return this.emptyLoopTime.multipliedBy(iterations);
	}
	
	// Finds the amount of time to run the toRun once by running it by the number of iterations specified.
	public Duration forLoop(Runnable toRun, int iterations)
	{
		Runnable loop = () ->
		{
			for(int i = 0; i < iterations; i++)
			{
				toRun.run();
				this.changeHash(i);
			}
		};
		
		return this.timeCode(loop).minus(this.getEmptyTime(iterations));
	}
	
	private Duration timeCode(Runnable toRun)
	{
		Instant start = Instant.now();
		toRun.run();
		Instant stop = Instant.now();
		return Duration.between(start, stop);
	}
}
