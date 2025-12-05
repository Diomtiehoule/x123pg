package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.BusinessUnitsDto;
import com.df.fne.core.exceptions.NotFoundException;
import com.df.fne.core.mappers.BusinessUnitMapper;
import com.df.fne.core.services.BusinessUnitService;
import com.df.fne.jpa.entities.BusinessUnits;
import com.df.fne.jpa.repositories.BusinessUnitsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final BusinessUnitsRepository businessUnitsRepository;
    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitServiceImpl(BusinessUnitsRepository businessUnitsRepository , BusinessUnitMapper businessUnitMapper){
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitsRepository = businessUnitsRepository;
    }

    @Override
    public BusinessUnitsDto create(BusinessUnitsDto businessUnitsDto) {
        BusinessUnits businessUnits = businessUnitsRepository.save(businessUnitMapper.toEntity(businessUnitsDto));
        return businessUnitMapper.toDto(businessUnits);
    }

    @Override
    public BusinessUnitsDto update(BusinessUnitsDto businessUnitsDto, Long id) {
        businessUnitsRepository.findById(id).orElseThrow(() -> new NotFoundException("BusinessUnit not found"));
        BusinessUnits businessUnitsUpdated = businessUnitsRepository.saveAndFlush(businessUnitMapper.toEntity(businessUnitsDto));
        return businessUnitMapper.toDto(businessUnitsUpdated);
    }

    @Override
    public BusinessUnitsDto get(Long id) {
        BusinessUnits businessUnits = businessUnitsRepository.findById(id).orElseThrow(()-> new NotFoundException("BusinessUnit not found"));
        return businessUnitMapper.toDto(businessUnits);
    }

    @Override
    public List<BusinessUnitsDto> getAll() {
        List<BusinessUnits> businessUnits = businessUnitsRepository.findAll();
        return businessUnitMapper.toDtoList(businessUnits);
    }

    @Override
    public void delete(Long id) {
        BusinessUnits businessUnits = businessUnitsRepository.findById(id).orElseThrow(()-> new NotFoundException("BusinessUnit not found"));
        businessUnitsRepository.deleteById(businessUnits.getId());
    }
}
