package rshinna.orcacast.application;

import org.springframework.stereotype.Service;
import rshinna.orcacast.application.output.TransactionOutput;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.domain.TransactionRepository;

import java.util.List;

@Service
public class ListTransactionByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public ListTransactionByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionOutput> execute(Category category) {
        return transactionRepository.findAllByCategory(category)
                .stream()
                .map(TransactionOutput::from)
                .toList();

    }
}
