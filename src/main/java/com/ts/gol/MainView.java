package com.ts.gol;

import com.ts.gol.model.Board;
import com.ts.gol.model.BoundedBoard;
import com.ts.gol.model.CellState;
import com.ts.gol.model.StandardRule;
import com.ts.gol.viewmodel.ApplicationState;
import com.ts.gol.viewmodel.ApplicationViewModel;
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
	private Canvas canvas;

	private Toolbar toolbar;
	private InfoBar infoBar;

	private Affine affine;

	private Simulation simulation;
	private Board initialBoard;

	private CellState drawMode = CellState.ALIVE;

	private ApplicationViewModel applicationViewModel;

	private boolean isDrawingEnabled = true;
	private boolean drawInitialBoard = true;

	public MainView(ApplicationViewModel applicationViewModel)
	{
		this.applicationViewModel = applicationViewModel;
		this.applicationViewModel.listenToAppState(this::onApplicationStateChanged);

		this.canvas = new Canvas(400, 400);
		this.canvas.setOnMousePressed(this::handleDraw);
		this.canvas.setOnMouseDragged(this::handleDraw);
		this.canvas.setOnMouseMoved(this::handleMoved);

		this.setOnKeyPressed(this::onKeyPressed);

		this.toolbar = new Toolbar(this, applicationViewModel);
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

	private void onApplicationStateChanged(ApplicationState state)
	{
		if (state == ApplicationState.EDITING)
		{
			this.isDrawingEnabled = true;
			this.drawInitialBoard = true;
		}
		else if (state == ApplicationState.SIMULATING)
		{
			this.isDrawingEnabled = false;
			this.drawInitialBoard = false;
			this.simulation = new Simulation(this.initialBoard, new StandardRule());
		}
		else
		{
			throw new IllegalArgumentException("Unsupported ApplicationState " + state.name());
		}
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
		if (!isDrawingEnabled)
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

		if (drawInitialBoard)
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
}