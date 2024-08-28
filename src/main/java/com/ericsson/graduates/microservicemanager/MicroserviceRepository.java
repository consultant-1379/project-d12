package com.ericsson.graduates.microservicemanager;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MicroserviceRepository extends JpaRepository<Microservice, Long> {
    List<Microservice> findByName(String name);

    List<Microservice> findByCategory(String category);

    List<Microservice> getMicroserviceByDependenciesNotNull();

    List<Microservice> getMicroserviceByDependenciesIsNull();
}
