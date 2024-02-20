package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ImavButtonMessage;

@Repository
public interface ImavButtonMessageRepository extends JpaRepository<ImavButtonMessage, Long> {

	@Query(value = "SELECT * FROM imav_buttonmessage o WHERE o.b_to = :b_to", nativeQuery = true)
	List<ImavButtonMessage> findAllByPhone_to(@Param("b_to") String b_to);

	@Query(value = "SELECT * FROM imav_buttonmessage o WHERE o.b_to = :b_to AND o.id_wamid = :id_wamid", nativeQuery = true)
	ImavButtonMessage findByPhoneAndIdWamid(@Param("b_to") String b_to, @Param("id_wamid") String idWamid);

}
