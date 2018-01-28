package com.almundo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.app.config.*;

/**
 * This class represents an employee independently of his/her role.
 * (OPERATOR/SUPERVISOR/DIRECTOR)
 */
public class CallAnswerTask implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	/** The dispatcher. */
	Dispatcher dispatcher;
	
	/** The employee role. */
	Role role;


	/**
	 * Instantiates a new CallAnswerTask.
	 *
	 * @param dispatcher the dispatcher
	 * @param role the role
	 */
	CallAnswerTask(Dispatcher dispatcher, Role role) {
		this.dispatcher = dispatcher;
		this.role = role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {		
			Long time = Config.asignRandomTimeToTask(); 
			Call task = dispatcher.answerCallByRole(role, time);
			Thread.sleep(time);
			if (task != null)
				log.info("FIN LLAMADA => " + task);
		} catch (InterruptedException ex) {
			log.error(ex.getMessage());
		}
	}

}
