package com.wanted.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @JsonBackReference
    @OneToMany(mappedBy = "company"
            , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Recruitment> recruitments;

}
