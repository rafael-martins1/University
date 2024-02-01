package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class DatabaseConfig {

	@Bean
	public DataSource dataSource() {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5433/trabalhotw");
		dataSource.setUser("postgres");
		dataSource.setPassword("trabalhosd");

		return dataSource;
	}

	@PostConstruct
	public void validateDatabaseConnection() {
		try (Connection ignored = dataSource().getConnection()) {
			System.out.println("Connected to the database");
		} catch (SQLException e) {
			System.err.println("Failed to connect to the database");
			e.printStackTrace();
		}
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

}
