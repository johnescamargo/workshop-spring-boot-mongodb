package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.imav.whatsapp.entity.Plano;

public interface PlanoRepository extends JpaRepository<Plano, Long>{

	@Query(value = "SELECT * FROM plano p WHERE p.convenio_id= :id AND p.text LIKE %:input%", nativeQuery = true)
	List<Plano> liveSearch(@Param("id") String id, @Param("input") String input);
	
}
