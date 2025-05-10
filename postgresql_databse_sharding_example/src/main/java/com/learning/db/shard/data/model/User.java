package com.learning.db.shard.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long userId;
    private String username;
    private String email;
    private String bio;
    private String profilePic;
    private LocalDateTime createdAt = LocalDateTime.now();
}
