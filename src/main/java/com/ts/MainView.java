package com.ts;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox
{
	private Button stepButton;
	private Canvas canvas;

	private Affine affine;

	private Simulation simulation;

	private int drawMode = 1;

	public MainView()
	{
		stepButton = new Button("step");
		this.stepButton.setOnAction(actionEvent ->
			{
				simulation.step();
				draw();
			}
		);
		this.canvas = new Canvas(400, 400);
		this.canvas.setOnMousePressed(this::handleDraw);
		this.canvas.setOnMouseDragged(this::handleDraw);

		this.setOnKeyPressed(this::onKeyPressed);

		this.getChildren().addAll(this.stepButton, this.canvas);

		this.affine = new Affine();
		this.affine.appendScale(400 / 10f, 400 / 10f);

		this.simulation = new Simulation(10, 10);
	}

	private void onKeyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getCode() == KeyCode.D)
		{
			this.drawMode = 1;
		}
		else if (keyEvent.getCode() == KeyCode.E)
		{
			this.drawMode = 0;
		}
	}

	private void handleDraw(MouseEvent mouseEvent)
	{
		double mouseX = mouseEvent.getX();
		double mouseY = mouseEvent.getY();

		try
		{
			Point2D simCoords = this.affine.inverseTransform(mouseX, mouseY);

			int simX = (int) simCoords.getX();
			int simY = (int) simCoords.getY();

			System.out.println(simX + ", " + simY);

			this.simulation.setState(simX, simY, drawMode);
			draw();
		}
		catch (NonInvertibleTransformException e)
		{
			e.printStackTrace();
		}
	}

	public void draw()
	{
		GraphicsContext g = this.canvas.getGraphicsContext2D();
		g.setTransform(affine);

		g.setFill(Color.LIGHTGRAY);
		g.fillRect(0, 0, 400, 400);

		g.setFill(Color.BLACK);

		for (int x = 0; x < this.simulation.width; x++)
		{
			for (int y = 0; y < this.simulation.height; y++)
			{
				if (this.simulation.getState(x, y) == 1)
				{
					g.fillRect(x, y, 1, 1);
				}
			}
		}

		g.setStroke(Color.GRAY);
		g.setLineWidth(0.05);

		for (int x = 0; x <= this.simulation.width; x++)
		{
			g.strokeLine(x, 0, x, 10);
		}

		for (int y = 0; y <= this.simulation.height; y++)
		{
			g.strokeLine(0, y, 10, y);
		}
	}
}
