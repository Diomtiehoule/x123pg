package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.ClientDto;
import com.df.fne.core.exceptions.BadRequestException;
import com.df.fne.core.exceptions.NotFoundException;
import com.df.fne.core.mappers.ClientMapper;
import com.df.fne.core.services.ClientService;
import com.df.fne.jpa.entities.Client;
import com.df.fne.jpa.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository , ClientMapper clientMapper){
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDto create(ClientDto clientDto) {

        Client existing = clientRepository.findOneByUniqueFields(
                clientDto.getNameReasonSocial(),
                clientDto.getEmail(),
                clientDto.getPhone()
        );

        if (existing != null) {

            if (existing.getNameReasonSocial().equals(clientDto.getNameReasonSocial())) {
                throw new BadRequestException("Client or ReasonSocial already exist");
            }

            if (existing.getEmail().equals(clientDto.getEmail())) {
                throw new BadRequestException("Email already exist");
            }

            if (existing.getPhone().equals(clientDto.getPhone())) {
                throw new BadRequestException("Phone already exist");
            }
        }

        Client client = clientRepository.save(clientMapper.toEntity(clientDto));
        return clientMapper.toDto(client);
    }



    @Override
    public ClientDto update(ClientDto clientDto, UUID id) {
        clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client not found"));
        Client ClientUpdated = clientRepository.saveAndFlush(clientMapper.toEntity(clientDto));
        return clientMapper.toDto(ClientUpdated);
    }

    @Override
    public ClientDto get(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        return clientMapper.toDto(client);
    }

    @Override
    public List<ClientDto> getAll() {
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDtoList(clients);
    }

    @Override
    public void delete(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        clientRepository.deleteById(client.getId());
    }

    @Override
    public ClientDto findByNameReasonSocial(String nameReasonSocial) {
        Client client = clientRepository.findByNameReasonSocial(nameReasonSocial);
        if(client == null){
            throw  new NotFoundException("Client not found");
        }

        return clientMapper.toDto(client);
    }
}
