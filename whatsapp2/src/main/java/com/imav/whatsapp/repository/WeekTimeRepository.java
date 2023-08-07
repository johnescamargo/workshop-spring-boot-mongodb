package com.imav.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imav.whatsapp.entity.WeekTime;

import jakarta.transaction.Transactional;

public interface WeekTimeRepository extends JpaRepository<WeekTime, Long> {

	@Transactional
	void deleteByDay(Integer day);

	boolean existsByDay(int code);

	WeekTime getByDay(int day);

}
