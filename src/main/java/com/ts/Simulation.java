package com.ts;

public class Simulation
{
	public static int DEAD = 0;
	public static int ALIVE = 1;

	int width;
	int height;
	int[][] board;

	public Simulation(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.board = new int[width][height];
	}

	public void printBoard()
	{
		System.out.print("---\n");
		for (int y = 0; y < height; y++)
		{
			String line = "|";
			for (int x = 0; x < width; x++)
			{
				if (this.board[x][y] == DEAD)
				{
					line += ".";
				}
				else
				{
					line += "*";
				}
			}
			line += "|";
			System.out.println(line);
		}
		System.out.print("---\n");
	}

	public void setAlive(int x, int y)
	{
		this.setState(x, y, ALIVE);
	}

	public void setDead(int x, int y)
	{
		this.setState(x, y, DEAD);
	}

	public void setState(int x, int y, int state)
	{
		if (x < 0 || x >= width)
		{
			return;
		}

		if (y < 0 || y >= height)
		{
			return;
		}

		this.board[x][y] = state;
	}

	public int countAliveNeighbours(int x, int y)
	{
		int count = 0;

		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (i == 0 && j == 0)
				{
					continue;
				}

				count += getState(x + i, y + j);
			}
		}

		return count;
	}

	public int getState(int x, int y)
	{
		if (x < 0 || x >= width)
		{
			return DEAD;
		}

		if (y < 0 || y >= height)
		{
			return DEAD;
		}

		return this.board[x][y];
	}

	public void step()
	{
		int[][] newBoard = new int[width][height];

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				int aliveNeighbours = countAliveNeighbours(x, y);

				if (getState(x, y) == ALIVE)
				{
					if (aliveNeighbours < 2)
					{
						newBoard[x][y] = DEAD;
					}
					else if (aliveNeighbours == 2 || aliveNeighbours == 3)
					{
						newBoard[x][y] = ALIVE;
					}
					else
					{
						newBoard[x][y] = DEAD;
					}
				}
				else
				{
					if (aliveNeighbours == 3)
					{
						newBoard[x][y] = ALIVE;
					}
				}
			}
		}

		this.board = newBoard;
	}
}
