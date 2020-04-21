package main.java.boot;

import main.java.boot.model.DBConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	@Bean
	public DBConfig dbConfig(){
		DBConfig config = new DBConfig();
		config.setDriverName("MYSQL");
		config.setUrl("localhost:3306/demo");
		config.setUserName("admin");
		config.setPassword("admin");
		config.setDatabaseName("demo");
		return config;
	}
	
}
