package rshinna.orcacast.application;

import org.springframework.stereotype.Service;
import rshinna.orcacast.application.input.PersistTransactionInput;
import rshinna.orcacast.application.output.TransactionOutput;
import rshinna.orcacast.domain.Transaction;
import rshinna.orcacast.domain.TransactionRepository;

@Service
public class PersistTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public TransactionOutput execute(PersistTransactionInput input) {
        var transaction = transactionRepository.save(
                new Transaction(input.description(), input.amount(), input.category()));

        return TransactionOutput.from(transaction);

    }
}