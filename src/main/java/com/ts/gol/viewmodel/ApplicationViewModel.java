package com.ts.gol.viewmodel;

import java.util.LinkedList;
import java.util.List;

public class ApplicationViewModel
{
	private ApplicationState currentState;
	private List<SimpleChangeListener<ApplicationState>> appStateListeners;

	public ApplicationViewModel(ApplicationState currentState)
	{
		this.currentState = currentState;
		appStateListeners = new LinkedList<>();
	}

	public void listenToAppState(SimpleChangeListener<ApplicationState> listener)
	{
		this.appStateListeners.add(listener);
	}

	public void setCurrentState(ApplicationState newState)
	{
		if (this.currentState != newState)
		{
			this.currentState = newState;
			notifyAppStateListeners();
		}
	}

	private void notifyAppStateListeners()
	{
		for (SimpleChangeListener<ApplicationState> appStateListener : appStateListeners)
		{
			appStateListener.valueChanged(this.currentState);
		}
	}
}