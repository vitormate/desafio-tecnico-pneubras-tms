package com.pneubras.tms.utils.enums;

public enum PriorityEnum {

    BAIXA(72),
    MEDIA(48),
    ALTA(24),
    CRITICA(8);

    private final int hours;

    PriorityEnum(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }
}
