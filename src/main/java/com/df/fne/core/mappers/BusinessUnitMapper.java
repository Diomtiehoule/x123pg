package com.df.fne.core.mappers;

import com.df.fne.core.domaines.BusinessUnitsDto;
import com.df.fne.jpa.entities.BusinessUnits;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {ClientMapper.class})
public interface BusinessUnitMapper {
    BusinessUnitsDto toDto(BusinessUnits businessUnits);
    BusinessUnits toEntity(BusinessUnitsDto businessUnitsDto);
    List<BusinessUnitsDto> toDtoList(List<BusinessUnits> businessUnitsList);
    List<BusinessUnits> toEntityList(List<BusinessUnitsDto> businessUnitsDtoList);
}
