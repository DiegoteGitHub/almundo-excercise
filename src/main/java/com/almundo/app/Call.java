package com.almundo.app;

/**
 * This class represents a call that posesses an ID 
 * and a role to wich is assigned
 */
public class Call {
	
	/** The id. */
	int id;
	
	/** The assigned role. */
	Role assignTo;
			
	/**
	 * Instantiates a new task.
	 *
	 * @param id => the call ID
	 */
	public Call(int id) {
		super();
		this.id = id;
	}

	/**
	 * Instantiates a new task.
	 *
	 * @param id  => the call ID
	 * @param role => the employee role
	 */
	public Call(int id, Role role) {
		super();
		this.id = id;
		this.assignTo = role;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the call ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new call ID
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the assign to.
	 *
	 * @return the employee role
	 */
	public Role getAssignTo() {
		return assignTo;
	}

	/**
	 * Sets the assign to.
	 *
	 * @param assignTo the new employee role
	 */
	public void setAssignTo(Role assignTo) {
		this.assignTo = assignTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Call [id=" + id + ", assigned to Role=" + assignTo + "]";
	}
	
}
