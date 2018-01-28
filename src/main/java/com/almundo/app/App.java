package com.almundo.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.app.AppTest;
import com.almundo.app.CallAnswerTask;

/**
 * The Class App.
 */
public class App {
	
	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(App.class);
	// CONSTANTS
	private static final Integer NUM_CALLS_TO_SIMULATE = 10;
	private static final Integer NUM_EMPLOYEES_TO_SIMULATE = 5;
	private static final Integer NUM_SUPERVISORS_TO_SIMULATE = 2;
	private static final Integer NUM_DIRECTORS_TO_SIMULATE = 1;
	private static final Integer NUM_POOL_THREADS = 18;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		
		log.info("Inicio ejemplo llamadas y trabajo concurrente asincronico.");
		// definimos un alcance para cada cola
		Dispatcher dispatcher = new Dispatcher(NUM_EMPLOYEES_TO_SIMULATE, NUM_SUPERVISORS_TO_SIMULATE, NUM_DIRECTORS_TO_SIMULATE);
		ExecutorService executor = Executors.newFixedThreadPool(NUM_POOL_THREADS);
		// Creamos un productor que simula 10 llamados concurrentes
		for (int i = 1 ; i <= NUM_CALLS_TO_SIMULATE; i++) {
			executor.execute(new CallGeneratorTask(dispatcher, i));
		}
		// Agregamos diferentes empleados con diferentes roles.
		for (int e = 0; e < NUM_EMPLOYEES_TO_SIMULATE; e++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		}
		for (int s = 0; s < NUM_SUPERVISORS_TO_SIMULATE; s++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		}
		for (int d = 0; d < NUM_DIRECTORS_TO_SIMULATE; d++) {
			executor.execute(new CallAnswerTask(dispatcher, Role.DIRECTOR));
		}
		// Ejecuta las llamadas en espera (solo empleados)
		int numRemainingCalls = dispatcher.getCommonQueue().size();
		while (numRemainingCalls > 0) {
			executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
			numRemainingCalls--;				
		}
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
		log.info("FIN SIMULACION");
	}

}