package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.CustomerMessage;

@Repository
public interface CustomerMessageRepository extends JpaRepository<CustomerMessage, String> {

	@Query(value = "SELECT * FROM customer_message u WHERE u.m_phone_from = :m_phone_from", nativeQuery = true)
	List<CustomerMessage> findAllByPhone_from(@Param("m_phone_from") String m_phone_from);

}
