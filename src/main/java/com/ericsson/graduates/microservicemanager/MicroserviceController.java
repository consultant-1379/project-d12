package com.ericsson.graduates.microservicemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.*;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/microservices")
public class MicroserviceController {

    @Autowired
    private MicroserviceRepository microserviceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MicroserviceManagerApplication.class);


    @GetMapping(value = "/")
    public ResponseEntity<List<Microservice>> getAllMicroservices() {
        return ResponseEntity.ok().body(microserviceRepository.findAll());
    }



    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Microservice>> getMicroservicesByName(@PathVariable("name") String name) {
        List<Microservice> result = microserviceRepository.findByName(name);

        if(result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(result);
        }

    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Microservice> getMicroserviceById(@PathVariable("id") Long id) {
        Optional<Microservice> result = microserviceRepository.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @RequestMapping(value = "category/{category}", method = RequestMethod.GET)
    public ResponseEntity<List<Microservice>> getMicroServicesByCategory(@PathVariable("category") String category) {
        System.out.println(category);
        List<Microservice> result = microserviceRepository.findByCategory(category);
        System.out.println(result);
        return ResponseEntity.ok().body(result);
    }


    @PostMapping(value = "/add")
    public ResponseEntity<Microservice> addMicroservice(@RequestBody PostRequest postRequest) {
        if(microserviceRepository.findByName(postRequest.microservice.getName()).size() > 0) {
            return ResponseEntity.badRequest().build();
        }

        Set<Microservice> dependentMicroservices = new HashSet<>();

        postRequest.dependencies.forEach( dependencyName -> {
            Microservice dependency = microserviceRepository.findByName(dependencyName).get(0);
            postRequest.microservice.addDependency(dependency);
        });

        Microservice saved = microserviceRepository.save(postRequest.microservice);


        URI uri = URI.create("/microservice/" + saved.getId());
        return ResponseEntity.created(uri).body(saved);
    }


    /**
     * Add a dependency to a specified microservice.
     * @param parentId id of the parent microservice to which the dependency should be attached.
     * @param childId id of the child microservice which is considered the dependency.
     * @return parentDependencies Collection of the parent microservice's dependencies.
     */
    @PostMapping(value = "{parentId}/deps/{childId}")
    public ResponseEntity<Microservice> addMicroserviceDependencyById(@PathVariable("parentId") Long parentId, @PathVariable("childId") Long childId) {
        if (parentId.equals(childId))
            return ResponseEntity.badRequest().build();

        Microservice parentMicroservice, childMicroservice;
        try {
            parentMicroservice = microserviceRepository.getReferenceById(parentId);
            childMicroservice = microserviceRepository.getReferenceById(childId);
            LOGGER.info(String.format("Fetched microservices: \n%s\n%s\n", parentMicroservice, childMicroservice));
        } catch (EntityNotFoundException e) {
            LOGGER.warn(String.format("Tried to fetch microservice which doesn't exist!\n%s", e.getMessage()));
            return ResponseEntity.notFound().build();
        }
        parentMicroservice.addDependency(childMicroservice);
        Microservice result = microserviceRepository.save(parentMicroservice);
        return ResponseEntity.ok(result);
    }



    @GetMapping(value = "/dependent")
    public ResponseEntity<List<Microservice>> getDependentMicroservices() {
        List<Microservice> dependentMicroservices = microserviceRepository.getMicroserviceByDependenciesNotNull();
        if (dependentMicroservices.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dependentMicroservices);
    }


    @GetMapping(value = "/no-dependencies")
    public ResponseEntity<List<Microservice>> getMicroservicesNoDependencies() {
        List<Microservice> noDependenciesMicroservices = microserviceRepository.getMicroserviceByDependenciesIsNull();
        if (noDependenciesMicroservices.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(noDependenciesMicroservices);
    }

}
