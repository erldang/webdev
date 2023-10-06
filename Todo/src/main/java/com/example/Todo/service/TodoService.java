package com.example.Todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Todo.model.TodoEntity;
import com.example.Todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	public Optional<TodoEntity>create(final TodoEntity entity){
		validate(entity);
		repository.save(entity);
		return repository.findById(entity.getId());
	}
	
	public List<TodoEntity>retrieve(final String userId){
		return repository.findByUserId(userId);
	}
	
	public Optional<TodoEntity>update(final TodoEntity entity){
		validate(entity);
		if(repository.existsById(entity.getId())) {
			repository.save(entity);
		}
		else throw new RuntimeException("Unknown id");
		
		return repository.findById(entity.getId());
	}
	
	public Optional<TodoEntity>updateTodo(final TodoEntity entity) {
		validate(entity);
		
		final Optional<TodoEntity> orignal = repository.findById(entity.getId());
		
		orignal.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			repository.save(todo);
		});
		return repository.findById(entity.getId());
	}
	
	public void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot benull");
		}
		if(entity.getUserId()==null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
}
