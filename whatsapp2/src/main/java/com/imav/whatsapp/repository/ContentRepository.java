package com.imav.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imav.whatsapp.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
