package com.learning.db.shard.aop.annotations;

import com.learning.db.shard.data.model.User;
import com.learning.db.shard.metadata.ShardHelper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ShardKeyAspect {

    private final ShardHelper shardHelper;

    // Pointcut that targets methods annotated with @ShardKeyAware
    @Pointcut("@annotation(com.learning.db.shard.aop.annotations.ShardKeyAware)")
    public void shardKeyRequiredMethods() {
        // This pointcut identifies methods annotated with @ShardKeyAware
    }


    @Before("shardKeyRequiredMethods() && args(arg, ..)")
    public void beforeMethodExecution(Object arg) {
        Long userId = resolveUserId(arg);  // Resolve userId from the argument
        if (userId == null)
            throw new IllegalArgumentException("Unable to locate shard key");
        shardHelper.setShardKey(userId);
    }

    @After("shardKeyRequiredMethods()")  // Executes after method execution
    public void afterMethodExecution() {
        shardHelper.clearShardKey();
    }

    private Long resolveUserId(Object arg) {
        if (arg instanceof Long) {
            return (Long) arg;  // If the argument is userId (Long), return it
        } else if (arg instanceof User) {
            return ((User) arg).getUserId();
        }
        return null;
    }
}

