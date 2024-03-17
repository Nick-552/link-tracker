--liquibase formatted sql

--changeset Nick-552:1
CREATE TABLE IF NOT EXISTS links
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    url             TEXT                            UNIQUE NOT NULL,
    last_updated_at TIMESTAMP               WITH TIME ZONE NOT NULL,
    last_check_at   TIMESTAMP               WITH TIME ZONE NOT NULL
)
--rollback drop table links
