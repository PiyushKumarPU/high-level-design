package com.learning.db.shard.config;

import com.learning.db.shard.routing.ShardRoutingDataSource;
import com.learning.db.shard.props.ShardProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class DataSourceConfig {

    private final ShardProperties shardProperties;

    @Bean
    public DataSource routingDataSource() {

        // constructing data source based on supplied shard information
        Map<Object, Object> targetDataSources = shardProperties.getShards().stream()
                .collect(Collectors.toMap(
                        ShardProperties.Shard::getName,
                        shard -> DataSourceBuilder.create()
                                .url(shard.getJdbcUrl())
                                .username(shard.getCredentials().getUsername())
                                .password(shard.getCredentials().getPassword())
                                .build(),
                        (a, b) -> b
                ));

        DataSource defaultDataSource = (DataSource) targetDataSources.values().stream().findFirst().orElseThrow(() -> new IllegalArgumentException("No shard found"));

        ShardRoutingDataSource routingDataSource = new ShardRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource); // fallback




        return routingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource routingDataSource) {
        return builder
                .dataSource(routingDataSource) // Use the routing data source for sharding
                .packages("com.learning.db.shard.data.model")  // The package where your entities are located
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}

