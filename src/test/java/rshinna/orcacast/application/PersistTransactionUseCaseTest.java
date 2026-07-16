package rshinna.orcacast.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rshinna.orcacast.application.input.PersistTransactionInput;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.domain.TransactionRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void deveCriarESalvarUmaTransacao() {

        var useCase = new PersistTransactionUseCase(transactionRepository);

        when(transactionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var input = new PersistTransactionInput("mercado", 5000, Category.GROCERIES);
        var result = useCase.execute(input);

        assertThat(result.description())
                .isEqualTo("mercado");
        assertThat(result.value())
                .isEqualByComparingTo("50.00");
        assertThat(result.category())
                .isEqualTo(Category.GROCERIES.name());

        verify(transactionRepository).save(any());

    }
}