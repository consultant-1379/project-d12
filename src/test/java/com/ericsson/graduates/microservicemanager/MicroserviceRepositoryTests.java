package com.ericsson.graduates.microservicemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class MicroserviceRepositoryTests {
	@Autowired private MicroserviceRepository microserviceRepository;
	private static Microservice msA, msB, msC, msD, msE;

	@BeforeEach
	void setUp() {
		msA = new Microservice("serv1", null, "some service", new Date(), Collections.singletonList("0.0.1"));
		msB = new Microservice("serv2", null, "a service", new Date(), Collections.singletonList("0.0.2"));
		msC = new Microservice("serv3", null, "the service", new Date(), Collections.singletonList("0.0.3"));
		msD = new Microservice("serv4", null, "the service", new Date(), Arrays.asList("0.0.4", "0.0.5", "0.1.1"), "test1");
	}

	@Test
	void contextLoads() {
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(microserviceRepository, is(notNullValue()));
	}

	@Test
	void microserviceInsertsSuccessfully() {
		microserviceRepository.save(msA);
		Microservice result = microserviceRepository.findByName("serv1").stream().findFirst().get();
		assertThat(result, hasProperty("name", is("serv1")));
	}

	@Test
	void microserviceFoundByCategory_Present() {
		microserviceRepository.save(msD);
		Microservice result = microserviceRepository.findByCategory("test1").stream().findFirst().get();
		assertThat(result, hasProperty("category", is("test1")));
	}

	@Test
	void microserviceNotFoundByCategory_Absent() {
		microserviceRepository.save(msC);
		List<Microservice> result = microserviceRepository.findByCategory("nonExistentCategory");
		assertThat(result, is(empty()));
	}

	@Test
	void microserviceFindByDependencies_DependenciesExist_FindsNone() {
		microserviceRepository.deleteAll();
		microserviceRepository.save(msA);
		microserviceRepository.save(msB);
		List<Microservice> result = microserviceRepository.getMicroserviceByDependenciesNotNull();
		assertThat(result, is(empty()));
	}

	@Test
	void microserviceFindByDependencies_DependenciesExist_FindsOne() {
		microserviceRepository.deleteAll();
		microserviceRepository.save(msD);
		msC.addDependency(msD);
		microserviceRepository.save(msC);
		List<Microservice> result = microserviceRepository.getMicroserviceByDependenciesNotNull();
		assertThat(result, hasSize(1));
	}

	@Test
	void microserviceFindByDependencies_NoDependencies_FindsOne() {
		microserviceRepository.deleteAll();
		microserviceRepository.save(msD);
		msC.addDependency(msD);
		microserviceRepository.save(msC);
		List<Microservice> result = microserviceRepository.getMicroserviceByDependenciesIsNull();
		assertThat(result, hasSize(1));
	}

	@Test
	void microserviceFindByDependencies_NoDependencies_FindsAll() {
		microserviceRepository.deleteAll();
		microserviceRepository.save(msA);
		microserviceRepository.save(msB);
		List<Microservice> result = microserviceRepository.getMicroserviceByDependenciesIsNull();
		assertThat(result, hasSize(2));
	}
}
