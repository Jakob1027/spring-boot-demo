package com.jakob.kafka;

import lombok.Getter;

@Getter
public class Customer {
    private final int customerId;
    private final String customerName;

    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }
}
