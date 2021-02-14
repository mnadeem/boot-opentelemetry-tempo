package io.opentelemetry.example.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "provider1EntityManagerFactory", transactionManagerRef = "provider1TransactionManager", basePackages = {"io.opentelemetry.example.flight" })
public class Provider1DbConfig {
	
	@Primary
	@Bean(name = "provider1DataSourceProperties")
	@ConfigurationProperties("provider1.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}
	
	@Primary
	@Bean(name = "provider1DataSource")
	@ConfigurationProperties("provider1.datasource.configuration")
	public HikariDataSource dataSource(@Qualifier("provider1DataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
	
	@Primary
	@Autowired
	@Bean(name = "provider1NamedParamJdbcTemplate")
	public NamedParameterJdbcTemplate cdrNamedParamJdbcTemplate(
			@Qualifier("provider1DataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Primary
	@Bean(name = "provider1EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("provider1DataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("io.opentelemetry.example.flight").persistenceUnit("provider1").build();

	}
	
	@Primary
	@Bean(name = "provider1TransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("provider1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
