package de.othr.hwwa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;

public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String data;

    private Collection<Task> pastTasks;

    private Collection<Task> activeTasks;

}
