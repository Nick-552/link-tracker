--liquibase formatted sql

--changeset Nick-552:1
CREATE TABLE IF NOT EXISTS chats_links
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    chat_id         BIGINT           REFERENCES chats (id) NOT NULL,
    link_id         BIGINT           REFERENCES links (id) NOT NULL,
    CONSTRAINT chat_link_unique UNIQUE (link_id, chat_id)
)
--rollback drop table chats_links
