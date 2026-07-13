package rshinna.orcacast.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.infrastructure.persistence.entity.TransactionEntity;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByCategory(Category category);
}
