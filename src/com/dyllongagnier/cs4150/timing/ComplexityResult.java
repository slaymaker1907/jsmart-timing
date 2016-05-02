package com.dyllongagnier.cs4150.timing;

import java.time.Duration;

import javax.json.Json;

public class ComplexityResult
{
	private int n;
	private Duration time;
	
	public ComplexityResult(Duration time, int n)
	{
		this.setTime(time);
		this.setN(n);
	}
	
	public Duration getTime()
	{
		return this.time;
	}
	
	public int getN()
	{
		return this.n;
	}
	
	private void setTime(Duration time)
	{
		if (time == null)
			throw new NullPointerException();
		this.time = time;
	}
	
	private void setN(int n)
	{
		if (n <= 0)
			throw new IllegalArgumentException();
		this.n = n;
	}
	
	@Override
	public String toString()
	{
		return Json.createObjectBuilder().add("n", this.getN()).add("time", time.toNanos() / 1000.0 / 1000.0).build().toString();
	}
}
