<!-- Copilot Instructions for ComponenteC-Maven-Dependencia -->

## Project Overview
This is **Component C** - A Maven dependency project for a logistics system integration.

### Project Type
- Java Maven project (no Spring Boot)
- Shared dependency JAR for Components A and B
- Java 17 target
- Standard Maven structure

### Key Requirements
- Reusable utility methods: `calcularTotal()`, `generarCodigoUnico()`
- Integration methods for Component A/B communication
- Package as JAR dependency
- Maintain commit traceability with prefixes

### Architecture Context
- Component A: Spring Boot + MariaDB (Clients/Orders)
- Component B: Spring Boot + PostgreSQL (Suppliers/Invoices)  
- Component C: Maven JAR (this project - shared utilities)
- Frontend: Next.js (existing)

### Development Guidelines
- Follow Maven best practices
- Use descriptive commit messages with prefixes
- Implement clean, reusable utility classes
- Include proper JavaDoc documentation