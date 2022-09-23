package com.task.planner.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.task.planner.model.TaskDTO;

@Repository
public interface TaskRepository extends MongoRepository<TaskDTO, String> {

	@Query("{'title': ?0}")
	Optional<TaskDTO> findByTask(String title);

	@Query("{'taskId': ?0}")
	Optional<TaskDTO> findByTaskId(String id);

	@Query("{'title': {$regex: ?0}}")
	Optional<TaskDTO> findByTitle(String title);
}
