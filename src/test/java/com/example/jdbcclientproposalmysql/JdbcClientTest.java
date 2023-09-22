package com.example.jdbcclientproposalmysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
@JdbcTest
@Sql("/schema.sql")
class JdbcClientTest {
    @Autowired
    private JdbcClient jdbcClient;
    @Container
    @ServiceConnection
    private static final MySQLContainer mySQL =
            new MySQLContainer("mysql:latest")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @Test
    void worksWithNamedParamters() {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into persons(name) values (:name)")
                .param("name", "test")
                .update(keyHolder);
        assertThat(keyHolder.getKey().longValue()).isPositive();
    }

    @Test
    void doesNotWorkWithPositionalParamters() {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into persons(name) values (?)")
                .param("test")
                .update(keyHolder);
        assertThat(keyHolder.getKey().longValue()).isPositive();
    }
}
