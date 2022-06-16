package com.sofka.kata.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

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
@Entity
@AllArgsConstructor
@Table(name = "todo")
public class Todo {

    /**
     * Identificador del todo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "td_id")
    private Long id;

    /**
     * Nombre del todo
     */
    @Column(name = "td_name", nullable = false, length = 100)
    private String name;

    /**
     * Estado del todo
     */
    @Column(name = "td_completed", nullable = false)
    private Boolean completed;

    /**
     * Relación de muchos a uno con la entidad todoList.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            targetEntity = TodoList.class,
            optional = false)
    @JoinColumn(name = "tdlist_id")
    @JsonBackReference
    private TodoList todoList;
}
