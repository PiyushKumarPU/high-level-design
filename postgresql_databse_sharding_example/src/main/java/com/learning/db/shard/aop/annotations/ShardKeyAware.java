package com.learning.db.shard.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to indicate that a method requires shard key management for database access.
 * <p>
 * Requirements:
 * - The first argument of the annotated method must be either:
 *     - a {@code long} representing the userId, or
 *     - a {@code User} object containing the userId.
 * <p>
 * Important:
 * - Do NOT use this annotation on methods also annotated with {@code @Transactional}.
 *   This is because Spring initializes the database connection before AOP advice is applied,
 *   which means the shard key would not be set in time.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShardKeyAware {
}
