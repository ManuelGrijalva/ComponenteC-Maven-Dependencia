# Componente C - Dependencia Compartida Maven

## 📋 Descripción

Componente C es una biblioteca Java compartida que proporciona utilidades reutilizables para el sistema de logística. Este proyecto Maven (sin Spring Boot) contiene métodos comunes utilizados por los Componentes A y B.

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐
│   Componente A  │◄──►│   Componente B  │
│ (Spring Boot +  │    │ (Spring Boot +  │
│    MariaDB)     │    │  PostgreSQL)    │
└─────────┬───────┘    └─────────┬───────┘
          │                      │
          │    ┌─────────────────┐│
          └───►│   Componente C  │◄──┘
               │ (Maven JAR -    │
               │  Dependencia)   │
               └─────────────────┘
```

## 🎯 Funcionalidades Principales

### 📊 CalculadoraUtils
- `calcularTotal(List<BigDecimal>)` - Suma elementos de una lista
- `calcularTotalConIva(BigDecimal, BigDecimal)` - Calcula total con IVA
- `aplicarDescuento(BigDecimal, BigDecimal)` - Aplica descuentos

### 🔧 CodigoUtils
- `generarCodigoUnico(String tipoEntidad)` - Genera códigos únicos por tipo
- `generarCodigoSimple()` - Genera UUID sin guiones
- `generarCodigoConPrefijo(String)` - Código con prefijo personalizado

### ✅ ValidacionUtils
- `validarEmail(String)` - Valida formato de email
- `validarTelefono(String)` - Valida formato de teléfono
- `validarMontoPositivo(BigDecimal)` - Valida montos positivos
- `validarNoVacio(String)` - Valida cadenas no vacías

### 🔄 IntegracionClient
- `obtenerEstadisticasPedidos()` - Conecta con Componente A
- `obtenerEstadisticasFacturas()` - Conecta con Componente B  
- `generarReporteConsolidado()` - Flujo circular de integración
- `notificarNuevaFactura()` - Notificaciones entre componentes

## 🛠️ Tecnologías

- **Java 17**
- **Maven 3.x**
- **JUnit 5** (testing)
- **Apache HTTP Client 5** (integraciones HTTP)
- **Jackson** (procesamiento JSON)

## 📦 Instalación como Dependencia

### 1. Construir el JAR
```bash
mvn clean package
```

### 2. Instalar en repositorio local
```bash
mvn install
```

### 3. Agregar dependencia en proyectos
```xml
<dependency>
    <groupId>com.logistica</groupId>
    <artifactId>componente-c</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚀 Uso

### Ejemplo: Calcular Total
```java
import com.logistica.componentec.utils.CalculadoraUtils;
import java.math.BigDecimal;
import java.util.Arrays;

List<BigDecimal> montos = Arrays.asList(
    new BigDecimal("100.50"),
    new BigDecimal("250.30"),
    new BigDecimal("75.20")
);

BigDecimal total = CalculadoraUtils.calcularTotal(montos);
```

### Ejemplo: Generar Código Único
```java
import com.logistica.componentec.utils.CodigoUtils;

String codigoCliente = CodigoUtils.generarCodigoUnico("CLIENTE");
String codigoPedido = CodigoUtils.generarCodigoUnico("PEDIDO");
```

### Ejemplo: Integración entre Componentes
```java
import com.logistica.componentec.integration.IntegracionClient;

IntegracionClient client = new IntegracionClient();
Map<String, Object> reporte = client.generarReporteConsolidado();
```

## 🧪 Testing

Ejecutar tests:
```bash
mvn test
```

## 📁 Estructura del Proyecto

```
componente-c/
├── pom.xml
├── README.md
├── .github/
│   └── copilot-instructions.md
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── logistica/
│   │               └── componentec/
│   │                   ├── utils/
│   │                   │   ├── CalculadoraUtils.java
│   │                   │   └── CodigoUtils.java
│   │                   ├── validation/
│   │                   │   └── ValidacionUtils.java
│   │                   └── integration/
│   │                       └── IntegracionClient.java
│   └── test/
│       └── java/
│           └── com/
│               └── logistica/
│                   └── componentec/
│                       └── utils/
│                           ├── CalculadoraUtilsTest.java
│                           └── CodigoUtilsTest.java
```

## 🔄 Integración Circular

Este componente implementa un flujo circular de integración:

1. **Componente A** utiliza métodos de **Componente C**
2. **Componente B** utiliza métodos de **Componente C**  
3. **Componente C** invoca endpoints de **Componente A** y **Componente B**
4. Se genera un reporte consolidado combinando datos de ambos sistemas

## 📝 Próximos Pasos

1. ✅ Subir a repositorio Git con commits trazables
2. Implementar Componente A (Spring Boot + MariaDB)
3. Implementar Componente B (Spring Boot + PostgreSQL)
4. Configurar APIs REST con OpenAPI 3
5. Integrar con frontend Next.js existente

## 🔗 Repositorios del Proyecto

- **Componente C (Maven):** https://github.com/ManuelGrijalva/ComponenteC-Maven-Dependencia
- **Frontend (Next.js):** https://github.com/ManuelGrijalva/Intro_Next.js
- **Componente A:** (Próximamente)
- **Componente B:** (Próximamente)

## 👨‍💻 Desarrollo

**Estudiante:** Manuel Grijalva  
**Proyecto:** Serie II - Examen Final  
**Curso:** Desarrollo web  
**Repositorio:** https://github.com/ManuelGrijalva/ComponenteC-Maven-Dependencia 
