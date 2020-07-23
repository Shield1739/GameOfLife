package com.ts.gol;

import com.ts.gol.model.Board;
import com.ts.gol.model.BoundedBoard;
import com.ts.gol.model.CellState;
import com.ts.gol.model.StandardRule;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox
{
	public static final int EDITING = 0;
	public static final int SIMULATING = 1;

	private Canvas canvas;

	private Toolbar toolbar;
	private InfoBar infoBar;

	private Affine affine;

	private Simulation simulation;
	private Board initialBoard;

	private CellState drawMode = CellState.ALIVE;

	private int applicationState = EDITING;

	public MainView()
	{
		this.canvas = new Canvas(400, 400);
		this.canvas.setOnMousePressed(this::handleDraw);
		this.canvas.setOnMouseDragged(this::handleDraw);
		this.canvas.setOnMouseMoved(this::handleMoved);

		this.setOnKeyPressed(this::onKeyPressed);

		this.toolbar = new Toolbar(this);
		this.infoBar = new InfoBar();
		this.infoBar.setDrawMode(this.drawMode);
		this.infoBar.setCursorPosition(0, 0);

		Pane spacer = new Pane();
		spacer.setMinSize(0, 0);
		spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		VBox.setVgrow(spacer, Priority.ALWAYS);

		this.getChildren().addAll(toolbar, this.canvas, spacer, this.infoBar);

		this.affine = new Affine();
		this.affine.appendScale(400 / 10f, 400 / 10f);

		this.initialBoard = new BoundedBoard(10, 10);
	}

	/**
	 * Handlers
	 */

	private void handleMoved(MouseEvent mouseEvent)
	{
		Point2D simCoords = getSimulationCoordinates(mouseEvent);

		this.infoBar.setCursorPosition((int) simCoords.getX(), (int) simCoords.getY());
	}

	private void onKeyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getCode() == KeyCode.D)
		{
			this.drawMode = CellState.ALIVE;
		}
		else if (keyEvent.getCode() == KeyCode.E)
		{
			this.drawMode = CellState.DEAD;
		}
	}

	private void handleDraw(MouseEvent mouseEvent)
	{
		if (applicationState == SIMULATING)
		{
			return;
		}

		Point2D simCoords = getSimulationCoordinates(mouseEvent);

		this.initialBoard.setState((int) simCoords.getX(), (int) simCoords.getY(), drawMode);
		draw();
	}

	/**
	 * Draw Methods
	 */

	public void draw()
	{
		GraphicsContext g = this.canvas.getGraphicsContext2D();
		g.setTransform(affine);

		g.setFill(Color.LIGHTGRAY);
		g.fillRect(0, 0, 400, 400);

		if (this.applicationState == EDITING)
		{
			drawSimulation(this.initialBoard);
		}
		else
		{
			drawSimulation(this.simulation.getBoard());
		}

		g.setStroke(Color.GRAY);
		g.setLineWidth(0.05);

		for (int x = 0; x <= initialBoard.getWidth(); x++)
		{
			g.strokeLine(x, 0, x, 10);
		}

		for (int y = 0; y <= initialBoard.getHeight(); y++)
		{
			g.strokeLine(0, y, 10, y);
		}
	}

	private void drawSimulation(Board boardToDraw)
	{
		GraphicsContext g = this.canvas.getGraphicsContext2D();
		g.setFill(Color.BLACK);

		for (int x = 0; x < boardToDraw.getWidth(); x++)
		{
			for (int y = 0; y < boardToDraw.getHeight(); y++)
			{
				if (boardToDraw.getState(x, y) == CellState.ALIVE)
				{
					g.fillRect(x, y, 1, 1);
				}
			}
		}
	}

	/**
	 * Setters
	 */

	public void setDrawMode(CellState newDrawMode)
	{
		this.drawMode = newDrawMode;
		this.infoBar.setDrawMode(newDrawMode);
	}

	public void setApplicationState(int applicationState)
	{
		if (this.applicationState == applicationState)
		{
			return;
		}

		if (applicationState == SIMULATING)
		{
			this.simulation = new Simulation(this.initialBoard, new StandardRule());
		}

		this.applicationState = applicationState;
	}

	/**
	 * Getters
	 */

	private Point2D getSimulationCoordinates(MouseEvent mouseEvent)
	{
		double mouseX = mouseEvent.getX();
		double mouseY = mouseEvent.getY();

		try
		{
			return this.affine.inverseTransform(mouseX, mouseY);
		}
		catch (NonInvertibleTransformException e)
		{
			throw new RuntimeException("Non invertible transform");
		}
	}

	public Simulation getSimulation()
	{
		return simulation;
	}

	public int getApplicationState()
	{
		return applicationState;
	}
}