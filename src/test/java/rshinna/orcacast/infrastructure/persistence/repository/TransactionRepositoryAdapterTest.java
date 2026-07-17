package rshinna.orcacast.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.domain.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TransactionRepositoryAdapter.class)
class TransactionRepositoryAdapterTest {


    private final TransactionRepositoryAdapter transactionRepositoryAdapter;

    @Autowired
    TransactionRepositoryAdapterTest(TransactionRepositoryAdapter transactionRepositoryAdapter) {
        this.transactionRepositoryAdapter = transactionRepositoryAdapter;
    }

    @Test
    void deveSalvarEBuscarTransacaoPorCategoria() {

        var transaction = new Transaction("farmácia", 3000, Category.PHARMA);

        transactionRepositoryAdapter.save(transaction);

        var result = transactionRepositoryAdapter.findAllByCategory(Category.PHARMA);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("farmácia");
    }
}