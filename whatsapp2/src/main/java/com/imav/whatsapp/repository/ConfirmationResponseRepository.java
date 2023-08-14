package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.ConfirmationResponse;

@Repository
public interface ConfirmationResponseRepository extends JpaRepository<ConfirmationResponse, String> {

	@Query(value = "SELECT * FROM confirmation_response c WHERE c.phone_number LIKE %:input% or c.name LIKE %:input% LIMIT 50", nativeQuery = true)
	List<ConfirmationResponse> findByPhoneNumberOrName(@Param("input") String input);	

	List<ConfirmationResponse> findByShippingDate(@Param("shipping_date") String shippingDate);
	
	@Query(value = "SELECT * FROM confirmation_response u WHERE u.id_wamid = :id_wamid", nativeQuery = true)
	ConfirmationResponse findById_Wamid(@Param("id_wamid") String id_wamid);
	
	Boolean existsByIdWamid(@Param("id_wamid") String id_wamid);

	List<ConfirmationResponse> findByServiceDate(String service_date);

	ConfirmationResponse findByIdWamid(String idWamid);
}
