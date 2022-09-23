package com.task.planner.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.planner.exception.TaskCollectionException;
import com.task.planner.model.TaskDTO;
import com.task.planner.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepo;

	@Override
	public List<TaskDTO> getAllTasks() {
		List<TaskDTO> tasks = taskRepo.findAll();
		if (tasks.size() > 0) {
			return tasks;
		} else {
			return new ArrayList<TaskDTO>();
		}
	}

	@Override
	public void createTask(TaskDTO task) throws TaskCollectionException {
		Optional<TaskDTO> taskOptional = taskRepo.findByTask(task.getTitle());
		if (taskOptional.isPresent()) {
			throw new TaskCollectionException(TaskCollectionException.TaskAlreadyExists());
		} else {
			taskRepo.save(task);
		}

	}

	@Override
	public void updateTask(String id, TaskDTO task) throws TaskCollectionException {
		Optional<TaskDTO> taskWithId = taskRepo.findByTaskId(id);
		String updateHistory = "";
		if (taskWithId.isPresent()) {
			TaskDTO taskToUpdate = taskWithId.get();

			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

			updateHistory += "Updated at: " + dateFormat.format(date) + ". ";
			if (task.getTitle() != null && !task.getTitle().trim().equals(taskToUpdate.getTitle())) {
				updateHistory += "Title from " + taskToUpdate.getTitle() + " to " + task.getTitle() + ".";
				taskToUpdate.setTitle(task.getTitle());
			}
			if (task.getDescription() != null && !task.getDescription().trim().equals(taskToUpdate.getDescription())) {
				updateHistory += "Description from " + taskToUpdate.getDescription() + " to " + task.getDescription()
						+ ". ";
				taskToUpdate.setDescription(task.getDescription());
			}
			if (task.getCreatedBy() != null && !task.getCreatedBy().trim().equals(taskToUpdate.getCreatedBy())) {
				updateHistory += "CreatedBy from " + taskToUpdate.getCreatedBy() + " to " + task.getCreatedBy() + ". ";
				taskToUpdate.setCreatedBy(task.getCreatedBy());
			}
			if (task.getAssignedTo() != null && !task.getAssignedTo().trim().equals(taskToUpdate.getAssignedTo())) {
				updateHistory += "AssignedTo from " + taskToUpdate.getAssignedTo() + " to " + task.getAssignedTo()
						+ ". ";
				taskToUpdate.setAssignedTo(task.getAssignedTo());
			}
			if ((task.getStatus() != null) && !task.getStatus().trim().equals(taskToUpdate.getStatus())) {
				System.out.print("!");
				updateHistory += "Status from " + taskToUpdate.getStatus() + " to " + task.getStatus() + ". ";
				taskToUpdate.setStatus(task.getStatus());
			}
			if (taskToUpdate.getStatus().trim().equals("DONE")) {
				taskToUpdate.setCompletedOn(new Date(System.currentTimeMillis()));
			}
			taskToUpdate.setHistory(updateHistory);
			taskRepo.save(taskToUpdate);
		} else {
			throw new TaskCollectionException(TaskCollectionException.NotFoundException(id));
		}

	}

	@Override
	public List<Optional<TaskDTO>> searchTasks(String taskId, String title) {
		List<Optional<TaskDTO>> tasks = new ArrayList<Optional<TaskDTO>>();
		if (taskId != null && !taskId.isEmpty()) {
			Optional<TaskDTO> tasksId = taskRepo.findByTaskId(taskId);
			if (tasksId.isPresent() && tasksId != null)
				tasks.add(tasksId);
		}
		if (title != null && !title.isEmpty()) {
			Optional<TaskDTO> tasksTitle = taskRepo.findByTitle(title);
			if (tasksTitle.isPresent() && tasksTitle != null)
				tasks.add(tasksTitle);
		}

		return tasks;
	}

	@Override
	public void assignTask(String taskId, String assignedTo) throws TaskCollectionException {

		Optional<TaskDTO> taskWithId = taskRepo.findByTaskId(taskId);
		if (taskWithId.isPresent()) {
			TaskDTO taskToUpdate = taskWithId.get();
			taskToUpdate.setAssignedTo(assignedTo);
			taskRepo.save(taskToUpdate);
		} else {
			throw new TaskCollectionException(TaskCollectionException.NotFoundException(taskId));
		}
	}

	@Override
	public TaskDTO getTaskHistory(String taskId) throws TaskCollectionException {

		Optional<TaskDTO> taskWithId = taskRepo.findByTaskId(taskId);
		if (taskWithId.isPresent()) {
			TaskDTO task = taskWithId.get();
			return task;
		} else {
			throw new TaskCollectionException(TaskCollectionException.NotFoundException(taskId));
		}
	}
}
