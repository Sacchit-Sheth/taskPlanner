package com.task.planner.exception;

public class TaskCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskCollectionException(String message) {
		super(message);
	}

	public static String NotFoundException(String id) {
		return "Task with " + id + " not found!";
	}

	public static String TaskAlreadyExists() {
		return "Task with given name already exists";
	}

	public static String StatusNotValid() {
		return "Status value not valid.";
	}
}
