package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.NotaFiscal;

@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

	@Query(value = "SELECT * FROM notafiscal n WHERE n.nf_done = false", nativeQuery = true)
	List<NotaFiscal> findAllByNfNotDone();

	@Query(value = "SELECT * FROM notafiscal n WHERE n.telefone LIKE %:input% or n.nome LIKE %:input% or n.cpf LIKE %:input% LIMIT 30", nativeQuery = true)
	List<NotaFiscal> findLiveSearch(@Param("input") String input);

	@Query(value = "SELECT * FROM notafiscal n WHERE n.timestamp_created >= :begindate AND n.timestamp_created <= :enddate", nativeQuery = true)
	List<NotaFiscal> findAllByTimestampCreated(@Param("begindate") long begindate, @Param("enddate") long enddate);

}
