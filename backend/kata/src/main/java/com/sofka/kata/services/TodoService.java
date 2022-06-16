package com.sofka.kata.services;


import com.sofka.kata.dto.TodoDto;
import com.sofka.kata.models.Todo;
import com.sofka.kata.repositories.TodoRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Clase tipo Servicio para la entidad Todo
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@Service
public class TodoService {

    /**
     * Repositorio del Todo.
     */
    private final TodoRepository todoRepository;
    /**
     * Objeto para mapear el dto.
     */
    private final ModelMapper mapper;

    //field injection
    public TodoService(TodoRepository todoRepository, ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper = mapper;
    }
    /**
     * Devuelve una lista con todos los Todo
     *
     * @return List
     */
    @Transactional(readOnly = true)
    public List<TodoDto> getAllTodo() {
        return todoRepository.findAll().stream().map(this::mapTodoDto).toList();
    }

    /**
     * Devuelve un todo por su id
     *
     * @param id Long
     * @return Objeto Todo
     */
    @Transactional(readOnly = true)
    public Todo getTodo(Long id) {
        return todoRepository.findById(id).orElseThrow();
    }

    /**
     * Mapea y guarda el objeto Todo
     *
     * @param todo TodoDto
     * @return Objeto TodoDto
     */
    @Transactional
    public TodoDto saveTodo(TodoDto todo) {
        Todo todoModel = mapTodo(todo);
        Todo saveTodo = todoRepository.save(todoModel);
        return mapper.map(saveTodo, TodoDto.class);
    }

    /**
     * Actualiza un todo por su id
     *
     * @param id   Long
     * @param todo TodoDto
     * @return Objeto TodoDto
     */
    @Transactional
    public TodoDto updateTodo(Long id, TodoDto todo) {
        todo.setId(id);
        Todo todoModel = mapTodo(todo);
        Todo saveTodo = todoRepository.save(todoModel);
        return mapper.map(saveTodo, TodoDto.class);
    }

    /**
     * Borra un Todo por su id
     *
     * @param id Long
     * @return Boolean
     */
    @Transactional
    public Boolean deleteTodo(Long id) {
        try {
            todoRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }


    /**
     * Método utilizado para mapear un todo a su dto
     *
     * @param todo Objeto
     * @return todoDto mapeado.
     */
    private TodoDto mapTodoDto(Todo todo) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        TodoDto todoDto;
        todoDto = mapper.map(todo, TodoDto.class);
        return todoDto;
    }

    /**
     * Método utilizado para mapear un todoDto a su todo
     *
     * @param todo Objeto
     * @return todo mapeado.
     */
    private Todo mapTodo(TodoDto todo) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Todo todoModel;
        todoModel = mapper.map(todo, Todo.class);
        return todoModel;
    }
}
