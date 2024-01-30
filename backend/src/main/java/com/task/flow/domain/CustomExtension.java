package com.task.flow.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class CustomExtension {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(length = 20)
    private String name;

    protected CustomExtension() {}

    public CustomExtension(String name) {
        this.name = name;
    }
}
