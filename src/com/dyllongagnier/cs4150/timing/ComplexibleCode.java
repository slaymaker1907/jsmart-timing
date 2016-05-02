package com.dyllongagnier.cs4150.timing;

public interface ComplexibleCode extends JunkState
{
	public void run(int n);
	public void setup(int n);
	
	public default JunkCode getCode(int n)
	{
		ComplexibleCode self = this;
		return new JunkCode()
		{
			@Override
			public int getJunkState()
			{
				return self.getJunkState();
			}

			@Override
			public void run()
			{
				self.run(n);
			}

			@Override
			public void setup()
			{
				self.setup(n);
			}
		};
	}
}
