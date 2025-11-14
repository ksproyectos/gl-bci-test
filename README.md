# User Service — Instrucciones rápidas

Proyecto Java Spring Boot. Instrucciones mínimas para compilar, ejecutar pruebas y generar cobertura.

Prerequisitos
- JDK 11+ instalado
- Usar el wrapper de Gradle incluido (no requiere instalación global)

Compilar y ejecutar pruebas
```bash
# Limpiar, ejecutar tests y generar reporte JaCoCo (HTML)
./gradlew clean test jacocoTestReport
```

Ver resultados
- Reporte de tests: build/reports/tests/test/index.html
- Reporte de cobertura (HTML): build/reports/jacoco/test/html/index.html

Verificación de cobertura (puede fallar si < 80%)
```bash
# Ejecuta la verificación de umbral (configurado en Gradle, falla si la cobertura < 80%)
./gradlew jacocoTestCoverageVerification
# o ejecutar la tarea completa de chequeo
./gradlew check
```

Ejecutar la aplicación
```bash
./gradlew bootRun
```

Ejecutar tests concretos
```bash
# Ejecutar una clase de test específica
./gradlew test --tests com.newbank.userservice.service.UserServiceTest
```

Notas
- Usamos la base H2 en memoria en `application.properties` para pruebas.
- Si necesita ajustar el umbral de cobertura, edite `build.gradle` (tarea `jacocoTestCoverageVerification`).
