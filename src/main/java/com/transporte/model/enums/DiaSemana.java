package com.transporte.model.enums;

import java.time.DayOfWeek;

public enum DiaSemana {
    SEG, TER, QUA, QUI, SEX, SAB, DOM;

    public static DiaSemana de(DayOfWeek d) {
        return values()[d.getValue() - 1];
    }
}