package com.ts.gol;

import com.ts.gol.model.Board;
import com.ts.gol.model.CellState;
import com.ts.gol.model.SimulationRule;

public class Simulation
{
	private Board simulationBoard;
	private SimulationRule simulationRule;

	public Simulation(Board simulationBoard, SimulationRule simulationRule)
	{
		this.simulationBoard = simulationBoard;
		this.simulationRule = simulationRule;
	}

	public void step()
	{
		Board nextState = this.simulationBoard.copy();

		for (int x = 0; x < this.simulationBoard.getWidth(); x++)
		{
			for (int y = 0; y < this.simulationBoard.getHeight(); y++)
			{
				CellState newState = this.simulationRule.getNextState(x, y, this.simulationBoard);
				nextState.setState(x, y, newState);
			}
		}

		this.simulationBoard = nextState;
	}

	public Board getBoard()
	{
		return simulationBoard;
	}
}
