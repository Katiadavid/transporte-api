package com.transporte.model.enums;

public enum StatusViagem {
    AGENDADA, EM_CURSO, CONCLUIDA, CANCELADA;

    /** AGENDADA → EM_CURSO → CONCLUIDA; AGENDADA/EM_CURSO → CANCELADA. */
    public boolean podeIrPara(StatusViagem novo) {
        return switch (this) {
            case AGENDADA -> novo == EM_CURSO || novo == CANCELADA;
            case EM_CURSO -> novo == CONCLUIDA || novo == CANCELADA;
            default -> false;
        };
    }
}