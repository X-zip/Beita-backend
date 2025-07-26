package com.example.demo.config;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 标识这是配置类，Spring会自动扫描并执行
public class MeilisearchConfig {

    // 从配置文件读取参数（自动注入）
    @Value("${meilisearch.url}")
    private String meiliUrl;

    @Value("${meilisearch.master-key}")
    private String masterKey;

    @Value("${meilisearch.index-name}")
    private String indexName;

    // 创建Client对象，并交给Spring容器管理
    @Bean // 标识这是一个Bean，其他地方可通过@Autowired注入
    public Client meilisearchClient() {
        // 使用配置文件的参数创建Config，避免硬编码
        Config config = new Config(meiliUrl, masterKey);
        return new Client(config);
    }

    // 创建Index对象，依赖上面的Client Bean
    @Bean
    public Index meilisearchIndex(Client client) { // Spring会自动注入上面的Client Bean
        return client.index(indexName); // 使用配置文件的索引名
    }
}
