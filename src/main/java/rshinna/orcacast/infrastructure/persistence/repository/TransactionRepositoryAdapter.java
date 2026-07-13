package rshinna.orcacast.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.domain.Transaction;
import rshinna.orcacast.domain.TransactionRepository;
import rshinna.orcacast.infrastructure.persistence.entity.TransactionEntity;

import java.util.List;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {
    private final TransactionJpaRepository transactionJpaRepository;

    public TransactionRepositoryAdapter(TransactionJpaRepository transactionJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = TransactionEntity.from(transaction);
        return transactionJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return transactionJpaRepository.findAllByCategory(category)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
