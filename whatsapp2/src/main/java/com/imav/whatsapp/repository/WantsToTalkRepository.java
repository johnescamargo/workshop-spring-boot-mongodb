package com.imav.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imav.whatsapp.entity.WantsToTalk;

import jakarta.transaction.Transactional;

public interface WantsToTalkRepository extends JpaRepository<WantsToTalk, String> {

	@Transactional
	void deleteByName(String name);
	
	@Transactional
	void deleteByPhone(String phone);

	boolean existsByPhone(String phone);
}
