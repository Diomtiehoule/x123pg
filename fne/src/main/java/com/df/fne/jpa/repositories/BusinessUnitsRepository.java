package com.df.fne.jpa.repositories;

import com.df.fne.jpa.entities.BusinessUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitsRepository extends JpaRepository<BusinessUnits , Long> {
}
