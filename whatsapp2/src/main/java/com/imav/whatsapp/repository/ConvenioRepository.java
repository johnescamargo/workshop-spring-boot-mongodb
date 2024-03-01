package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imav.whatsapp.entity.Convenio;

public interface ConvenioRepository extends JpaRepository<Convenio, Long>{

	@Query(value = "SELECT * FROM convenio c WHERE c.name LIKE %:input%", nativeQuery = true)
	List<Convenio> livesearch(@Param("input") String input);

}
