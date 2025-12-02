package com.example.modulo1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PessoaIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        r.add("spring.datasource.username", () -> postgres.getUsername());
        r.add("spring.datasource.password", () -> postgres.getPassword());
    }

    @Autowired
    private TestRestTemplate rt;

    @Test
    public void testListEndpoint() {
        ResponseEntity<String> res = rt.getForEntity("/api/pessoas?page=0&size=10", String.class);
        assert(res.getStatusCode() == HttpStatus.OK);
    }
}
