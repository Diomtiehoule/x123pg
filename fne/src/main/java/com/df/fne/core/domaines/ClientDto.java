package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.InvoiceTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class ClientDto extends UserDateAudit {
    private UUID id;
    private String reference;
    private String nameReasonSocial;
    private String taxPayerAccountNumber;
    private InvoiceTemplate clientType;
    private String phone;
    private String email;
    private String address;
}
