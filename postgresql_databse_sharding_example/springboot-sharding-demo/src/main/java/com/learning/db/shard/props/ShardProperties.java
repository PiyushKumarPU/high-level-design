package com.learning.db.shard.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "sharding")
public class ShardProperties {
    private List<Shard> shards;


    @Data
    public static class Shard {
        private Range range;
        private String name;
        private String host;
        private int port;
        private String url;
        private String database;
        private Credentials credentials;

        public String getJdbcUrl() {
            return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
        }
    }

    @Data
    public static class Range {
        private int start;
        private int end;
    }

    @Data
    public static class Credentials {
        private String username;
        private String password;
    }

}
