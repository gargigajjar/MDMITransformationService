package org.mdmi.rt.service.web;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application {
	@Value("${origins.url}")
	private String[] url;
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(url).allowedMethods("GET", "HEAD", "PUT");
            }
        };
    }
	public static void main(String[] args) throws Exception {
		
		Properties prop = new Properties();
		InputStream xxx = Application.class.getClassLoader().getResourceAsStream("version.properties");
		if (xxx != null) {
			prop.load(xxx);
			String RUNTIMEVERSION = prop.getProperty("version");
			String RUNTIMEBUILD = prop.getProperty("build.date");
			System.setProperty("mdmi.engine.version", RUNTIMEVERSION + " :: " + RUNTIMEBUILD);
		}

		SpringApplication.run(Application.class, args);
	}
}
