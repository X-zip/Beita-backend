package com.example.demo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan("utils")
public class BeitaApplication  extends SpringBootServletInitializer  {
	public static void main(String[] args) {
		SpringApplication.run(BeitaApplication.class, args);
	}

}
