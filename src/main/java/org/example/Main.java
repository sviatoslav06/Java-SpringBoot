package org.example;

import jakarta.persistence.Entity;
import org.example.entities.CategoryEntity;
import org.example.repositories.CategoryRepository;
import org.example.storage.StorageProperties;
import org.example.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //System.out.println("Hello Java");
    }

    @Bean
    CommandLineRunner runner(CategoryRepository repository, StorageService storageService) {
        return args -> {
            try {
                storageService.init();

//            CategoryEntity category = new CategoryEntity();
//            category.setName("Продукти");
//            category.setDescription("Щоб поїсти");
//            category.setImage("1.jpg");
//            category.setDateCreated(new Date());

                //repository.save(category);
            }
            catch(Exception ex) {
                System.out.println("Помилка ініціалізації сайту " + ex.getMessage());
            }
        };
    }
}