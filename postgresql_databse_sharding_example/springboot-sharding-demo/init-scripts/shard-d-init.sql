CREATE TABLE IF NOT EXISTS users (
    user_id     BIGINT PRIMARY KEY,
    username    TEXT,
    email       TEXT,
    bio         TEXT,
    profile_pic TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
