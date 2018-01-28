package com.almundo.app;

/**
 * This class simulates concurrent calls.
 * 
 * It shares the dispatcher class which contains the task queues.
 */
public class CallGeneratorTask implements Runnable {

	/** The dispatcher. */
	Dispatcher dispatcher;
	int id = 0;

	/**
	 * Initializes a new CallGeneratorTask task with an ID
	 *
	 * @param dispatcher => call dispatcher
	 * @param id => call ID
	 */
	CallGeneratorTask(Dispatcher dispatcher, int id) {
		this.dispatcher = dispatcher;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		dispatcher.dispatchCall(new Call(id));
	}
}
