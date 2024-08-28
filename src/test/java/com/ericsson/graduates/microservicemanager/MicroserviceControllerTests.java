package com.ericsson.graduates.microservicemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MicroserviceController.class)
public class MicroserviceControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MicroserviceRepository microserviceRepository;

    @Test
    void getByName_ReturnsMicroservice() throws Exception {
        when(microserviceRepository.findByName("serv1")).thenReturn(Collections.singletonList(
                new Microservice("serv1", null, "the service", new Date(), new ArrayList<>())));
        this.mockMvc.perform(get("/microservices/name/serv1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("serv1")));
    }

    @Test
    void getByName_NoMatch() throws Exception {
        when(microserviceRepository.findByName("serv1")).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/microservices/name/serv1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void getById_ReturnsMicroservice() throws Exception {
        when(microserviceRepository.findById(1L)).thenReturn(
                Optional.of(new Microservice("serv1", null, "the service", new Date(), new ArrayList<>())));
        this.mockMvc.perform(get("/microservices/id/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("serv1")));
    }

    @Test
    void getById_NoMatch() throws Exception {
        when(microserviceRepository.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/microservices/id/1")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString("")));
    }

    @Test
    void getByCategory_ReturnsMicroservice() throws Exception {
        when(microserviceRepository.findByCategory("testCat")).thenReturn(Collections.singletonList(
                new Microservice("serv1", null, "the service", new Date(), new ArrayList<>(), "testCat")));
        this.mockMvc.perform(get("/microservices/category/testCat")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("serv1")));
    }
    @Test
    void getByCategory_NoMatch() throws Exception {
        when(microserviceRepository.findByCategory("testCat")).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/microservices/category/testCat")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void addMicroservice_Successfully() throws Exception {
        Microservice msA = new Microservice("serv1", null, "some service", new Date(), Collections.singletonList("0.0.1"));
        msA.setId(1L);
        PostRequest postReq = new PostRequest(msA, Collections.emptyList());
        when(microserviceRepository.save(ArgumentMatchers.any(Microservice.class))).thenReturn(msA);
        this.mockMvc.perform(
                post("/microservices/add")
                        .content(asJsonString(postReq))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void addMicroservice_MalformedRequest() throws Exception {
        this.mockMvc.perform(
                        post("/microservices/add")
                                .content("")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addDependency_Successfully() throws Exception {
        Microservice msA = new Microservice("serv1", null, "some service", new Date(), Collections.singletonList("0.0.1"));
        msA.setId(1L);
        Microservice msB = new Microservice("serv1", null, "some service", new Date(), Collections.singletonList("0.0.1"));
        msB.setId(2L);

        //Bind parent and child references to mock
        when(microserviceRepository.getReferenceById(ArgumentMatchers.eq(1L))).thenReturn(msA);
        when(microserviceRepository.getReferenceById(ArgumentMatchers.eq(2L))).thenReturn(msB);

        //Bind parent to return as saved entity by the mock
        when(microserviceRepository.save(ArgumentMatchers.eq(msA))).thenReturn(msA);
        this.mockMvc.perform(
                post("/microservices/1/deps/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dependencies").hasJsonPath());
    }

    @Test
    void addDependency_PreventsParentFromBeingItsOwnChild() throws Exception {
        Microservice msA = new Microservice("serv1", null, "some service", new Date(), Collections.singletonList("0.0.1"));
        msA.setId(1L);

        //Bind parent and child references to mock
        when(microserviceRepository.getReferenceById(ArgumentMatchers.eq(1L))).thenReturn(msA);

        //Bind parent to return as saved entity by the mock
        when(microserviceRepository.save(ArgumentMatchers.eq(msA))).thenReturn(msA);
        this.mockMvc.perform(
                        post("/microservices/1/deps/1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addDependency_EntityNotFoundHandled() throws Exception {
        when(microserviceRepository.getReferenceById(ArgumentMatchers.eq(1L))).thenThrow(EntityNotFoundException.class);
        when(microserviceRepository.getReferenceById(ArgumentMatchers.eq(2L))).thenThrow(EntityNotFoundException.class);
        this.mockMvc.perform(
                        post("/microservices/1/deps/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findDependents_Successfully() throws Exception {
        Microservice msA = new Microservice("serv1", null, "the service", new Date(), new ArrayList<>());
        Microservice msB = new Microservice("serv2", null, "the service", new Date(), new ArrayList<>());
        msA.addDependency(msB);
        when(microserviceRepository.getMicroserviceByDependenciesNotNull()).thenReturn(Collections.singletonList(msA));
        this.mockMvc.perform(get("/microservices/dependent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("serv1")));
    }

    @Test
    void findDependents_NonePresent() throws Exception {
        when(microserviceRepository.getMicroserviceByDependenciesNotNull()).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/microservices/dependent"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findNoDependencies_Successfully() throws Exception {
        Microservice msA = new Microservice("serv1", null, "the service", new Date(), new ArrayList<>());
        when(microserviceRepository.getMicroserviceByDependenciesIsNull()).thenReturn(Collections.singletonList(msA));
        this.mockMvc.perform(get("/microservices/no-dependencies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("serv1")));
    }

    @Test
    void findNoDependencies_NonePresent() throws Exception {
        when(microserviceRepository.getMicroserviceByDependenciesIsNull()).thenReturn(Collections.emptyList());
        this.mockMvc.perform(get("/microservices/no-dependencies"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
