# Spec: Refactorización SonarCloud - Código Limpio

## Objetivo
Corregir los 9 code smells detectados por SonarCloud para mejorar la mantenibilidad y el cumplimiento de estándares Java modernos.

## Tareas de Refactorización
1. **AeroLinea.java:** Renombrar constantes a MAYÚSCULAS_CON_GUIONES.
2. **FlyService.java:** Cambiar `Stream.collect(Collectors.toList())` por `.toList()`.
3. **TourHelper.java:** Usar `HashSet.newHashSet()` en lugar de `new HashSet<>()`.
4. **TourHelper.java:** Usar `ZoneId.of("America/Mexico_City")` en `.now()`.
5. **FlyService.java:** Refactorizar `if/else` anidados en `sort()` usando Java `switch` expression.
6. **Global:** Limpiar imports no utilizados en el repositorio.
7. **Global:** Quitar modificadores `public` en clases y métodos de prueba de JUnit 5.
8. **UserRequestDto.java:** Eliminar campo `email` privado no usado.
9. **TourService.java:** Completar o eliminar comentarios `TODO`.

## Estrategia de Verificación
- Tras aplicar cada cambio, compilar el proyecto.
- Ejecutar la suite de tests (`mvn test`) para asegurar que no se rompió la lógica del `FlyService`.