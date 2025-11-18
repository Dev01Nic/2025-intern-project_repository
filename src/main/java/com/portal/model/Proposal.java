package com.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "proposal")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String task;

    public Proposal() {
    }

    public Proposal(Long id, String task) {
        this.id = id;
        this.task = task;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }
}
