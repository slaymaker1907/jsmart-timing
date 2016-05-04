package com.dyllongagnier.cs4150.timing;

import java.time.Duration;

/**
 * This class, unlike the CodeTimer or FixedSampleSize classes, takes the minimum out of a set of timings. This is because the minimum is least likely to be
 * affected by noise and it is very unlikely that something just naturally runs faster for the same/similar input.
 * 
 * @author Dyllon Gagnier
 */
public class MinTimer extends FixedSampleSize
{
	private Duration minimumTime;
	
	/**
	 * The number of samples to take.
	 * @param sampleSize Samples to take.
	 * @param minimumTime The minimum amount of time to take a sample of.
	 */
	public MinTimer(int sampleSize, Duration minimumTime)
	{
		super(sampleSize);
		this.minimumTime = minimumTime;
	}
	
	/**
	 * Uses a default minimum time of 50 ms.
	 * @param sampleSize
	 */
	public MinTimer(int sampleSize)
	{
		super(sampleSize);
		this.minimumTime = Duration.ofMillis(50);
	}
	
	@Override
	public Duration getTimeToRun()
	{
		return minimumTime;
	}
	
	/**
	 * Returns the minimum instead of the average durations.
	 * @param durations The durations to take the minimum of.
	 */
	@Override
	protected Duration getAverageDuration(Iterable<Duration> durations)
	{
		Duration best = durations.iterator().next();
		for(Duration duration : durations)
		{
			if (duration.compareTo(best) < 0)
				best = duration;
		}
		
		return best;
	}
	
	@Override
	protected int numberOfCollections()
	{
		return 0;
	}
}
