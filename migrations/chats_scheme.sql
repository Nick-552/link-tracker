--liquibase formatted sql

--changeset Nick-552:1
CREATE TABLE IF NOT EXISTS chats
(
    id BIGINT PRIMARY KEY NOT NULL
)
--rollback drop table chats
