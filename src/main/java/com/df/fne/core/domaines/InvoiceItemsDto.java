package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
@Getter
@Setter
public class InvoiceItemsDto extends UserDateAudit {

    private String itemRef;
    private String description;
    private int quantity;
    private int unitPrice;
    private int discountAmount;
    private String measureUnit;

    private List<String> taxes = new ArrayList<>();
    private List<CustomTaxDto> customTaxes = new ArrayList<>();
    private int amount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomTaxDto {
        private String name;
        private int amount;
    }
}
