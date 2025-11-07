package com.logistica.componentec.validation;

import com.logistica.componentec.exception.SolicitudInvalidaException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilidades de validación empresarial para el sistema de logística.
 * Proporciona métodos reutilizables para validar datos empresariales.
 */
public class ValidacionUtils {

    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern TELEFONO_PATTERN = Pattern.compile(
        "^[+]?[0-9]{7,15}$"
    );

    private static final Pattern CODIGO_POSTAL_PATTERN = Pattern.compile(
        "^[0-9]{5,10}$"
    );

    private static final Pattern CODIGO_PROYECTO_PATTERN = Pattern.compile(
        "^[A-Z]{2,3}-[0-9]{4,6}$"
    );

    // Constantes de validación empresarial
    private static final BigDecimal MONTO_MINIMO_PROYECTO = new BigDecimal("100.00");
    private static final BigDecimal MONTO_MAXIMO_PROYECTO = new BigDecimal("1000000.00");
    private static final int LONGITUD_MINIMA_NOMBRE = 2;
    private static final int LONGITUD_MAXIMA_NOMBRE = 100;

    /**
     * Valida si un email tiene formato correcto.
     */
    public static boolean validarEmail(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida email y lanza excepción si es inválido.
     */
    public static void validarEmailObligatorio(String email, String nombreCampo) {
        if (!validarEmail(email)) {
            throw new SolicitudInvalidaException(nombreCampo, email, "formato de email inválido");
        }
    }

    /**
     * Valida si un teléfono tiene formato correcto.
     */
    public static boolean validarTelefono(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }

    /**
     * Valida si un código postal tiene formato correcto.
     */
    public static boolean validarCodigoPostal(String codigoPostal) {
        return codigoPostal != null && CODIGO_POSTAL_PATTERN.matcher(codigoPostal).matches();
    }

    /**
     * Valida si un código de proyecto tiene formato correcto (ej: PR-001234).
     */
    public static boolean validarCodigoProyecto(String codigo) {
        return codigo != null && CODIGO_PROYECTO_PATTERN.matcher(codigo).matches();
    }

    /**
     * Valida si un monto es positivo.
     */
    public static boolean validarMontoPositivo(BigDecimal monto) {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida si un monto está dentro del rango empresarial válido.
     */
    public static boolean validarMontoEmpresarial(BigDecimal monto) {
        return monto != null &&
               monto.compareTo(MONTO_MINIMO_PROYECTO) >= 0 &&
               monto.compareTo(MONTO_MAXIMO_PROYECTO) <= 0;
    }

    /**
     * Valida monto empresarial y lanza excepción si es inválido.
     */
    public static void validarMontoEmpresarialObligatorio(BigDecimal monto, String nombreCampo) {
        if (!validarMontoEmpresarial(monto)) {
            throw new SolicitudInvalidaException(nombreCampo,
                monto != null ? monto.toString() : "null",
                String.format("debe estar entre %s y %s", MONTO_MINIMO_PROYECTO, MONTO_MAXIMO_PROYECTO));
        }
    }

    /**
     * Valida si una cadena no es null ni vacía.
     */
    public static boolean validarTextoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Valida longitud de texto dentro de rangos empresariales.
     */
    public static boolean validarLongitudTexto(String texto, int minimo, int maximo) {
        if (!validarTextoNoVacio(texto)) return false;
        int longitud = texto.trim().length();
        return longitud >= minimo && longitud <= maximo;
    }

    /**
     * Valida nombre empresarial (2-100 caracteres).
     */
    public static boolean validarNombreEmpresarial(String nombre) {
        return validarLongitudTexto(nombre, LONGITUD_MINIMA_NOMBRE, LONGITUD_MAXIMA_NOMBRE);
    }

    /**
     * Valida nombre empresarial y lanza excepción si es inválido.
     */
    public static void validarNombreEmpresarialObligatorio(String nombre, String nombreCampo) {
        if (!validarNombreEmpresarial(nombre)) {
            throw new SolicitudInvalidaException(nombreCampo, nombre,
                String.format("debe tener entre %d y %d caracteres", LONGITUD_MINIMA_NOMBRE, LONGITUD_MAXIMA_NOMBRE));
        }
    }

    /**
     * Valida que una fecha no sea nula ni futura.
     */
    public static boolean validarFechaNoFutura(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now());
    }

    /**
     * Valida que una fecha esté en el futuro.
     */
    public static boolean validarFechaFutura(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now());
    }

    /**
     * Valida que una fecha de fin sea posterior a fecha de inicio.
     */
    public static boolean validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return fechaInicio != null && fechaFin != null && fechaFin.isAfter(fechaInicio);
    }

    /**
     * Valida rango de fechas y lanza excepción si es inválido.
     */
    public static void validarRangoFechasObligatorio(LocalDate fechaInicio, LocalDate fechaFin) {
        if (!validarRangoFechas(fechaInicio, fechaFin)) {
            throw new SolicitudInvalidaException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    /**
     * Valida que una lista no sea nula ni vacía.
     */
    public static <T> boolean validarListaNoVacia(List<T> lista) {
        return lista != null && !lista.isEmpty();
    }

    /**
     * Valida que un ID sea válido (positivo).
     */
    public static boolean validarId(Long id) {
        return id != null && id > 0;
    }

    /**
     * Valida ID y lanza excepción si es inválido.
     */
    public static void validarIdObligatorio(Long id, String nombreEntidad) {
        if (!validarId(id)) {
            throw new SolicitudInvalidaException("ID de " + nombreEntidad,
                id != null ? id.toString() : "null", "debe ser un número positivo");
        }
    }
}
