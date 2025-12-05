package com.df.fne.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvoiceItemsUtils {
    private String itemRef;
    private String description;
    private int quantity;
    private int unitPrice;
    private int discountAmount;
    private String measureUnit;


    private List<String> taxes;

    @JsonProperty("customTaxe")
    private List<CustomTax> customTaxe = new ArrayList<>();

    private int amount;

    @Data
    public static class CustomTax {
        private String name;
        private double amount;
    }
}