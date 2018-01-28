package com.almundo.app;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the concurrent task assignment to each role using four queues
 * one for each role and one for ON_HOLD calls
 * (OPERATOR/SUPERVISOR/DIRECTOR).
 */
public class Dispatcher {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	/** The common queue. */
	Queue<Call> commonQueue = new ConcurrentLinkedQueue<Call>();

	/** The operators queue. */
	LinkedBlockingQueue<Call> operatorQueue;

	/** The director queue. */
	LinkedBlockingQueue<Call> directorQueue;

	/** The supervisor queue. */
	LinkedBlockingQueue<Call> supervisorQueue;

	/** Default total operators. */
	int totalOperator = 5;

	/** Default The total supervisors. */
	int totalSupervisor = 2;

	/** Default total directors. */
	int totalDirector = 1;

	/**
	 * Initializes a new dispatcher with defaults.
	 */
	Dispatcher() {
		operatorQueue = new LinkedBlockingQueue<Call>(totalOperator);
		supervisorQueue = new LinkedBlockingQueue<Call>(totalSupervisor);
		directorQueue = new LinkedBlockingQueue<Call>(totalDirector);
	}

	/**
	 * Initializes a new dispatcher with the amount of different roles passed by parameter.
	 *
	 * @param totalOperator => the total operators
	 * @param totalSupervisor => the total supervisors
	 * @param totalDirector => the total director
	 */
	Dispatcher(int totalOperator, int totalSupervisor, int totalDirector) {
		this.totalOperator = totalOperator;
		this.totalSupervisor = totalSupervisor;
		this.totalDirector = totalDirector;
		operatorQueue = new LinkedBlockingQueue<Call>(totalOperator);
		supervisorQueue = new LinkedBlockingQueue<Call>(totalSupervisor);
		directorQueue = new LinkedBlockingQueue<Call>(totalDirector);
	}

	/**
	 * Get task by role.
	 *
	 * @param role => Employee role
	 * @param time => call duration
	 * @return task element (call)
	 */
	public synchronized Call answerCallByRole(Role role, Long time) {
		Call task = null;
		switch (role) {
		case OPERATOR:
			if (operatorQueue.isEmpty())
				task = takeTaskOnHold(role);
			else {
				task = operatorQueue.poll();
				log.info("Employee " + role + " taking the call: " + task + " - Time: " + time + " - Pending: " + operatorQueue.size());
			}
			break;
		case SUPERVISOR:
			if (supervisorQueue.isEmpty())
				task = takeTaskOnHold(role);
			else {
				task = supervisorQueue.poll();
				log.info("Employee " + role + " taking the call: " + task + " - Time: " + time + " - Pending: " + supervisorQueue.size());
			}
			break;
		case DIRECTOR:
			if (directorQueue.isEmpty())
				task = takeTaskOnHold(role);
			else {
				task = directorQueue.poll();
				log.info("Employee " + role + " taking the call: " + task + " - Time: " + time + " - Pending: " + directorQueue.size());
			}
			break;
		default:
			log.info("Hold on a moment, all employees are busy");
			break;
		}
		if (log.isDebugEnabled()) {
			log.debug("\nPending operators: " + operatorQueue.size() + "\n" + " Pending supervisors: "	+ supervisorQueue.size() + "\n" + " Pending directors: " + directorQueue.size());
		}
		return task;
	}

	/**
	 * Take task on hold.
	 *
	 * @param role => Employee role
	 */
	private synchronized Call takeTaskOnHold(Role role) {
		Call task = commonQueue.poll();
		if (!commonQueue.isEmpty()) {
			log.info("Employee " + role + " taking on hold call: " + task + " - Pending: " + commonQueue.size());
		}
		return task;
	}

	/**
	 * Assign the call to the available employees (first to OPERATOR role, second to SUPERVISOR role
	 * third to DIRECTOR role and if no employees are available => the call is ON_HOLD
	 *
	 * @param task => the call to enqueue
	 */
	public synchronized void dispatchCall(Call call) {
		addTask2EmployeeQueue(call);
	}

	/**
	 * Adds the task to employee queue
	 * @param task => the call to enqueue
	 */
	private void addTask2EmployeeQueue(Call task) {
		try {
			task.setAssignTo(Role.OPERATOR);
			operatorQueue.add(task);
			log.info("Assigning call: " + task.getId() + " to operators queue.");
		} catch (IllegalStateException ex) {
			log.info("At this moment all OPERATORS are busy, call will be transferred to SUPERVISORS queue.");
			addTask2SupervisorQueue(task);
		}
	}

	/**
	 * Adds the task to supervisor queue
	 * @param task => The call to enqueue
	 */
	private void addTask2SupervisorQueue(Call task) {
		try {
			task.setAssignTo(Role.SUPERVISOR);
			supervisorQueue.add(task);
			log.info("Assigning call: " + task.getId() + " to supervisors queue.");
		} catch (IllegalStateException ex) {
			log.info("At this moment all SUPERVISORS are busy, call will be transferred to DIRECTORS queue.");
			addTask2DirectorQueue(task);
		}
	}

	/**
	 * Adds the task to director queue
	 * @param task => The call to enqueue
	 */
	private void addTask2DirectorQueue(Call task) {
		try {
			task.setAssignTo(Role.DIRECTOR);
			directorQueue.add(task);
			log.info("Assigning call: " + task.getId() + " to directors queue.");
		} catch (IllegalStateException ex) {
			log.info("At this moment all DIRECTORS are busy, call will be put ON HOLD...");
			addTask2CommonQueue(task);
		}
	}

	/**
	 * Adds the task to common queue
	 * @param task => The call to enqueue
	 */
	private void addTask2CommonQueue(Call task) {
		log.info("Assigning call: " + task.getId() + " to ON HOLD queue.");
		task.setAssignTo(Role.ON_HOLD);
		commonQueue.add(task);

	}

	/**
	 * Gets the common queue.
	 *
	 * @return the common queue
	 */
	public Queue<Call> getCommonQueue() {
		return commonQueue;
	}

	/**
	 * Sets the common queue.
	 *
	 * @param commonQueue the new common queue
	 */
	public void setCommonQueue(Queue<Call> commonQueue) {
		this.commonQueue = commonQueue;
	}

	/**
	 * Gets the employee queue.
	 *
	 * @return the employee queue
	 */
	public LinkedBlockingQueue<Call> getOperatorQueue() {
		return operatorQueue;
	}

	/**
	 * Sets the employee queue.
	 *
	 * @param employeeQueue the new employee queue
	 */
	public void setOperatorQueue(LinkedBlockingQueue<Call> employeeQueue) {
		this.operatorQueue = employeeQueue;
	}

	/**
	 * Gets the director queue.
	 *
	 * @return the director queue
	 */
	public LinkedBlockingQueue<Call> getDirectorQueue() {
		return directorQueue;
	}

	/**
	 * Sets the director queue.
	 *
	 * @param directorQueue the new director queue
	 */
	public void setDirectorQueue(LinkedBlockingQueue<Call> directorQueue) {
		this.directorQueue = directorQueue;
	}

	/**
	 * Gets the supervisor queue.
	 *
	 * @return the supervisor queue
	 */
	public LinkedBlockingQueue<Call> getSupervisorQueue() {
		return supervisorQueue;
	}

	/**
	 * Sets the supervisor queue.
	 *
	 * @param supervisorQueue the new supervisor queue
	 */
	public void setSupervisorQueue(LinkedBlockingQueue<Call> supervisorQueue) {
		this.supervisorQueue = supervisorQueue;
	}

	/**
	 * Gets the total employee.
	 *
	 * @return the total employee
	 */
	public int gettotalOperator() {
		return totalOperator;
	}

	/**
	 * Sets the total employee.
	 *
	 * @param totalOperator the new total employee
	 */
	public void settotalOperator(int totalOperator) {
		this.totalOperator = totalOperator;
	}

	/**
	 * Gets the total supervisor.
	 *
	 * @return the total supervisor
	 */
	public int getTotalSupervisor() {
		return totalSupervisor;
	}

	/**
	 * Sets the total supervisor.
	 *
	 * @param totalSupervisor the new total supervisor
	 */
	public void setTotalSupervisor(int totalSupervisor) {
		this.totalSupervisor = totalSupervisor;
	}

	/**
	 * Gets the total director.
	 *
	 * @return the total director
	 */
	public int getTotalDirector() {
		return totalDirector;
	}

	/**
	 * Sets the total director.
	 *
	 * @param totalDirector the new total director
	 */
	public void setTotalDirector(int totalDirector) {
		this.totalDirector = totalDirector;
	}

}
