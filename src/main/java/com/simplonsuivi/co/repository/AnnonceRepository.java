package com.simplonsuivi.co.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplonsuivi.co.domain.Annonce;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

}
