package com.transporte.model.enums;

import java.math.BigDecimal;

public enum TipoPassageiro {
    ADULTO("1.00"),
    ESTUDANTE("0.80"),
    IDOSO("0.50"),
    CRIANCA("0.50");

    private final BigDecimal fator;

    TipoPassageiro(String fator) {
        this.fator = new BigDecimal(fator);
    }

    /** Multiplicador aplicado ao preço base da rota. */
    public BigDecimal getFator() {
        return fator;
    }
}