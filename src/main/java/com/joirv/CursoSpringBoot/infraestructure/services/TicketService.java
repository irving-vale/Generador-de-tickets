package com.joirv.CursoSpringBoot.infraestructure.services;


import com.joirv.CursoSpringBoot.api.models.request.TicketRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TicketResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.mappers.TicketMapper;
import com.joirv.CursoSpringBoot.domain.repositories.CustomerRepository;
import com.joirv.CursoSpringBoot.domain.repositories.FlyRepository;
import com.joirv.CursoSpringBoot.domain.repositories.TicketRepository;
import com.joirv.CursoSpringBoot.infraestructure.abstract_services.ITicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final TicketMapper ticketMapper;


    @Override
    public ApiResponseDto<TicketResponseDto> create(TicketRequestDto request) {
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new EntityNotFoundException("Fly not found"));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        var ticket = TicketEntity.builder().id(UUID.randomUUID()).fly(fly).customer(customer).price(fly.getPrice().multiply(BigDecimal.valueOf(0.25))).purchaseDate(LocalDate.now()).arrivalDate(LocalDateTime.now()).departureDate(LocalDateTime.now()).build();
        log.info("Ticket created: {}", ticket);
        ticketRepository.save(ticket);

        TicketResponseDto ticketToDto = ticketMapper.toTicketResponseDto(ticket);
        return ApiResponseDto.<TicketResponseDto>builder().status("success").message("Ticket created successfully").statusCode(200).data(ticketToDto).meta(null).build();
    }

//	@Override
//	public TicketResponseDto read(UUID uuid) {
//		return ticketRepository.findById(uuid)
//		        .map(ticket ->TicketResponseDto.builder()
//			      .id(ticket.getId())
//			      .departureDate(ticket.getDepartureDate())
//			      .arrivalDate(ticket.getArrivalDate())
//			      .purchaseDate(ticket.getPurchaseDate())
//			      .price(ticket.getPrice())
//			      .fly(FlyResponseDto.builder()
//			        .id(ticket.getFly().getId())
//			        .originLat(ticket.getFly().getOriginLat())
//			        .originLng(ticket.getFly().getOriginLng())
//			        .destinyLat(ticket.getFly().getDestinyLat())
//			        .destinyLng(ticket.getFly().getDestinyLng())
//			        .price(ticket.getFly().getPrice())
//			        .originName(ticket.getFly().getOriginName())
//			        .destinyName(ticket.getFly().getDestinyName())
//			        .aeroLine(ticket.getFly().getAeroLine())
//			        .build())
//		        .build())
//		        .orElseThrow(() -> new RuntimeException("Ticket not found"));
//
//
//
//
//	}


    /**
     * Esto es lo que se hace con MapStruct
     **/
    @Override
    public ApiResponseDto<TicketResponseDto> read(UUID uuid) {
        var ticket = ticketRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        var ticketResponseDto = ticketMapper.toTicketResponseDto(ticket);
        return ApiResponseDto.<TicketResponseDto>builder().status("success").message("Ticket found successfully").statusCode(200).data(ticketResponseDto).meta(null).build();
    }

    @Override
    public ApiResponseDto<TicketResponseDto> update(UUID uuid, TicketRequestDto request) {
        TicketEntity ticketEntity = ticketRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        var flyUpdate = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new EntityNotFoundException("Fly not found"));

        var customerUpdate = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        var TicketUpdate = TicketEntity.builder().id(ticketEntity.getId()).fly(flyUpdate).customer(customerUpdate).price(flyUpdate.getPrice().multiply(BigDecimal.valueOf(0.25))).purchaseDate(LocalDate.now()).arrivalDate(LocalDateTime.now()).departureDate(LocalDateTime.now()).build();
        log.info("Ticket updated: {}", TicketUpdate);
        ticketRepository.save(TicketUpdate);
        var ticketToDto = ticketMapper.toTicketResponseDto(TicketUpdate);
        return ApiResponseDto.<TicketResponseDto>builder().status("success").message("Ticket updated successfully").statusCode(200).data(ticketToDto).meta(null).build();
    }

    @Override
    public ApiResponseDto<Void> delete(UUID uuid) {
        TicketEntity ticketEntity = ticketRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticketRepository.delete(ticketEntity);
        log.info("Ticket deleted: {}", ticketEntity);
        return ApiResponseDto.<Void>builder().status("success").message("Ticket deleted successfully").statusCode(200).data(null).meta(null).build();
    }


    @Override
    public ApiResponseDto<BigDecimal> flyByPrice(Long idFly) {
        var fly = flyRepository.findById(idFly).orElseThrow(() -> new EntityNotFoundException("Fly not found"));
        var extraPrice = fly.getPrice().multiply(BigDecimal.valueOf(0.25));
        var finalPrice = fly.getPrice().add(extraPrice);
        return ApiResponseDto.<BigDecimal>builder().status("success").message("Fly found successfully").statusCode(200).data(finalPrice).meta(null).build();


    }
}
