package com.learning.db.shard.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ShardRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCurrentShard(String shardKey) {
        System.out.println("Setting current shard key : " + shardKey);
        CONTEXT.set(shardKey);
    }

    public static void clear() {
        System.out.println("Clearing current shard key : " + CONTEXT.get());
        CONTEXT.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CONTEXT.get();
    }
}

