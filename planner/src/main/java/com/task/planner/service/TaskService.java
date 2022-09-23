package com.task.planner.service;

import java.util.List;
import java.util.Optional;

import com.task.planner.exception.TaskCollectionException;
import com.task.planner.model.TaskDTO;

public interface TaskService {


	public List<TaskDTO> getAllTasks();

	public void createTask(TaskDTO task) throws TaskCollectionException;
	
	public void updateTask(String id, TaskDTO task) throws TaskCollectionException;
	
	public List<Optional<TaskDTO>> searchTasks(String taskId,String title);
	
	public void assignTask(String taskId, String assignedTo) throws TaskCollectionException;

	public TaskDTO getTaskHistory(String taskId) throws TaskCollectionException;
	
}
