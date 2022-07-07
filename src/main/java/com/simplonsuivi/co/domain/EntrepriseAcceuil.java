package com.simplonsuivi.co.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
public class EntrepriseAcceuil {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String raisonSocial;
	private String prenomPersonneContact;
	private String nomPersonneContat;
	@ManyToMany
	@JoinTable(joinColumns = @JoinColumn(name="entreprise_id"),
	           inverseJoinColumns = @JoinColumn(name = "user_id"),
	           name="apprenant_entreprise")
	private Collection<User> users;

}
