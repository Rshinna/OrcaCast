package rshinna.orcacast.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import rshinna.orcacast.application.input.PersistTransactionInput;
import rshinna.orcacast.domain.Category;

public record CreateTransactionRequest(
        @NotBlank
        String description,
        @Positive long amount,
        @NotNull Category category
) {
    public PersistTransactionInput toInput() {
        return new PersistTransactionInput(description, amount, category);
    }
}
