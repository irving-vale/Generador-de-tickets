package com.joirv.CursoSpringBoot.infraestructure.exceptions;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.exceptions.personalExceptions.CustomerCounterException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleRuntimeException(RuntimeException e) {
		ApiResponseDto<Void> error = ApiResponseDto.<Void>builder()
				.status("error")
				.message(e.getMessage())
		        		.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
		        		.data(null)
		        		.meta(null)
				.build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
		ApiResponseDto<Void> error = ApiResponseDto.<Void>builder()
				.status("error")
				.message(e.getMessage())
		        		.statusCode(HttpStatus.NOT_FOUND.value())
		        		.data(null)
		        		.meta(null)
				.build();

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// Error de integridad en base de datos (ej: clave duplicada)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
		        .status("error")
		        .message("Database error: " + ex.getMessage())
		        .statusCode(HttpStatus.CONFLICT.value())
		        .data(null)
		        .meta(null)
		        .build();
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(CustomerCounterException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleCustomerCounterException(CustomerCounterException e) {
		ApiResponseDto<Void> error = ApiResponseDto.<Void>builder()
				.status("error")
				.message(e.getMessage())
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.data(null)
				.meta(null)
				.build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	}

