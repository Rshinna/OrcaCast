package rshinna.orcacast.application.input;

import rshinna.orcacast.domain.Category;

public record PersistTransactionInput(
        String description,
        long amount,
        Category category
) {
}
