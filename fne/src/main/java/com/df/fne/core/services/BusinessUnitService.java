package com.df.fne.core.services;

import com.df.fne.core.domaines.BusinessUnitsDto;

import java.util.List;

public interface BusinessUnitService{
    BusinessUnitsDto create(BusinessUnitsDto businessUnitsDto);
    BusinessUnitsDto update(BusinessUnitsDto businessUnitsDto, Long id);
    BusinessUnitsDto get(Long id);
    List<BusinessUnitsDto> getAll();
    void delete(Long id);
}
