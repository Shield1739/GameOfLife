package com.ts;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class Toolbar extends ToolBar
{
	private MainView mainView;

	public Toolbar(MainView mainView)
	{
		this.mainView = mainView;
		Button draw = new Button("Draw");
		draw.setOnAction(this::handleDraw);
		Button erase = new Button("Erase");
		erase.setOnAction(this::handleErase);
		Button step = new Button("Step");
		step.setOnAction(this::handleStep);

		this.getItems().addAll(draw, erase, step);
	}

	private void handleDraw(ActionEvent actionEvent)
	{
		System.out.println("Draw");
		this.mainView.setDrawMode(1);
	}

	private void handleErase(ActionEvent actionEvent)
	{
		System.out.println("Erase");
		this.mainView.setDrawMode(0);
	}

	private void handleStep(ActionEvent actionEvent)
	{
		System.out.println("Step");
		this.mainView.getSimulation().step();
		this.mainView.draw();
	}
}
