package com.df.fne.jpa.repositories;

import com.df.fne.core.domaines.TvaDto;
import com.df.fne.jpa.entities.Tva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvaRepository extends JpaRepository<Tva, Long> {
}
