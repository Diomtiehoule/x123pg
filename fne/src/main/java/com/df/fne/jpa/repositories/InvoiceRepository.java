package com.df.fne.jpa.repositories;

import com.df.fne.jpa.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    @Query("SELECT i FROM Invoice i WHERE " +
            "(:template IS NULL OR :template = '' OR LOWER(i.template) LIKE LOWER(CONCAT('%', :template, '%'))) " +
            "AND (:company IS NULL OR :company = '' OR LOWER(i.clientCompanyName) LIKE LOWER(CONCAT('%', :company, '%'))) " +
            "AND (:pos IS NULL OR :pos = '' OR LOWER(i.pointOfSale) LIKE LOWER(CONCAT('%', :pos, '%'))) ")
    List<Invoice> invoiceFilter(
            @Param("template") String template,
            @Param("company") String clientCompanyName,
            @Param("pos") String pointOfSale
    );


}
