package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db.properties")
public class SpringJdbcConfig {

    private Environment environment;

    @Autowired
    public SpringJdbcConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url.1"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name.1"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username.1"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password.1"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }
}
