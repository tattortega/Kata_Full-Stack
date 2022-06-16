package com.sofka.kata.controllers;

import com.sofka.kata.dto.TodoDto;
import com.sofka.kata.services.TodoService;
import com.sofka.kata.utility.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * Controlador para el todo
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT})
@RequestMapping("/todo")
public class TodoController {

    /**
     * Servicio para la entidad todo
     */
    private final TodoService todoService;
    /**
     * Variable para el manejo de las respuestas de las API
     */
    private final Response response = new Response();

    /**
     * Variable para el manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
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
     * Endpoint para obtener todos los todo
     *
     * @return Objeto Response en formato JSON
     */
    @GetMapping()
    public ResponseEntity<Response> getAllTodo() {
        response.restart();
        try {
            response.data = todoService.getAllTodo();
            response.message = "Lista de todo";
            httpStatus = HttpStatus.OK;
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);

    }

    /**
     * Endpoint para obtener un todo por ID
     *
     * @param id Long
     * @return Objeto Response en formato JSON
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> getTodo(@PathVariable("id") Long id) {
        response.restart();
        try {
            response.data = todoService.getTodo(id);
            response.message = "Todo encontrado";
            httpStatus = HttpStatus.OK;
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para guardar un nuevo todo
     *
     * @param todoDto Objeto TodoDto
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> saveTodo(@RequestBody TodoDto todoDto) {
        response.restart();
        try {
            response.data = todoService.saveTodo(todoDto);
            response.message = "Todo creado";
            httpStatus = HttpStatus.CREATED;
        } catch (DataAccessException exception) {
            getErrorMessageForResponse(exception);
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para actualizar todo por su ID
     *
     * @param todo TodoDto
     * @param id   Long
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTodo(@RequestBody TodoDto todo, @PathVariable(value = "id") Long id) {
        response.restart();
        try {
            response.data = todoService.updateTodo(id, todo);
            response.message = "Todo actualizado";
            httpStatus = HttpStatus.OK;
        } catch (DataAccessException exception) {
            getErrorMessageForResponse(exception);
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Endpoint para eliminar de un todo por su ID.
     *
     * @param id del todo que se desea eliminar
     * @return Objeto Response en formato JSON
     */
    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> deleteTodo(@PathVariable("id") Long id) {
        response.restart();
        try {
            response.data = todoService.deleteTodo(id);
            if (Boolean.TRUE.equals(response.data)) {
                response.message = "Todo eliminado correctamente";
                httpStatus = HttpStatus.OK;
            } else {
                response.message = "Todo no encontrado";
                httpStatus = HttpStatus.NOT_FOUND;
            }
        } catch (Exception exception) {
            getErrorMessageInternal(exception);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
