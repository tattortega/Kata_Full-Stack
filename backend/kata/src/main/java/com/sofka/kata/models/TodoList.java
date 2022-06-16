package com.sofka.kata.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Entidad del Todo
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todolist")
public class TodoList {

    /**
     * Identificador del todoList
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tl_id")
    private Long id;

    /**
     * Nombre del todoList
     */
    @Column(name = "tl_name", nullable = false, length = 30)
    private String name;

    /**
     * Relación de uno a muchos con la entidad todo
     */
    @OneToMany(mappedBy = "todoList",
            targetEntity = Todo.class,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Todo> todos;
}
