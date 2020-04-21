package main.java.boot.controllers;

import java.net.InetAddress;

import main.java.boot.model.DBConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@Autowired
	DBConfig config;
	
	@RequestMapping("/")
	public String index(){
		return "Hello Spring Boot .....";
	}
	
	@RequestMapping("/host")
	public String host(){
		String hostname = null;
		String text = "";
		try {
			hostname = InetAddress.getLocalHost().getHostAddress();
			if(config != null){
				text = config.toString();
			}
		}
		catch(Exception ex){hostname="Unknown";ex.printStackTrace();}
		return "Spring Boot hosted as : " + hostname + "<br/>Config: " + text;
	}
}
