package com.ts.gol;

import com.ts.gol.viewmodel.ApplicationState;
import com.ts.gol.viewmodel.ApplicationViewModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 * Game of Life Simulator
 * v0.3
 */
public class App extends Application
{

	@Override
	public void start(Stage stage)
	{
		ApplicationViewModel applicationViewModel = new ApplicationViewModel(ApplicationState.EDITING);

		MainView mainView = new MainView(applicationViewModel);
		Scene scene = new Scene(mainView, 640, 480);
		stage.setScene(scene);
		stage.show();

		mainView.draw();
	}

	public static void main(String[] args)
	{
		launch();
	}

}