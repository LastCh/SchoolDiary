package ru.bmstu.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.bmstu.diary.util.CsvReaderWriter;

import java.io.File;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "ru.bmstu.diary")
public class AppConfig {
    @Bean
    public CsvReaderWriter csvReaderWriter(ResourceLoader resourceLoader) {
        String filePath = new File("data/students.csv").getAbsolutePath();
        Resource resource = resourceLoader.getResource("classpath:students.csv");
        return new CsvReaderWriter(filePath, resource);
    }
}