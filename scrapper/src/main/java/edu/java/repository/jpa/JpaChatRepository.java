package edu.java.repository.jpa;

import edu.java.domain.jpa.entity.TgChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<TgChatEntity, Long> {
}
