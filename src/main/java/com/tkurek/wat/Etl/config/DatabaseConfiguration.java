package com.tkurek.wat.Etl.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    public static final String DATABASE_SOURCE_ONE = "ZRODLO_POS";
    public static final String DATABASE_SOURCE_TWO = "ZRODLO_PRACOWNICY";
    public static final String DATABASE_STAGE = "STAGE";
    public static final String DATABASE_WAREHOUSE = "WAREHOUSE";
    public static final String DATABASE_METADATA = "METADATA";

    @Bean(name = DATABASE_SOURCE_ONE, destroyMethod = "")
    @ConfigurationProperties(prefix = "datasources.zrodlopos")
    @Primary
    public DataSource dataSourceOne() {
        return new HikariDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasources.zrodlopos.liquibase")
    public LiquibaseProperties primaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean(name = "liquibase", destroyMethod = "") // Pierwszy bean musi mieć nazwę 'liquibase' :/
    public SpringLiquibase primaryLiquibase() {
        return springLiquibase(dataSourceOne(), primaryLiquibaseProperties());
    }

    @Bean(name = DATABASE_SOURCE_TWO, destroyMethod = "")
    @ConfigurationProperties(prefix = "datasources.zrodlopracownicy")
    public DataSource dataSourceAnother() {
        return new HikariDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasources.zrodlopracownicy.liquibase")
    public LiquibaseProperties secendaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase secendaryLiquibase() {
        return springLiquibase(dataSourceOne(), secendaryLiquibaseProperties());
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }
}
