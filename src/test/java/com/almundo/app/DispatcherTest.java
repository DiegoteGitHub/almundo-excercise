package com.almundo.app;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.app.config.Config;

/**
 * The Class DispatcherTest.
 * 
 */
public class DispatcherTest {

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(DispatcherTest.class);

	Dispatcher dispatcher = new Dispatcher(Config.NUM_OPERATORS_TO_SIMULATE, Config.NUM_SUPERVISORS_TO_SIMULATE, Config.NUM_DIRECTORS_TO_SIMULATE);	
	Call task1 = new Call(1);
	Call task2 = new Call(2);
	Call task3 = new Call(3);
	Call task4 = new Call(4);
	Call task5 = new Call(5);
	Call task6 = new Call(6);
	Call task7 = new Call(7);
	Call task8 = new Call(8);
	Call task9 = new Call(9);
	Call task10 = new Call(10);


	/**
	 * Test the task priority assignment. In this case OPERATORS queue is empty 
	 * => the task should be assigned to that queue.
	 */
	@Test
	public void dispatchCall2OperatorQueue() {
		log.info("STARTING => dispatchCall2OperatorQueue TEST");
		// Empty queue to ensure size
		dispatcher.getOperatorQueue().clear();
		dispatcher.dispatchCall(task1);
		Assert.assertEquals(1, dispatcher.getOperatorQueue().size());
		log.info("ENDING => dispatchCall2OperatorQueue TEST");
	}

	/**
	 * In this case OPERATORS queue is full => next task should be assigned to SUPERVISORS queue
	 */
	@Test
	public void dispatchCall2SupervisorQueue() {
		// Empty queue to ensure size
		log.info("STARTING => dispatchCall2SupervisorQueue TEST");
		dispatcher.getSupervisorQueue().clear();
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		// All operators busy 
		dispatcher.dispatchCall(task6);
		Assert.assertEquals(1, dispatcher.getSupervisorQueue().size());
		log.info("ENDING => dispatchCall2SupervisorQueue TEST");
	}

	/**
	 * In this case both OPERATORS and SUPERVISORS queues are full so the next
	 * task should be assigned to DIRECTORS queue
	 */
	@Test
	public void dispatchCall2DirectorQueue() {
		log.info("STARTING => dispatchCall2DirectorQueue TEST");
		// Empty queues to ensure size
		dispatcher.getOperatorQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.getDirectorQueue().clear();	
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		// All operators are busy
		dispatcher.dispatchCall(task6);
		dispatcher.dispatchCall(task7);
		dispatcher.dispatchCall(task8);
		// All supervisors are busy
		dispatcher.dispatchCall(task9);
		Assert.assertEquals(1, dispatcher.getDirectorQueue().size());
		log.info("ENDING => dispatchCall2DirectorQueue TEST");
	}

	/**
	 * We test how each thread are consuming tasks (calls) based on it's role, in this
	 * case OPERATOR and if it's queue is empty it checks the ON HOLD queue (common queue)
	 */
	@Test
	public void testAnswerCallByRoleOperator() {
		log.info("STARTING => testAnswerCallByRoleOperator TEST");
		// Empty operators call queue.
		dispatcher.getOperatorQueue().clear();
		// Assign a call to operators queue
		dispatcher.dispatchCall(task1);
		Role role = Role.OPERATOR;
		// This parameter simulates the call duration.
		Long time = 5000L;
		// Verify that the call is answered
		Assert.assertEquals(1, dispatcher.getOperatorQueue().size());
		dispatcher.answerCallByRole(role, time);
		Assert.assertEquals(0, dispatcher.getOperatorQueue().size());
		log.info("ENDING => testAnswerCallByRoleOperator TEST");
	}

	/**
	 * We test how each thread are consuming tasks (calls) based on it's role, in this
	 * case SUPERVISOR and if it's queue is empty it checks the ON HOLD queue (common queue)
	 */
	@Test
	public void testAnswerCallByRoleSupervisor() {
		log.info("STARTING => testAnswerCallByRoleSupervisor TEST");
		// Empty operators and supervisors queue
		dispatcher.getOperatorQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		// All operators are busy
		dispatcher.dispatchCall(task6);
		// All supervisors are busy
		Role role = Role.SUPERVISOR;
		// Simulate call duration
		Long time = 5000L;
		// Verify that the call is answered
		Assert.assertEquals(1, dispatcher.getSupervisorQueue().size());
		dispatcher.answerCallByRole(role, time);
		Assert.assertEquals(0, dispatcher.getSupervisorQueue().size());
		log.info("ENDING => testAnswerCallByRoleSupervisor TEST");
	}

	/**
	 * We test how each thread are consuming tasks (calls) based on it's role, in this
	 * case DIRECTOR and if it's queue is empty it checks the ON HOLD queue (common queue)
	 */
	@Test
	public void testAnswerCallByRoleDirector() {
		log.info("STARTING => testAnswerCallByRoleDirector TEST");
		// Empty all roles queues
		dispatcher.getOperatorQueue().clear();
		dispatcher.getSupervisorQueue().clear();
		dispatcher.getDirectorQueue().clear();	
		dispatcher.dispatchCall(task1);
		dispatcher.dispatchCall(task2);
		dispatcher.dispatchCall(task3);
		dispatcher.dispatchCall(task4);
		dispatcher.dispatchCall(task5);
		// All operators are busy
		dispatcher.dispatchCall(task6);
		dispatcher.dispatchCall(task7);
		dispatcher.dispatchCall(task8);
		// All supervisors are busy
		dispatcher.dispatchCall(task9);
		Role role = Role.DIRECTOR;
		// Simulates call duration.
		Long time = 5000L;
		// Verify that the call is answered
		Assert.assertEquals(1, dispatcher.getDirectorQueue().size());
		dispatcher.answerCallByRole(role, time);
		Assert.assertEquals(0, dispatcher.getDirectorQueue().size());
		log.info("ENDING => testAnswerCallByRoleDirector TEST");
	}

}
