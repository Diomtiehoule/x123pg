package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.jpa.entities.AuditLog;
import com.df.fne.jpa.entities.Client;
import com.df.fne.jpa.entities.Invoice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class BusinessUnitsDto extends UserDateAudit {
    private Long id;
    private String libelle;
    private String code;
    private String description;
    private List<Invoice> invoices = new ArrayList<>();
    private List<AuditLog> audits = new ArrayList<>();
}
