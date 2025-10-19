package com.parttime.job.Application.projectmanagementservice.shippingmanagement.enumration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ShippingEnumration {
    READY_TO_PICK("ready_to_pick"),
    PICKING("picking"),
    MONEY_COLLECT_PICKING("money_collect_picking"),
    PICKED("picked"),
    STORING("storing"),
    TRANSPORTING("transporting"),
    SORTING("sorting"),
    DELIVERING("delivering"),
    MONEY_COLLECT_DELIVERING("money_collect_delivering"),
    DELIVERED("delivered"),
    DELIVERY_FAIL("delivery_fail"),
    WAITING_TO_RETURN("waiting_to_return"),
    RETURN("return"),
    RETURN_TRANSPORTING("return_transporting"),
    RETURN_SORTING("return_sorting");

    private final String value;

    ShippingEnumration(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ShippingEnumration fromValue(String value) {
        for (ShippingEnumration status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
