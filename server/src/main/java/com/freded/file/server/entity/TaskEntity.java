package com.freded.file.server.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Task in the entity manager.
 * It includes fields for task identification, name, description, creation date,
 * last updated date, and the user who created the task.
 */
@Entity
@Getter
public class TaskEntity {

    /**
     * Unique identifier for the task, generated randomly.
     */
    @Id
    private String id;

    private String createdBy;



}
