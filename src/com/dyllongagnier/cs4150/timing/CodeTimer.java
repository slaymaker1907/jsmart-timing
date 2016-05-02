package com.dyllongagnier.cs4150.timing;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * This class provides a way to time code in a meaningful way by accounting for as many confounding
 * variables as possible such as the garbage collector, the just in time compiler, and other
 * random noise.
 * 
 * To use this class, first implement the JunkCode interface (this allows setup to be called before
 * each timing sample is taken and has a getJunk method that is used to prevent almost useless
 * loops from being compiled away).
 * 
 * Once the JunkCode interface is implemented, call warmup which will run the input code many, many
 * times to allow the code being timed to be compiled by the JIT (the code that is warmed up need not
 * be the same object as the one to eventually be timed, but it should be of the same class; this
 * allows for JITing the code with small data so that warmup is fast).
 * 
 * Finally, call timeCode with whatever code you want to have timed. This class tries to ensure
 * that the time returned is statistically significant to a limited degree.
 * 
 * Many of the methods in this class are made protected with the intention that this class may
 * be customized to suit a wide variety of timing needs.
 * 
 * @author Dyllon Gagnier
 *
 */
public class CodeTimer extends JunkHash
{
	/**
	 * The class used to loop things many times.
	 */
	private TimingLoop loop = new TimingLoop();
	
	/**
	 * This returns the loop used to loop things many times.
	 * @return The loop used to loop things many times.
	 */
	protected TimingLoop getLoop()
	{
		return loop;
	}
	
	/**
	 * This method returns the number of times garbage collection has occured.  Garbage collection is usually parallel,
	 * so for precision, -XX:+UseSerialGC should be used to make sure stop the world happens.
	 * @return The number of times GC occured.
	 */
	protected int numberOfCollections()
	{
		int result = 0;
		for(GarbageCollectorMXBean collector : ManagementFactory.getGarbageCollectorMXBeans())
		{
			result += collector.getCollectionCount();
		}
		return result;
	}
	
	/**
	 * Runs the input function and returns true if garbage collection occured
	 * while running the input function.
	 * @param toRun The function to run.
	 * @return True if GC occured.
	 */
	protected boolean detectGarbageCollection(Runnable toRun)
	{
		int collCount = numberOfCollections();
		toRun.run();
		return numberOfCollections() != collCount;
	}
	
	/**
	 * This method runs a particular function until the time passed running the function
	 * exceeds the input time.
	 * @param toRun The function to repeat.
	 * @param timeToRun The time to run the function for.
	 * @return The results of timing the code.
	 */
	private TimedIterations runForTime(Runnable toRun, Duration timeToRun)
	{
		Duration timeRun = Duration.ofNanos(0);
		int itersToRun = 1;
		while(timeRun.compareTo(timeToRun) < 0)
		{
			itersToRun *= 2;
			timeRun = this.getLoop().forLoop(toRun, itersToRun);
		}
		
		return new TimedIterations(timeRun, itersToRun);
	}
	
	/**
	 * This method runs setup of the junk code once and then runs
	 * the main code the input times and ensures that the code in the main
	 * part of toRun is compiled.
	 * @param toRun The code to compile.
	 * @param iterations The number of times to run the code.
	 */
	public void warmup(JunkCode toRun, int iterations)
	{
		toRun.setup();
		this.getLoop().forLoop(toRun, iterations);
		this.changeHash(toRun.getJunkState());
	}
	
	/**
	 * This is identical to the warmup code taking an int except that it
	 * runs the code 50,000 times.
	 * @param toRun The code to run.
	 */
	public void warmup(JunkCode toRun)
	{
		this.warmup(toRun, 50_000);
	}
	
	/**
	 * Returns the minimum timing sample size (to ensure that accuracy of the clock doesn't
	 * contaminate measurements).
	 * @return The minimum time to run.
	 */
	protected Duration getTimeToRun()
	{
		return Duration.ofSeconds(2);
	}
	
	/**
	 * Runs code for a minimal amount of time and makes sure that garbage collection does not
	 * occur when running the code and then returns the time to run the input code once.
	 * 
	 * @param toTime The code to time.
	 * @return The time to run the code.
	 */
	private Duration getTimingSample(JunkCode toTime)
	{		
		MutableObject<TimedIterations> actualTime = new MutableObject<>();
		Runnable garbageRun = () ->
		{
			actualTime.setValue(this.runForTime(toTime, this.getTimeToRun()));
		};
		
		toTime.setup();
		while(this.detectGarbageCollection(garbageRun))
		{
			toTime.setup();
			this.changeHash(toTime.getJunkState());
		}
		return actualTime.getValue().getTimePerIteration();
	}
	
	/**
	 * Times the input code and returns the time to run it.
	 * @param toTime The code to time.
	 * @return The time to run code.
	 */
	public Duration timeCode(JunkCode toTime)
	{
		ArrayList<Duration> sampleSet = new ArrayList<>();
		while(!this.isSignificant(sampleSet))
			sampleSet.add(this.getTimingSample(toTime));
		return this.getAverageDuration(sampleSet);
	}
	
	/**
	 * Returns the average duration in an iterable of durations.
	 * @param durations The input durations to measure.
	 * @return The average duration of of the input durations.
	 */
	protected Duration getAverageDuration(Iterable<Duration> durations)
	{
		Duration sum = Duration.ZERO;
		int count = 0;
		for(Duration duration : durations)
		{
			sum = sum.plus(duration);
			count++;
		}
		
		return sum.dividedBy(count);
	}
	
	/**
	 * Returns true if the input values are statistically significant.
	 * @param values The values to determine if significant.
	 * @return True if durations are statistically significant.
	 */
	protected boolean isSignificant(ArrayList<Duration> values)
	{
		SummaryStatistics sample = this.toStats(values);
		if (sample.getN() < 10)
			return false;
		double critVal = this.getCritValue(sample);
		return critVal < this.getMeanDelta() * sample.getMean();
	}
	
	/**
	 * Gets how close the measured average must be to the real mean time.
	 * @return
	 */
	protected double getMeanDelta()
	{
		return 0.01;
	}
	
	/**
	 * Returns the critical value in reference to statistical significance for the input 
	 * data sample.
	 * @param sample The data to determine the critical value of.
	 * @return The critical value of the input.
	 */
	protected double getCritValue(SummaryStatistics sample)
	{
		TDistribution distro = new TDistribution(sample.getN() - 1);
		return distro.inverseCumulativeProbability(1.0 - this.getAlpha() / 2) * sample.getStandardDeviation() / Math.sqrt(sample.getN());
	}
	
	/**
	 * Gets the T test to use for this class.
	 * @return The T test for this class.
	 */
	protected TTest getTTest()
	{
		return new TTest();
	}
	
	/**
	 * Computes the average of an array of doubles.
	 * @param values The data.
	 * @return The average of the data.
	 */
	protected double computeAverage(double[] values)
	{
		double sum = 0;
		for(double value : values)
			sum += value;
		return sum / values.length;
	}
	
	/**
	 * Finds the statistics for Apache from an iterable of durations.
	 * @param values The values to take the statistics from.
	 * @return Statistics about the input values.
	 */
	private SummaryStatistics toStats(Iterable<Duration> values)
	{
		SummaryStatistics result = new SummaryStatistics();
		for(Duration value : values)
			result.addValue(this.durationToDouble(value));
		return result;
	}
	
	/**
	 * Converts a duration to a double
	 * @param value The value to get the duration of.
	 * @return The nanoseconds of the duration.
	 */
	protected double durationToDouble(Duration value)
	{
		return value.toNanos();
	}
	
	/**
	 * Gets the alpha value for statistical significance.
	 * @return The alpha of significance.
	 */
	protected double getAlpha()
	{
		return 0.05;
	}
}
