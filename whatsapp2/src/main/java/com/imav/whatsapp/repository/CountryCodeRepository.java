package com.imav.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imav.whatsapp.entity.CountryCode;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

	boolean existsByCode(@Param("code") int code);

}
