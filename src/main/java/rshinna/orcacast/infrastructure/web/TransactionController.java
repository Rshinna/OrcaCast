package rshinna.orcacast.infrastructure.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rshinna.orcacast.application.ListTransactionByCategoryUseCase;
import rshinna.orcacast.application.PersistTransactionUseCase;
import rshinna.orcacast.application.output.TransactionOutput;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.infrastructure.web.dto.CreateTransactionRequest;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;


    public TransactionController(PersistTransactionUseCase persistTransactionUseCase, ListTransactionByCategoryUseCase listTransactionByCategoryUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionOutput create(@Valid @RequestBody CreateTransactionRequest request) {
        return persistTransactionUseCase.execute(request.toInput());
    }

    @GetMapping
    public List<TransactionOutput> listByCategory(@RequestParam Category category) {
        return listTransactionByCategoryUseCase.execute(category);
    }
}
