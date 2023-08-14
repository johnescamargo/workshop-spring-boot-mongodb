package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	@Query(value = "SELECT * FROM customer c WHERE c.phone_number LIKE %:input% or c.name LIKE %:input% LIMIT 40;", nativeQuery = true)
	List<Customer> liveSearch(@Param("input") String input);

	@Query(value = "SELECT * FROM customer c ORDER BY c.timestamp DESC LIMIT 150;", nativeQuery = true)
	List<Customer> findAllLimit150();

	//@Query(value = "SELECT * FROM customer c WHERE c.phone_number= :phone_number;", nativeQuery = true)
	Customer findByPhoneNumber(@Param("phone_number") String phone_number);

	boolean existsByPhoneNumber(@Param("phone_number") String phone_number);

}
