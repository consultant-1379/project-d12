package com.ericsson.graduates.microservicemanager;

import java.util.List;

public class PostRequest {
    public Microservice microservice;
    public List<String> dependencies;

    public PostRequest(Microservice microservice, List<String> dependencies) {
        this.microservice = microservice;
        this.dependencies = dependencies;
    }
}
