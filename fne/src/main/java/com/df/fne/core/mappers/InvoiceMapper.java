package com.df.fne.core.mappers;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.jpa.entities.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceMapper {
    InvoiceDto toDto(Invoice invoice);
    Invoice toEntity(InvoiceDto invoiceDto);
    List<InvoiceDto> toDtoList(List<Invoice> invoices);
    List<Invoice> toEntityList(List<InvoiceDto> invoicesDto);
}
