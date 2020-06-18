package com.ukrpost.test.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class AppConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public SpringLiquibase liquibase() {
	    SpringLiquibase liquibase = new SpringLiquibase();
	    liquibase.setChangeLog("classpath:db/changelog.xml");
	    liquibase.setDataSource(getDataSource());
	    return liquibase;
	}
	
	public DataSource getDataSource() {
		return this.dataSource;
	}
}
