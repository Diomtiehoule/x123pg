package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class BusinessUnitsDto extends UserDateAudit {
    private Long id;
    private String libelle;
    private String code;
    private String description;
}
