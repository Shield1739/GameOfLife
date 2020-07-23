package com.ts.gol;

import com.ts.gol.model.CellState;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class Toolbar extends ToolBar
{
	private MainView mainView;

	private Simulator simulator;

	public Toolbar(MainView mainView)
	{
		this.mainView = mainView;
		Button draw = new Button("Draw");
		draw.setOnAction(this::handleDraw);
		Button erase = new Button("Erase");
		erase.setOnAction(this::handleErase);
		Button reset = new Button("Reset");
		reset.setOnAction(this::handleReset);

		Button step = new Button("Step");
		step.setOnAction(this::handleStep);
		Button start = new Button("Start");
		start.setOnAction(this::handleStart);
		Button stop = new Button("Stop");
		stop.setOnAction(this::handleStop);

		this.getItems().addAll(draw, erase, reset, step, start, stop);
	}

	/**
	 * Button Handlers
	 */

	private void handleDraw(ActionEvent actionEvent)
	{
		this.mainView.setDrawMode(CellState.ALIVE);
	}

	private void handleErase(ActionEvent actionEvent)
	{
		this.mainView.setDrawMode(CellState.DEAD);
	}

	private void handleReset(ActionEvent actionEvent)
	{
		this.mainView.setApplicationState(MainView.EDITING);
		this.simulator = null;
		this.mainView.draw();
	}

	private void handleStep(ActionEvent actionEvent)
	{
		switchToSimulatingState();

		this.mainView.getSimulation().step();
		this.mainView.draw();
	}

	private void handleStart(ActionEvent actionEvent)
	{
		switchToSimulatingState();
		this.simulator.start();
	}

	/**
	 * Other methods
	 */

	private void switchToSimulatingState()
	{
		if (this.mainView.getApplicationState() == MainView.EDITING)
		{
			this.mainView.setApplicationState(MainView.SIMULATING);
			this.simulator = new Simulator(mainView, this.mainView.getSimulation());
		}
	}

	private void handleStop(ActionEvent actionEvent)
	{
		this.simulator.stop();
	}
}
