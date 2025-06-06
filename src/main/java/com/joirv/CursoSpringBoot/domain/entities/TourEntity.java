package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tour")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TourEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;
	@OneToMany(mappedBy = "tour",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<ReservationEntity> reservations;
	@OneToMany(mappedBy = "tour",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<TicketEntity> tickets;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer")
	private CustomerEntity customer;

	@PrePersist
	@PreUpdate
	@PreRemove
	public void updateFk() {
		if (tickets != null) {
			tickets.forEach(ticket -> ticket.setTour(this));
		}
		if (reservations != null) {
			reservations.forEach(reservation -> reservation.setTour(this));
		}
	}

	public void addTicket(TicketEntity ticket) {
		if (Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
		this.tickets.add(ticket);
		ticket.setTour(this);
	}




	public void removeTicket(UUID id) {
		if (tickets == null) return;
		tickets.removeIf(ticket -> {
			if (ticket.getId().equals(id)) {
				ticket.setTour(null);
				return true; // lo elimina de la colección
			}
			return false;
		});
	}

	public void addReservation(ReservationEntity reservation) {
		if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
		this.reservations.add(reservation);
		reservation.setTour(this);
	}

	public void removeReservation(UUID id) {
		if (reservations == null) return;
		reservations.removeIf(reservation -> {
			if (reservation.getId().equals(id)) {
				reservation.setTour(null);
				return true; // lo elimina de la colección
			}
			return false;
		});
	}



//	public void addTicket(TicketEntity ticket) {
//		if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
//		this.tickets.add(ticket);
//		ticket.setTour(this);
//	}
//
//	public void removeTicket(UUID id) {
//		if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
//		this.tickets.removeIf(ticket -> ticket.getId().equals(id));
//	}
//
//	public void updateTicket(){
//		this.tickets.forEach(ticket ->ticket.setTour(this));
//	}
//
//	public void addReservation(ReservationEntity reservation) {
//		if(Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
//			this.reservations.add(reservation);
//			reservation.setTour(this);
//
//
//	}
//	public void removeReservation(UUID idReservation) {
//		if(Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
//		this.reservations.removeIf(reservation -> reservation.getId().equals(id));
//	}
//	public void updateReservation(){
//		this.reservations.forEach(reservation ->reservation.setTour(this));
//	}

}


