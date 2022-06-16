package com.sofka.kata.controllers;

import com.sofka.kata.models.TodoList;
import com.sofka.kata.services.TodoListService;
import com.sofka.kata.utility.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/todo-list")
public class TodoListController {

    /**
     * Servicio para la entidad todoList
     */
    private final TodoListService todoListService;
    /**
     * Variable para el manejo de las respuestas de las API
     */
    private final Response response = new Response();

    /**
     * Variable para el manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }
    /**
     * Administrador para las excepciones del sistema
     *
     * @param exception Objeto Exception
     */
    private void getErrorMessageInternal(Exception exception) {
        response.error = true;
        response.message = exception.getMessage();
        response.data = exception.getCause();
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Administrador para las excepciones en SQL con respecto al manejo del acceso a los datos
     *
     * @param exception Objeto DataAccessException
     */
    private void getErrorMessageForResponse(DataAccessException exception) {
        response.error = true;
        if (exception.getRootCause() instanceof SQLException) {
            SQLException sqlEx = (SQLException) exception.getRootCause();
            var sqlErrorCode = sqlEx.getErrorCode();
            switch (sqlErrorCode) {
                case 1062:
                    response.message = "El dato ya está registrado";
                    break;
                case 1452:
                    response.message = "El usuario indicado no existe";
                    break;
                default:
                    response.message = exception.getMessage();
                    response.data = exception.getCause();
            }
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
            response.message = exception.getMessage();
            response.data = exception.getCause();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Endpoint para obtener todos los todoList
     *
     * @return Objeto Response en formato JSON
     */
    @GetMapping()
    public ResponseEntity<Response> getAllTodoList() {
        response.restart();
        try {
            response.data = todoListService.getAllTodoList();
            response.message = "Lista de todoList";
            httpStatus = HttpStatus.OK;
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);

    }

    /**
     * Endpoint para obtener un todoList por ID
     *
     * @param id Long
     * @return Objeto Response en formato JSON
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> getTodoList(@PathVariable("id") Long id) {
        response.restart();
        try {
            response.data = todoListService.getTodoList(id);
            response.message = "TodoList encontrado";
            httpStatus = HttpStatus.OK;
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para guardar un nuevo todoList
     *
     * @param todoList Objeto TodoList
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> saveTodoList(@RequestBody TodoList todoList) {
        response.restart();
        try {
            response.data = todoListService.saveTodoList(todoList);
            response.message = "TodoList creado";
            httpStatus = HttpStatus.CREATED;
        } catch (DataAccessException exception) {
            getErrorMessageForResponse(exception);
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para actualizar todolist por su ID
     *
     * @param todoList Objeto TodoList
     * @param id       Long
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTodoList(@RequestBody TodoList todoList, @PathVariable(value = "id") Long id) {
        response.restart();
        try {
            response.data = todoListService.updateTodoList(id, todoList);
            response.message = "TodoList actualizado";
            httpStatus = HttpStatus.OK;
        } catch (DataAccessException exception) {
            getErrorMessageForResponse(exception);
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para eliminar de un todoList por su ID.
     *
     * @param id del todo que se desea eliminar
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteTodoList(@PathVariable("id") Long id) {
        response.restart();
        try {
            response.data = todoListService.deleteTodoList(id);
            if (Boolean.TRUE.equals(response.data)) {
                response.message = "TodoList eliminado correctamente";
                httpStatus = HttpStatus.OK;
            } else {
                response.message = "TodoList no encontrado";
                httpStatus = HttpStatus.NOT_FOUND;
            }
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
