package com.df.fne.jpa.repositories;

import com.df.fne.jpa.entities.InvoiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineInvoiceRepository extends JpaRepository<InvoiceItems,Long> {
}
