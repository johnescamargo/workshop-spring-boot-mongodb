package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ImavMessage;

@Repository
public interface ImavMessageRepository extends JpaRepository<ImavMessage, Long> {

	@Query(value = "SELECT * FROM imav_message o WHERE o.m_to = :m_to", nativeQuery = true)
	List<ImavMessage> findAllByPhone_to(@Param("m_to") String m_to);

	@Query(value = "SELECT * FROM imav_message o WHERE o.m_to = :m_to and o.id_wamid = :id_wamid", nativeQuery = true)
	ImavMessage findByPhoneAndIdWamid(@Param("m_to") String m_to, @Param("id_wamid") String idWamid);

}
