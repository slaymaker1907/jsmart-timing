package com.dyllongagnier.cs3810;

import java.time.Duration;

import com.dyllongagnier.cs4150.timing.CodeTimer;
import com.dyllongagnier.cs4150.timing.MinTimer;

/**
 * This is the main function to compare cache aware algorithms.
 * @author Dyllon Gagnier
 */
public class CacheSum
{
	/**
	 * This timing experiment compares the time it takes to sum elements in an array
	 * following row major order and going against row major order.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Create data to be used throughout.
		RandomArrayGenerator rand = new RandomArrayGenerator();
		CodeTimer timer = new MinTimer(10);
		int bigSize = 8192;
		int smallSize = 100;
		
		// Create a few versions of these objects to be used to warm up the JVM and then later
		// to do the actual experiment.
		SlowSum smallSlow = new SlowSum(rand, smallSize);
		SlowSum bigSlow = new SlowSum(rand, bigSize);
		FastSum smallFast = new FastSum(rand, smallSize);
		FastSum bigFast = new FastSum(rand, bigSize);
		
		// Warm up the JVM and then take measurement.
		timer.warmup(smallFast);
		System.out.println("Starting timing samples.");
		Duration fastTime = timer.timeCode(bigFast);
		System.out.println(fastTime);
		
		timer.warmup(smallSlow);
		Duration slowTime = timer.timeCode(bigSlow);
		System.out.println(slowTime);
		
		// Print out the speedup for the cache aware code.
		System.out.println(1.0 * slowTime.toNanos() / fastTime.toNanos());
	}
}
