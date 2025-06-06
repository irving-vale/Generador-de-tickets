package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "customer")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class CustomerEntity implements Serializable {
	@Id
	private String dni;
	@Column(length = 50)
	private String fullName;
	@Column(length = 20)
	private String creditCard;
	@Column(length = 12)
	private String phoneNumber;
	private Integer totalFlights;
	private Integer totalLodgings;
	private Integer totalTours;
	@OneToMany(mappedBy = "customer",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<TicketEntity>tickets;

	@OneToMany(mappedBy = "customer",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<ReservationEntity> reservations;

	@OneToMany(mappedBy = "customer",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<TourEntity> tours;



}
