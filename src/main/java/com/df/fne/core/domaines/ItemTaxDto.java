package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.TaxeCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class ItemTaxDto extends UserDateAudit {

    private Long id;
    private TaxeCategory taxeCategory;
    private String taxName;
    private int taxAmount;
    private double taxRate;
    private InvoiceItemsDto invoiceItems;
}
