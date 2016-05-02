package com.dyllongagnier.cs4150.timing;

import java.time.Duration;

public class TimedIterations
{
	private int iterations;
	private Duration time;
	
	public TimedIterations(Duration time, int iterations)
	{
		this.setTime(time);
		this.setIterations(iterations);
	}
	
	public int getIterations()
	{
		return this.iterations;
	}
	
	public Duration getTime()
	{
		return this.time;
	}
	
	private void setIterations(int iterations)
	{
		this.iterations = iterations;
	}
	
	private void setTime(Duration time)
	{
		if (time == null)
			throw new NullPointerException();
		this.time = time;
	}
	
	public Duration getTimePerIteration()
	{
		return this.getTime().dividedBy(this.getIterations());
	}
}
