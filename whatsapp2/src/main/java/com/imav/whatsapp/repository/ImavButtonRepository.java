package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ImavButton;

@Repository
public interface ImavButtonRepository extends JpaRepository<ImavButton, Long> {

	@Query(value = "SELECT * FROM imav_button u WHERE u.imav_buttonmessage_id = :imav_buttonmessage_id", nativeQuery = true)
	List<ImavButton> findAllByImavButtonMessageId(@Param("imav_buttonmessage_id") long imav_buttonmessage_id);

}
