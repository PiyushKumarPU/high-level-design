package com.learning.db.shard.metadata;

import com.learning.db.shard.routing.ShardRoutingDataSource;
import com.learning.db.shard.props.ShardProperties;
import com.learning.db.shard.props.ShardRange;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShardHelper {

    private final List<ShardRange> shardRanges = new ArrayList<>();

    private final ShardProperties shardProperties;

    @PostConstruct
    public void init() {
        shardRanges.addAll(shardProperties.getShards().stream()
                .map(shard -> ShardRange.builder()
                        .start(shard.getRange().getStart()).end(shard.getRange().getEnd()).name(shard.getName())
                        .build())
                .toList());
    }

    public String resolveShard(long userId) {
        List<ShardRange> matchingRanges = shardRanges.stream()
                .filter(range -> range.isValidRange(userId))
                .toList();

        if (matchingRanges.isEmpty()) {
            throw new IllegalArgumentException("No shard found for Shard key : " + userId);
        }

        if (matchingRanges.size() > 1) {
            String shardNames = matchingRanges.stream()
                    .map(ShardRange::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("Shard key " + userId + " matches multiple shard ranges: " + shardNames);
        }

        return matchingRanges.getFirst().name();
    }

    public void setShardKey(Long userId) {
        ShardRoutingDataSource.setCurrentShard(resolveShard(userId));
    }

    public void clearShardKey() {
        ShardRoutingDataSource.clear();
    }
}

