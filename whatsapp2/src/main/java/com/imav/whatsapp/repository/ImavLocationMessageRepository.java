package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ImavLocationMessage;

@Repository
public interface ImavLocationMessageRepository extends JpaRepository<ImavLocationMessage, Long> {

	@Query(value = "SELECT * FROM imav_locationmessage o WHERE o.l_to = :l_to", nativeQuery = true)
	List<ImavLocationMessage> findAllByPhone_to(@Param("l_to") String l_to);

	@Query(value = "SELECT * FROM imav_locationmessage o WHERE o.l_to = :l_to AND o.id_wamid = :id_wamid", nativeQuery = true)
	ImavLocationMessage findByPhoneAndIdWamid(@Param("l_to") String l_to, @Param("id_wamid") String idWamid);

	Boolean existsByIdWamid(@Param("id_wamid") String id_wamid);

	ImavLocationMessage findByIdWamid(@Param("id_wamid") String idWamid);

	@Query(value = "SELECT COUNT(l_to) FROM imav_locationmessage c WHERE c.l_to = :l_to", nativeQuery = true)
	Integer countByL_to(@Param("l_to") String l_to);

}
