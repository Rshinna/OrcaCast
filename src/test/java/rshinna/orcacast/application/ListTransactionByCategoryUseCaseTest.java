package rshinna.orcacast.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.domain.Transaction;
import rshinna.orcacast.domain.TransactionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTransactionByCategoryUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void deveListarTransacoesPorCategoria() {

        var useCase = new ListTransactionByCategoryUseCase(transactionRepository);
        var transacaoExistente = new Transaction("mercado", 5000, Category.GROCERIES);

        when(transactionRepository.findAllByCategory(Category.GROCERIES)).thenReturn(List.of(transacaoExistente));

        var result = useCase.execute(Category.GROCERIES);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).description()).isEqualTo("mercado");
        verify(transactionRepository).findAllByCategory(Category.GROCERIES);
    }
}