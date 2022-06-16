package com.sofka.kata.services;

import com.sofka.kata.models.TodoList;
import com.sofka.kata.repositories.TodoListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Clase tipo Servicio para la entidad todoList
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@Service
public class TodoListService {

    /**
     * Repositorio del todoList
     */
    private final TodoListRepository todoListRepository;

    public TodoListService(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    /**
     * Devuelve una lista con todos los todoList
     *
     * @return List TodoList
     */
    @Transactional(readOnly = true)
    public List<TodoList> getAllTodoList() {
        return todoListRepository.findAll();
    }

    /**
     * Devuelve un todoList por su id.
     *
     * @param id Long
     * @return Objeto Todolist
     */
    @Transactional(readOnly = true)
    public TodoList getTodoList(Long id) {
        return todoListRepository.findById(id).orElseThrow();
    }

    /**
     * Guarda un nuevo todoList
     *
     * @param todoList TodoList
     * @return Todolist
     */
    @Transactional
    public TodoList saveTodoList(TodoList todoList) {
        return todoListRepository.save(todoList);
    }

    /**
     * Actualiza un todoList por su id
     *
     * @param id       Long
     * @param todoList Objeto TodoList
     * @return Objeto TodoList
     */
    @Transactional
    public TodoList updateTodoList(Long id, TodoList todoList) {
        todoList.setId(id);
        return todoListRepository.save(todoList);
    }

    /**
     * Borra un todoList por su id
     *
     * @param id Long
     */
    @Transactional
    public Boolean deleteTodoList(Long id) {
        try {
            todoListRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

}
