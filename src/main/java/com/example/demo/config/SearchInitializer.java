package com.example.demo.config;
import com.example.demo.service.BeitaService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchInitializer {

    @Bean
    public ApplicationRunner triggerMeilisearchInit(BeitaService searchService) {
        return args -> {
            // 应用启动后异步触发初始化
            searchService.initMeilisearch();
        };
    }
}
