package com.joirv.CursoSpringBoot.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

	private Long totalItems;
	private int currentPage;
	private int totalPages;
	private int pageSize;
}
