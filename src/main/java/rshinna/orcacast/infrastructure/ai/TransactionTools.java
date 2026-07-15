package rshinna.orcacast.infrastructure.ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import rshinna.orcacast.application.ListTransactionByCategoryUseCase;
import rshinna.orcacast.application.PersistTransactionUseCase;
import rshinna.orcacast.application.input.PersistTransactionInput;
import rshinna.orcacast.application.output.TransactionOutput;
import rshinna.orcacast.domain.Category;

import java.util.List;

@Component
public class TransactionTools {

    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;

    public TransactionTools(PersistTransactionUseCase persistTransactionUseCase, ListTransactionByCategoryUseCase listTransactionByCategoryUseCase) {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
    }

    @Tool(description = "Registra uma nova transação financeira quando o usuário relata um gasto ou compra, como 'gastei 50 reais no mercado'.")
    public TransactionOutput createTransaction(
            @ToolParam(description = "Registra a descrição da transação finaceira.") String description,
            @ToolParam(description = "Recebe o valor da transação financeira em centavos.") long amountCents,
            @ToolParam(description = "Categoria da transação: GROCERIES para mercado/supermercado, PHARMA para farmácia/remédios, AUTO para gastos com veículo/combustível.") Category category){
        var input = new PersistTransactionInput(description, amountCents, category);
        return persistTransactionUseCase.execute(input);
    }

    @Tool(description = "Lista as transações já  registradas filtradas por categoria, quando o usuário perguntar sobre seus gastos em determinada categoria.")
    public List<TransactionOutput> listTransactionsByCategory(@ToolParam(description = "Categoria da transação: GROCERIES para mercado/supermercado, PHARMA para farmácia/remédios, AUTO para gastos com veículo/combustível.") Category category){
        return listTransactionByCategoryUseCase.execute(category);
    }
}
