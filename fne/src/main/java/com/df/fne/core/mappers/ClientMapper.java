package com.df.fne.core.mappers;

import com.df.fne.core.domaines.ClientDto;
import com.df.fne.jpa.entities.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {BusinessUnitMapper.class})
public interface ClientMapper {
    ClientDto toDto(Client client);
    Client toEntity(ClientDto clientDto);
    List<ClientDto> toDtoList(List<Client> clients);
    List<Client> toEntityList(List<ClientDto> clients);
}
