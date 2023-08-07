package com.imav.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	@Query(value = "SELECT * FROM customer c WHERE c.c_phone_number LIKE %:input% or c.c_name LIKE %:input% LIMIT 40", nativeQuery = true)
	List<Customer> liveSearch(@Param("input") String input);

	@Query(value = "SELECT * FROM customer c WHERE c.c_phone_number = :c_phone_number", nativeQuery = true)
	Customer findByPhoneNumber(@Param("c_phone_number") String c_phone_number);

	@Query(value = "SELECT * FROM customer c ORDER BY c.c_timestamp DESC LIMIT 150;", nativeQuery = true)
	List<Customer> findAllLimit150();

	@Query("select case when count(c)> 0 then true else false end from Customer c where lower(c.c_phone_number) like lower(:c_phone_number)")
	boolean existsByPhoneNumber(@Param("c_phone_number") String c_phone_number);
	
}
