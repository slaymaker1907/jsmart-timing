package com.dyllongagnier.cs3810;

/**
 * This class is a 2D array of integers that is designed to exhibit good performance by
 * backing the data structure with a 1D array layed out in row major order.
 * @author Dyllon Gagnier
 */
public class IntArray2D
{
	/**
	 * The underlying array.
	 */
	private int[] data;
	
	/**
	 * The number of columns this array has.
	 */
	private int cols;
	
	/**
	 * Creates a new 2D array of specified size.
	 * @param rows Number of columns.
	 * @param cols Number of rows.
	 */
	public IntArray2D(int rows, int cols)
	{
		this.data = new int[rows * cols];
		this.cols = cols;
	}
	
	/**
	 * Maps a row and column to an index of the underlying array using a row
	 * major layout.
	 * @param row The row to access.
	 * @param col The col to access.
	 * @return A column to the underlying array.
	 */
	private int getIndex(int row, int col)
	{
		return row * cols + col;
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public int getInt(int row, int col)
	{
		return data[getIndex(row, col)];
	}
	
	public int setInt(int row, int col, int data)
	{
		return this.data[getIndex(row, col)] = data;
	}
	
	public int plusEqual(int row, int col, int toAdd)
	{
		return this.data[getIndex(row, col)] += toAdd;
	}
	
	public int size()
	{
		return data.length;
	}
	
	public int rows()
	{
		return data.length / cols;
	}
	
	public int cols()
	{
		return cols;
	}
}
