package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "AuditLogs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessUnits_id")
    private BusinessUnits businessUnits;

    private String action;
    @Column(columnDefinition = "TEXT")
    private String details;

}

