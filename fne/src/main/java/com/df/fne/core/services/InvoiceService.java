package com.df.fne.core.services;

import com.df.fne.core.domaines.InvoiceDto;

import java.util.List;

public interface InvoiceService extends BaseService<InvoiceDto>{
    List<InvoiceDto> filterInvoice(String template, String company, String pos);
}
