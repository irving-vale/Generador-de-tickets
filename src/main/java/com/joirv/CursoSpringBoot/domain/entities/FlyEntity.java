package com.joirv.CursoSpringBoot.domain.entities;

import com.joirv.CursoSpringBoot.util.AeroLinea;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "fly")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class FlyEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double originLat;
	private Double originLng;
	private Double destinyLat;
	private Double destinyLng;
	private BigDecimal price;
	@Column(length = 20)
	private String originName;
	@Column(length = 20)
	private String destinyName;
	@Enumerated(EnumType.STRING)
	private AeroLinea aeroLine;
	@OneToMany(mappedBy = "fly",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true,
	fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<TicketEntity>tickets;

}
