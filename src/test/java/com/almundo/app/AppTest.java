package com.almundo.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.app.config.Config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	private static final Logger log = LoggerFactory.getLogger(AppTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test
	 */
	public void testApp() {
		assertTrue(true);
	}

	/**
	 * Simulates 10 concurrent calls and 8 OPERATORs (5 OPERATORS, 2 SUPERVISORS
	 * 5 and 1 DIRECTOR). All workers must have at least one task
	 * 
	 * @throws InterruptedException
	 * 
	 */
	public void testDispatch10TaskConcurrency() throws InterruptedException {

		log.info("********** TEST 10 REGULAR CALLS WITH NO EXTRA OPERATORS TO TAKE ON_HOLD CALLS **********.");
		// Define capacity of each queue
		Dispatcher dispatcher = new Dispatcher(Config.NUM_OPERATORS_TO_SIMULATE, Config.NUM_SUPERVISORS_TO_SIMULATE, Config.NUM_DIRECTORS_TO_SIMULATE);
		ExecutorService executor = Executors.newFixedThreadPool(20);
		// Creates 10 call generators
		generateCalls(10, dispatcher, executor);
		// Creates different OPERATORs with different roles
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.DIRECTOR));
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
	}

	/**
	 * In this case 10 concurrent calls but more OPERATORs than calls so they can take ON_HOLD calls
	 * @throws InterruptedException
	 * 
	 */
	public void testManyOPERATORsTakeTaskOnHold() throws InterruptedException {
		log.info("********** MANY OPERATORS TAKING ON_HOLD CALLS **********.");
		// Define capacity of each queue
		Dispatcher dispatcher = new Dispatcher(Config.NUM_OPERATORS_TO_SIMULATE, Config.NUM_SUPERVISORS_TO_SIMULATE, Config.NUM_DIRECTORS_TO_SIMULATE);
		ExecutorService executor = Executors.newFixedThreadPool(24);
		generateCalls(10, dispatcher, executor);
		// Creates different OPERATORs with different roles
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.SUPERVISOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.DIRECTOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.execute(new CallAnswerTask(dispatcher, Role.OPERATOR));
		executor.shutdown();
		executor.awaitTermination(2, TimeUnit.MINUTES);
	}
	
	private void generateCalls(int numCalls, Dispatcher dispatcher, ExecutorService executor) {
		for (int i = 0; i < numCalls; i++) {
			executor.execute(new CallGeneratorTask(dispatcher, i));
		}
	}

}