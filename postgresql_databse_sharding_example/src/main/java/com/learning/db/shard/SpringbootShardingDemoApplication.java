package com.learning.db.shard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.learning.db.shard.data.repository"
)
@EntityScan(basePackages = "com.learning.db.shard")
@EnableAspectJAutoProxy
public class SpringbootShardingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingDemoApplication.class, args);
    }

}
