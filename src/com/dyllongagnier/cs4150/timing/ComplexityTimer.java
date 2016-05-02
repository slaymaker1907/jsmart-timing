package com.dyllongagnier.cs4150.timing;

import java.util.ArrayList;

public class ComplexityTimer extends JunkHash
{
	private CodeTimer timer;
	
	public ComplexityTimer()
	{
		this.setTimer(new CodeTimer());
	}
	
	protected void setTimer(CodeTimer timer)
	{
		this.timer = timer;
	}
	
	public CodeTimer getTimer()
	{
		return this.timer;
	}
	
	public Iterable<ComplexityResult> timeCode(ComplexibleCode code, Iterable<Integer> sizeN)
	{
		ArrayList<ComplexityResult> result = new ArrayList<>();
		for(Integer n : sizeN)
			result.add(this.timeCode(code, n));
		return result;
	}
	
	public ComplexityResult timeCode(ComplexibleCode code, int n)
	{
		JunkCode toRun = code.getCode(n);
		ComplexityResult result = new ComplexityResult(this.getTimer().timeCode(toRun), n);
		this.changeHash(this.getTimer().getJunkState());
		return result;
	}
	
	
}
