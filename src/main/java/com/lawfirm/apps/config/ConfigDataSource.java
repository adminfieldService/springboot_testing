/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.config;

import java.io.IOException;
import static java.util.Collections.singletonMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author newbiecihuy
 */
@Configuration
@EnableJpaRepositories(
        basePackages = Constants.PACKAGE_REPO_LF,//"com.spin.bpr.datasource.repo",
        entityManagerFactoryRef = "entityManagerLf",
        transactionManagerRef = "transactionManagerLf"
)
@EnableTransactionManagement
public class ConfigDataSource {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "entityManagerLf")
    public LocalContainerEntityManagerFactoryBean entityManagerDs(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dsDataSource())
                .properties(singletonMap("hibernate.hbm2ddl.auto", "create"))//.properties(hibernateProperties())#Hibernate ddl auto (none, create, create-drop, validate, update)s
                .packages(Constants.PACKAGE_ENTITIES_LF)//"com.spin.bpr.datasource.entity"
                .persistenceUnit(Constants.JPA_UNIT_NAME_LF).build();
    }

    @Primary
    @Bean(name = "dsDataSource")
    @ConfigurationProperties(prefix = "bar.datasource")
    public DataSource dsDataSource() {
        /*
	 * return DataSourceBuilder .create() .build();
         */
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("bar.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("bar.datasource.url"));
        dataSource.setUsername(env.getProperty("bar.datasource.username"));
        dataSource.setPassword(env.getProperty("bar.datasource.password"));
        return dataSource;
    }

    @Primary
    @Bean(name = "transactionManagerLf")
    public PlatformTransactionManager transactionManagerLf(@Qualifier("entityManagerLf") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, ?> hibernateProperties() {

        Resource resource = new ClassPathResource("hibernate.properties");

        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            return properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
