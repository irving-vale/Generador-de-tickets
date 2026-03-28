package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.exceptions.personalExceptions.CustomerCounterException;
import com.joirv.CursoSpringBoot.infraestructure.services.FlyService;
import com.joirv.CursoSpringBoot.util.SortType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlyControllerTest {

    @Mock
    private FlyService flyService;

    @InjectMocks
    private FlyController flyController;

    @Test
    @DisplayName("findAllPagination should return 200 OK when successful")
    void findAllPagination_ShouldReturn200OK_WhenSuccessful() {
        // Given
        ApiResponseDto<List<FlyResponseDto>> expectedResponse = ApiResponseDto.<List<FlyResponseDto>>builder()
                .status("success")
                .statusCode(200)
                .data(List.of(new FlyResponseDto()))
                .build();
        given(flyService.findAllPagination(anyInt(), anyInt(), any(SortType.class))).willReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> response = flyController.findAllPagination(1, 10, SortType.NONE);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(flyService).findAllPagination(1, 10, SortType.NONE);
    }

    @Test
    @DisplayName("readLessPrice should return 200 OK when successful")
    void readLessPrice_ShouldReturn200OK_WhenSuccessful() {
        // Given
        ApiResponseDto<List<FlyResponseDto>> expectedResponse = ApiResponseDto.<List<FlyResponseDto>>builder()
                .status("success")
                .statusCode(200)
                .data(List.of(new FlyResponseDto()))
                .build();
        given(flyService.readLessPrice(anyInt(), anyInt(), any(SortType.class), any(BigDecimal.class))).willReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> response = flyController.readLessPrice(1, 10, SortType.NONE, new BigDecimal("100.00"));

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(flyService).readLessPrice(1, 10, SortType.NONE, new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("readBetweenPrice should return 200 OK when successful")
    void readBetweenPrice_ShouldReturn200OK_WhenSuccessful() {
        // Given
        ApiResponseDto<List<FlyResponseDto>> expectedResponse = ApiResponseDto.<List<FlyResponseDto>>builder()
                .status("success")
                .statusCode(200)
                .data(List.of(new FlyResponseDto()))
                .build();
        given(flyService.readBetweenPrice(any(BigDecimal.class), any(BigDecimal.class))).willReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> response = flyController.readBetweenPrice(new BigDecimal("50.00"), new BigDecimal("150.00"));

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(flyService).readBetweenPrice(new BigDecimal("50.00"), new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("readByOriginDestiny should return 200 OK when successful")
    void readByOriginDestiny_ShouldReturn200OK_WhenSuccessful() {
        // Given
        ApiResponseDto<Set<FlyResponseDto>> expectedResponse = ApiResponseDto.<Set<FlyResponseDto>>builder()
                .status("success")
                .statusCode(200)
                .data(Set.of(new FlyResponseDto()))
                .build();
        given(flyService.readByOriginDestiny(anyString(), anyString())).willReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseDto<Set<FlyResponseDto>>> response = flyController.readByOriginDestiny("MEX", "JFK");

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(flyService).readByOriginDestiny("MEX", "JFK");
    }

    @Test
    @DisplayName("findAllPagination should throw EntityNotFoundException when service throws it")
    void findAllPagination_ShouldThrowException_WhenServiceFails() {
        // Given
        given(flyService.findAllPagination(anyInt(), anyInt(), any(SortType.class)))
                .willThrow(new EntityNotFoundException("No flights found"));

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> flyController.findAllPagination(1, 10, SortType.NONE));
        verify(flyService).findAllPagination(1, 10, SortType.NONE);
    }

    @Test
    @DisplayName("readBetweenPrice should throw CustomerCounterException when service throws it")
    void readBetweenPrice_ShouldThrowException_WhenServiceFails() {
        // Given
        given(flyService.readBetweenPrice(any(BigDecimal.class), any(BigDecimal.class)))
                .willThrow(new CustomerCounterException("Invalid price range"));

        // When & Then
        assertThrows(CustomerCounterException.class, () -> flyController.readBetweenPrice(new BigDecimal("200.00"), new BigDecimal("100.00")));
        verify(flyService).readBetweenPrice(new BigDecimal("200.00"), new BigDecimal("100.00"));
    }
}
