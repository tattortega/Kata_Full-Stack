package com.sofka.kata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Objeto DTO de la clase Todo
 *
 * @author Ricardo Ortega <tattortega.28@gmail.com>
 * @version 1.0.0 2022-06-16
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class TodoDto implements Serializable {
    private Long id;
    private String name;
    private Boolean completed;
    private Long todoListId;
    private String todoListName;
}