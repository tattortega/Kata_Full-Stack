package com.sofka.kata.repositories;

import com.sofka.kata.models.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de la entidad TodoList
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
}
