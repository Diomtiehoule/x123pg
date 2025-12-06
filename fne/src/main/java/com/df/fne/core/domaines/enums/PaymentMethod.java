package com.df.fne.core.domaines.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    cash("cash"),
    mobile_money("mobile-money"),
    card("card"),
    check("check"),
    deferred("deferred"),
    transfer("transfer");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentMethod fromValue(String value) {
        if (value == null) return null;
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.value.equalsIgnoreCase(value) || pm.name().equalsIgnoreCase(value.replace("-", "_"))) {
                return pm;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + value);
    }
}



