package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BusinessUnits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessUnits extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String code;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "businessUnits", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "businessUnits", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "businessUnits", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> audits = new ArrayList<>();

}
