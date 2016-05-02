package com.dyllongagnier.cs3810;

import java.util.Random;

public class RandomArrayGenerator
{
	private Random rand = new Random();
	
	// Makes an N by N array.
	public IntArray2D getRandomIntArray(int N)
	{
		IntArray2D result = new IntArray2D(N, N);
		for(int row = 0; row < N; row++)
			for(int col = 0; col < N; col++)
				result.setInt(row, col, rand.nextInt());
		
		return result;
	}
}
