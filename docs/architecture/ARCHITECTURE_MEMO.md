# Memoria de Arquitectura: FlyService
## Principios de Diseño Aplicados
1. **Validación Defensiva:** Todo parámetro de entrada debe validarse antes de ser usado (Null checks, .trim(), rangos numéricos).
2. **Fail Fast:** Si un parámetro es inválido, lanzar `IllegalArgumentException` inmediatamente. No procesar datos basura.
3. **Manejo de Errores:** Las excepciones deben ser descriptivas y estar validadas mediante pruebas unitarias (`assertThrows`).
4. **Normalización:** Los strings deben normalizarse (.trim() + capitalización) en la capa de servicio para garantizar consistencia en la base de datos.