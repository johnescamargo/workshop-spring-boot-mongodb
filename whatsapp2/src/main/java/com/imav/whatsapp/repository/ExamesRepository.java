package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.Exames;
import com.imav.whatsapp.entity.NotaFiscal;

@Repository
public interface ExamesRepository extends JpaRepository<Exames, Long> {

	List<Exames> findAllByNotaFiscal(NotaFiscal id);

}
