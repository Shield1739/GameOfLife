package com.ts.gol.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationViewModelTest
{

	private ApplicationViewModel applicationViewModel;

	@BeforeEach
	void setUp()
	{
		applicationViewModel = new ApplicationViewModel(ApplicationState.EDITING);
	}

	@Test
	void setApplicationState_setToNewState()
	{
		TestAppStateListener listener = new TestAppStateListener();
		applicationViewModel.listenToAppState(listener);

		applicationViewModel.setCurrentState(ApplicationState.SIMULATING);

		assertTrue(listener.appStateUpdated);
		assertEquals(ApplicationState.SIMULATING, listener.updatedAppState);

	}

	@Test
	void setApplicationState_setToSameState()
	{
		TestAppStateListener listener = new TestAppStateListener();
		applicationViewModel.listenToAppState(listener);

		applicationViewModel.setCurrentState(ApplicationState.EDITING);

		assertFalse(listener.appStateUpdated);
		assertNull(listener.updatedAppState);
	}

	private static class TestAppStateListener implements SimpleChangeListener<ApplicationState>
	{
		private boolean appStateUpdated = false;
		private ApplicationState updatedAppState = null;

		@Override
		public void valueChanged(ApplicationState newAppState)
		{
			appStateUpdated = true;
			updatedAppState = newAppState;
		}
	}

}