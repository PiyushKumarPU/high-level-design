
# Spring Boot Database Sharding Demo


## What is Database Sharding?

**Database sharding** is the practice of breaking up a large database into smaller, faster, more easily managed parts called *shards*. Each shard holds a unique subset of the data, typically partitioned by a *shard key*. Rather than scaling vertically (i.e., upgrading a single machine), sharding enables **horizontal scaling**, which is more cost-effective and performant at large scale.

### Benefits of Sharding

- **Improved Performance**: Smaller datasets per shard lead to faster queries and lower latencies.
- **Horizontal Scalability**: You can add more shards (nodes) to handle increasing data or load.
- **Increased Availability**: A failure in one shard doesn’t necessarily bring down the entire system.
- **Better Maintainability**: Smaller datasets are easier to back up, migrate, and maintain.

### How to Choose a Shard Key

Choosing the right **shard key** is critical to prevent *hotspotting*, which occurs when one shard receives a disproportionate amount of traffic or data.

**Tips to select an effective shard key:**
- Choose a key with **high cardinality** (e.g., `user_id`, `order_id`).
- Ensure the key’s access pattern distributes **evenly** across shards.
- Avoid time-based keys unless bucketing (e.g., YYYY-MM).
- Analyze query access patterns (e.g., don’t use `country` if 90% of users are from one).

**Common Anti-patterns:**
- Using sequential keys like `auto_increment` IDs → can lead to one shard being overloaded.
- Unevenly distributed keys (e.g., geography, status flags) → can cause data skew.

### Open Source References

- [Vitess (YouTube, Slack, GitHub sharding)](https://vitess.io/)
- [Citus (PostgreSQL sharding)](https://www.citusdata.com/)
- [Apache ShardingSphere](https://shardingsphere.apache.org/)
- [CockroachDB](https://www.cockroachlabs.com/docs/stable/architecture/overview.html)

> Real-world usage: Companies like Instagram, Twitter, Slack, and Uber use sharding to handle billions of records and high-throughput traffic reliably.


## Overview

This project demonstrates a **database sharding** implementation using **Spring Boot** and **PostgreSQL**, configured with **YAML-based shard metadata** and **AOP-based shard resolution**.

### What is Database Sharding?

Database sharding is a technique for distributing data across multiple databases (shards) to improve scalability and performance. Each shard contains a subset of the total data, often divided by a shard key (e.g., `user_id`).

In this project:
- Shard key: `user_id`
- Four shards: `shard-a`, `shard-b`, `shard-c`, and `shard-d`
- Each shard handles a user ID range:
  - `shard-a`: 1–25
  - `shard-b`: 26–50
  - `shard-c`: 51–75
  - `shard-d`: 76–100


## Sharding vs. Table Partitioning

While both **sharding** and **table partitioning** aim to optimize database performance by splitting large datasets, they serve different purposes and operate at different levels.

### Key Differences

| Feature                  | Sharding                                         | Table Partitioning                           |
|--------------------------|--------------------------------------------------|-----------------------------------------------|
| **Definition**            | Horizontal scaling by splitting data across **multiple physical databases or servers** | Logical subdivision of a table **within a single database** |
| **Where it's applied**    | Across databases/instances (e.g., shard_a, shard_b) | Inside a single database table (e.g., `users`) |
| **Scaling**               | **Horizontal** — can scale out across machines  | **Vertical** — stays on same machine          |
| **Isolation**             | Better — each shard is an independent DB        | Less — partitions share resources (CPU, disk) |
| **Failure tolerance**     | High — one shard down doesn’t affect others     | Low — table partitions go down together       |
| **Administration**        | More complex (multiple DBs to manage)           | Easier — single DB, often native support      |
| **Cross-node queries**    | Harder — may need orchestration or scatter-gather logic | Easy — DB engine manages partitions internally |

### Why Use Sharding Over Partitioning?

You **need sharding** when:
- You’ve **outgrown the resources of a single database instance**
- You want **higher availability** and fault isolation
- You need to **scale writes** horizontally
- You want to **distribute data across regions**
- You require **tenant-level isolation**

### When Table Partitioning is Sufficient

Partitioning is ideal when:
- Data is large but fits on a **single server**
- You're performing **OLAP or analytical queries**
- You want **simpler maintenance** and performance via **partition pruning**

### Summary

Use partitioning for **performance tuning within one DB**; use sharding for **scaling across databases and infrastructure**.



## Architecture

### Sharding Configuration

The shard metadata is defined in `sharding-metadata.yml`:

```yaml
sharding:
  shards:
    - name: shard-a
      range: { start: 1, end: 25 }
      host: localhost
      port: 5433
      database: shard_a
      credentials: { username: user, password: pass }
    ...
```

`application.yaml` imports the shard configuration:

```yaml
spring:
  config:
    import: classpath:sharding-metadata.yml
```

### Key Components

| Component | Description |
|----------|-------------|
| `ShardProperties`, `ShardRange` | Models to load and represent shard metadata |
| `ShardHelper` | Resolves the correct shard for a given `userId` |
| `ShardRoutingDataSource` | Dynamic DataSource that routes requests to the right shard |
| `@ShardKeyAware` & `ShardKeyAspect` | AOP to inject shard key context |
| `UserService`, `UserController` | Service and REST controller to manage `User` data |
| `UserRepository` | Spring Data repository using JPA |
| `DataSourceConfig` | Configures the dynamic sharded DataSource |
| `docker-compose.yaml` | Spins up four PostgreSQL containers, one for each shard |

## How it Works

1. **User Request** hits `POST /users` or `GET /users/{userId}`
2. AOP logic intercepts the method call (via `@ShardKeyAware`)
3. Shard key (`userId`) is resolved via `ShardHelper`
4. The current thread is bound to the correct `DataSource`
5. Repository call is executed against the selected shard
6. Shard key context is cleared after execution

## Running the Project

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle

### Run Shards via Docker

```bash
docker-compose up -d
```

### Start Application

```bash
./gradlew bootRun
```

### Sample API

```http
POST /users
Content-Type: application/json

{
  "userId": 33,
  "username": "john",
  "email": "john@example.com"
}
```

### Project Structure

```
src/
├── main/java/com/learning/db/shard
│   ├── aop/annotations/...
│   ├── config/DataSourceConfig.java
│   ├── metadata/ShardHelper.java
│   ├── props/ShardProperties.java
│   ├── routing/ShardRoutingDataSource.java
│   ├── service/UserService.java
│   └── web/controller/UserController.java
└── resources/
    ├── application.yaml
    └── sharding-metadata.yml
```

---
