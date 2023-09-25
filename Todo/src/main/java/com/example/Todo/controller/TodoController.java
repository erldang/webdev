package com.example.Todo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Todo.dto.ResponseDTO;
import com.example.Todo.dto.TodoDTO;
import com.example.Todo.model.TodoEntity;
import com.example.Todo.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?>createTodo(@RequestBody TodoDTO dto){
		try {
			log.info("Log:createTodo entrance");
			
			TodoEntity entity = TodoDTO.toEntity(dto);
			log.info("Log:dto => entity ok!");
			
			entity.setUserId("temporary-user");
			
			Optional<TodoEntity> entities = service.create(entity);
			log.info("Log:serivce.create ok!");
			
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			log.info("Log:entities => dtos ok!");
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("Log:responsedto ok!");
			
			return ResponseEntity.ok().body(response);
		}catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?>retrieveTodoList() {
		String temporaryUserId = "temporary-user";
		List<TodoEntity> entities = service.retrieve(temporaryUserId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
}
