package com.df.fne.jpa.repositories;

import com.df.fne.jpa.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByNameReasonSocial(String nameReasonSocial);
    @Query("SELECT c FROM Client c WHERE c.nameReasonSocial = :name OR c.email = :email OR c.phone = :phone")
    Client findOneByUniqueFields(@Param("name") String name, @Param("email") String email, @Param("phone") String phone);

}
