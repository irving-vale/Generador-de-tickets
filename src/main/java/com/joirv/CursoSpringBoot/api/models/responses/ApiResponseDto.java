package com.joirv.CursoSpringBoot.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto <T> {
	private String status;      // Ejemplo: "success" o "error"
	private String message;     // Ejemplo: "Operación exitosa"
	private int statusCode;     // Código HTTP (ej: 200, 404)
	private T data;             // Objeto o lista de objetos devueltos
	private Meta meta;          // Información adicional (opcional, útil para paginación)
}
