package com.dyllongagnier.cs3810;

/**
 * This class implements a sum function for 2D arrays of ints that exhibits extremely poor
 * cache performance.
 * @author Dyllon Gagnier
 */
public class SlowSum extends FastSum
{
	/**
	 * This initializes the slow sum with a particular generator of arrays and a size
	 * to make the arrays (NxN arrays).
	 * @param gen The generator to use.
	 * @param size The size of the arrays to make (NxN).
	 */
	public SlowSum(RandomArrayGenerator gen, int size)
	{
		super(gen, size);
	}
	
	
	@Override
	public void run()
	{
		int rows = current.rows();
		int cols = current.cols();
		for(int col = 0; col < cols; col++)
			for(int row = 0; row < rows; row++)
				sum += current.getInt(row, col);
	}
}
