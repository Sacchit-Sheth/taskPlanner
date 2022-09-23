package com.task.planner.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.task.planner.exception.TaskCollectionException;
import com.task.planner.model.TaskDTO;
import com.task.planner.service.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	// Get all the tasks
	@GetMapping("/tasks")
	public ResponseEntity<MappingJacksonValue> getAllTasks() {
		List<TaskDTO> tasks = taskService.getAllTasks();
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("history");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", simpleBeanPropertyFilter);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(tasks);
		mappingJacksonValue.setFilters(filterProvider);
		return new ResponseEntity<>(mappingJacksonValue, tasks.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	// Create a new task
	@PostMapping("/task/create")
	public ResponseEntity<?> createTask(@RequestBody TaskDTO task) {
		try {
			taskService.createTask(task);
			SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("history");
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter",
					simpleBeanPropertyFilter);
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(task);
			mappingJacksonValue.setFilters(filterProvider);
			return new ResponseEntity<MappingJacksonValue>(mappingJacksonValue, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (TaskCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	// Update task with given ID
	@PutMapping("/task/update/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TaskDTO task) {
		try {
			taskService.updateTask(id, task);
			return new ResponseEntity<>("Updated Task with id " + id + "", HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (TaskCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Search for a given task with ID and/or title
	@GetMapping("/task/search/")
	public ResponseEntity<?> searchTasks(@RequestParam(required = false) String taskId,
			@RequestParam(required = false) String title) {
		List<Optional<TaskDTO>> tasks = taskService.searchTasks(taskId, title);
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("history");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", simpleBeanPropertyFilter);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(tasks);
		mappingJacksonValue.setFilters(filterProvider);
		return new ResponseEntity<>(tasks != null ? mappingJacksonValue : new ArrayList<Optional<TaskDTO>>(),
				tasks.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	// Assign Task with ID to assignee
	@PutMapping("task/{taskId}/assign/{assignedTo}")
	public ResponseEntity<?> assignTask(@PathVariable("taskId") String taskId,
			@PathVariable("assignedTo") String assignedTo) {

		try {
			taskService.assignTask(taskId, assignedTo);
			return new ResponseEntity<>("Assigned Task with id " + taskId + " to " + assignedTo, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (TaskCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// Show history of updates for a given task
	@GetMapping("/task/{taskId}/history")
	public ResponseEntity<?> getTaskHistory(@PathVariable("taskId") String taskId) throws TaskCollectionException {
		TaskDTO task = taskService.getTaskHistory(taskId);
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("history");
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", simpleBeanPropertyFilter);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(task);
		mappingJacksonValue.setFilters(filterProvider);
		return new ResponseEntity<>(mappingJacksonValue, task == null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

}
