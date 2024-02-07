package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ImageDb;

@Repository
public interface ImageDbRepository extends JpaRepository<ImageDb, String> {

	@Query(value = "SELECT * FROM image_db i WHERE i.phone = :phone", nativeQuery = true)
	List<ImageDb> findAllByPhone(@Param("phone") String phone);

}
