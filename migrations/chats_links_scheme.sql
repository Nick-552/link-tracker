--liquibase formatted sql

--changeset Nick-552:1
CREATE TABLE IF NOT EXISTS chats_links
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    link_id         BIGINT           REFERENCES links (id) NOT NULL,
    chat_id         BIGINT           REFERENCES chats (id) NOT NULL
    CONSTRAINT unique_chats_links UNIQUE CHECK (link_id <> chat_id)
)
--rollback drop table chats_links
