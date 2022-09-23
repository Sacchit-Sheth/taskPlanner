package com.task.planner.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFilter;

@Document(collection = "tasks")
@JsonFilter("userFilter")
public class TaskDTO {

	@Id
	private String taskId;

	@NotNull(message = "Please provide a Title.")
	private String title;

	private String description;

	@NotNull(message = "CreatedBy cannot by null.")
	private String createdBy;

	private String assignedTo;

	private Date completedOn;

	@NotNull(message = "Status value not valid.")
	@Pattern(regexp = "(CREATED|PENDING|IN PROGRESS|DONE)", message = "Status value not valid.")
	private String status;

	private List<String> history = new ArrayList<String>();

	public TaskDTO() {
		super();
	}

	public TaskDTO(String taskId, String title, String description, String createdBy, String assignedTo,
			Date completedOn,
			@Pattern(regexp = "(CREATED|PENDING|IN PROGRESS|DONE)", message = "Status value not valiFd.") String status,
			List<String> history) {
		super();
		this.taskId = taskId;
		this.title = title;
		this.description = description;
		this.createdBy = createdBy;
		this.assignedTo = assignedTo;
		this.completedOn = completedOn;
		this.status = status;
		this.history = history;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(String update) {
		this.history.add(update);
	}
}
