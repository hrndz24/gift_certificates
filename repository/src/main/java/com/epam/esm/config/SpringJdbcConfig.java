package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
public class SpringJdbcConfig {

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/gift_certificate?useUnicode=true&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("si3Llega7la5");
        return dataSource;
    }

    @Bean(name = "JdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }
}
