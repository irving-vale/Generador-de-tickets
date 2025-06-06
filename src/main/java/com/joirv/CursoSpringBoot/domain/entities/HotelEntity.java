package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "hotel")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class HotelEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	private String name;
	@Column(length = 50)
	private String address;
	private Integer rating;
	private BigDecimal price;

	@OneToMany(
	        mappedBy = "hotel",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	        fetch = FetchType.LAZY
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<ReservationEntity> reservations;
}
