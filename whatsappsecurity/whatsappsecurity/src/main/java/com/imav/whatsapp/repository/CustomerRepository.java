package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	@Query(value = "SELECT * FROM customer c WHERE c.c_phone_number ILIKE %:input% or c.c_name ILIKE %:input% LIMIT 40", nativeQuery = true)
	List<Customer> liveSearch(@Param("input") String input);

	@Query(value = "SELECT * FROM customer c WHERE c.c_phone_number = :c_phone_number", nativeQuery = true)
	Customer findAllByPhoneNumber(@Param("c_phone_number") String c_phone_number);
}
