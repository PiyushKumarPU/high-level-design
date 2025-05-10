package com.learning.db.shard.props;

import lombok.Builder;

@Builder(toBuilder = true)
public record ShardRange(long start, long end, String name) {

    public boolean isValidRange(long userId) {
        return userId >= start() && userId <= end();
    }
}
