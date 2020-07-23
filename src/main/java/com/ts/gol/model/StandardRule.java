package com.ts.gol.model;

public class StandardRule implements SimulationRule
{
	@Override
	public CellState getNextState(int x, int y, Board board)
	{
		int aliveNeighbours = countAliveNeighbours(x, y, board);

		if (board.getState(x, y) == CellState.ALIVE)
		{
			if (aliveNeighbours < 2)
			{
				return CellState.DEAD;
			}
			else if (aliveNeighbours == 2 || aliveNeighbours == 3)
			{
				return CellState.ALIVE;
			}
			else
			{
				return CellState.DEAD;
			}
		}
		else
		{
			if (aliveNeighbours == 3)
			{
				return CellState.ALIVE;
			}
		}

		return CellState.DEAD;
	}

	public int countAliveNeighbours(int x, int y, Board board)
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

				count += countCell(x + i, y + j, board);
			}
		}

		return count;
	}

	private int countCell(int x, int y, Board board)
	{
		if (board.getState(x, y) == CellState.ALIVE)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
