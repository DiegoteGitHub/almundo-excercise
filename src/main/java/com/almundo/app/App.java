package com.almundo.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.app.config.Config;

/**
 * The Class App.
 */
public class App {
	
	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(App.class);
	// CONSTANTS
	private static final Integer NUM_POOL_THREADS = 22;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		
		log.info("Inicio ejemplo llamadas y trabajo concurrente asincronico.");
		// definimos un alcance para cada cola
		Dispatcher dispatcher = new Dispatcher(Config.NUM_OPERATORS_TO_SIMULATE, Config.NUM_SUPERVISORS_TO_SIMULATE, Config.NUM_DIRECTORS_TO_SIMULATE);
		ExecutorService executor = Executors.newFixedThreadPool(NUM_POOL_THREADS);
		// Creamos un generador que simula 10 llamados concurrentes
		for (int i = 0 ; i < Config.NUM_CALLS_TO_SIMULATE; i++) {
			executor.execute(new CallGeneratorTask(dispatcher, i));
		}
		// Agregamos diferentes empleados con diferentes roles.
		for (int e = 0; e < Config.NUM_OPERATORS_TO_SIMULATE; e++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		}
		for (int s = 0; s < Config.NUM_SUPERVISORS_TO_SIMULATE; s++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		}
		for (int d = 0; d < Config.NUM_DIRECTORS_TO_SIMULATE; d++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.DIRECTOR));
		}
		int numRemainingCalls = dispatcher.getCommonQueue().size();
		while (numRemainingCalls > 0) {
			log.info("Remaining calls => " + numRemainingCalls);
			log.info("LAUNCHING NEW OPERATOR ANSWER TASK...");
			executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
			numRemainingCalls--;				
		}
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
		log.info("END SIMULATION");
	}
}