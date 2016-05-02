package com.dyllongagnier.cs3810;

import com.dyllongagnier.cs4150.timing.JunkCode;

/**
 * 
 * @author Dyllon Gagnier
 *
 */
public class FastSum implements JunkCode
{
	protected int sum = 0;
	private RandomArrayGenerator gen;
	protected IntArray2D current;
	private int size;
	
	public FastSum(RandomArrayGenerator gen, int size)
	{
		this.gen = gen;
		this.size = size;
	}
	
	@Override
	public int getJunkState()
	{
		return sum;
	}

	@Override
	public void run()
	{
		int rows = current.rows();
		int cols = current.cols();
		for(int row = 0; row < rows; row++)
			for(int col = 0; col < cols; col++)
				sum += current.getInt(row, col);
	}

	@Override
	public void setup()
	{
		current = gen.getRandomIntArray(size);
	}
}
