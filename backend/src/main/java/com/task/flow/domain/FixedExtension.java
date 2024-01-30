package com.task.flow.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class FixedExtension {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(columnDefinition = "boolean default false")
    @Setter private boolean isChecked;


    protected FixedExtension() {}

    public FixedExtension(Long id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }
}

