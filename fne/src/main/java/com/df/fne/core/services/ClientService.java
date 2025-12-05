package com.df.fne.core.services;

import com.df.fne.core.domaines.ClientDto;
import com.df.fne.jpa.entities.Client;

import java.util.Optional;

public interface ClientService extends BaseService<ClientDto> {
    ClientDto findByNameReasonSocial(String nameReasonSocial);
}
