package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.InvoiceTemplate;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String reference;
    @Column(unique = true)
    private String nameReasonSocial;
    @Column(unique = true)
    private String taxPayerAccountNumber;
    @Enumerated(EnumType.STRING)
    private InvoiceTemplate clientType;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String address;
}
