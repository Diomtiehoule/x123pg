package com.df.fne.core.mappers;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.jpa.entities.Invoice;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, InvoiceItemsMapper.class, NotificationMapper.class, BusinessUnitMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapper {
    InvoiceDto toDto(Invoice invoice);
    Invoice toEntity(InvoiceDto invoiceDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInvoiceFromDto(InvoiceDto dto, @MappingTarget Invoice entity);
    List<InvoiceDto> toDtoList(List<Invoice> invoices);
    List<Invoice> toEntityList(List<InvoiceDto> invoicesDto);
}
