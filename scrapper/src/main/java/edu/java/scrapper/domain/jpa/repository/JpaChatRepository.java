package edu.java.scrapper.domain.jpa.repository;

import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
}
