package com.simplonsuivi.co.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.simplonsuivi.co.domain.Promo;


@RepositoryRestController
public interface PromoRespository extends JpaRepository<Promo, Long> {

}
