package com.ericsson.graduates.microservicemanager;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "engineer")
public class Engineer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "leadEngineer")
    @JsonIgnore
    private Set<Microservice> microservices;

    public Engineer() {
    }

    /**
     *
     * @param name
     * @param email
     */
    public Engineer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Microservice> getMicroservices() {
        return microservices;
    }

    public void setMicroservices(Set<Microservice> microservices) {
        this.microservices = microservices;
    }
}

