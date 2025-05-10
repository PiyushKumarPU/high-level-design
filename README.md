# High-Level Design (HLD) Repository

This repository is a curated collection of **High-Level Design (HLD) documents** along with their respective **production-grade implementations**. It serves as a reference for designing scalable, maintainable, and robust software systems.

## 📘 Overview

This repo captures HLDs for real-world system features and components. Each HLD is structured to include:

- Problem statement and scope
- Functional and non-functional requirements
- System architecture diagrams
- Component breakdown
- Design decisions and trade-offs
- Technology choices
- API contracts (if applicable)
- Implementation details (with source code)

## 📂 Repository Structure

```
/high-level-design/
├── README.md
├── <feature-1>/
│   ├── hld.md
│   ├── diagrams/
│   └── implementation/ (Spring Boot / Java / Kafka / etc.)
├── <feature-2>/
│   ├── hld.md
│   ├── diagrams/
│   └── implementation/
...
```

## 🚀 Features Covered

The goal is to cover a broad range of real-world backend and system design problems such as:

- Notification Services
- Rate Limiter
- URL Shortener
- Payment Gateway Integration
- Order Management System (OMS)
- File Upload Service (with CDN)
- [Database Sharding](/postgresql_databse_sharding_example/)
- CQRS and Event Sourcing
- Microservice Communication (Sync + Async)
- Saga Pattern (Orchestration & Choreography)
- API Gateway
- Distributed Caching
- Retry Mechanisms with Exponential Backoff

> More features will be added regularly with complete documentation and implementation.

## 🛠️ Tech Stack (per use case)

- **Backend**: Java, Spring Boot, Spring Cloud, Kafka, Redis
- **Databases**: PostgreSQL, MongoDB
- **Message Brokers**: Kafka, RabbitMQ
- **Deployment**: Docker, Docker Compose
- **CI/CD**: GitHub Actions (planned)

## 🎯 Purpose

This repository is built to:

- Help with **System Design interview preparation**
- Demonstrate **best practices** for production-level design and implementation
- Serve as a **learning playground** for developers and architects

## 🤝 Contribution

Contributions, ideas, and suggestions are welcome. Feel free to open issues or submit PRs for new HLDs or enhancements to existing ones.

---

**Let's build scalable systems together.**
