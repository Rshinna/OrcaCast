package rshinna.orcacast.application;

import org.springframework.stereotype.Service;
import rshinna.orcacast.domain.TransactionRepository;

@Service
public class ListTransactionByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public ListTransactionByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


}
