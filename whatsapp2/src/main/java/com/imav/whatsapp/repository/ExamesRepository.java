package com.imav.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.Exames;

@Repository
public interface ExamesRepository extends JpaRepository<Exames, Long> {

}
