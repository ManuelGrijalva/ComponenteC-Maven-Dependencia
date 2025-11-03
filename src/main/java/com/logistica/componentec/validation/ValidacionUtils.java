package com.logistica.componentec.validation;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utilidades de validación para el sistema de logística.
 * Proporciona métodos reutilizables para validar datos comunes.
 */
public class ValidacionUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern TELEFONO_PATTERN = Pattern.compile(
        "^[+]?[0-9]{7,15}$"
    );

    /**
     * Valida si un email tiene formato correcto.
     * 
     * @param email Email a validar
     * @return true si el email es válido
     */
    public static boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida si un teléfono tiene formato correcto.
     * 
     * @param telefono Teléfono a validar
     * @return true si el teléfono es válido
     */
    public static boolean validarTelefono(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }

    /**
     * Valida si un monto es positivo.
     * 
     * @param monto Monto a validar
     * @return true si el monto es positivo
     */
    public static boolean validarMontoPositivo(BigDecimal monto) {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida si una cadena no es null ni vacía.
     * 
     * @param valor Cadena a validar
     * @return true si la cadena tiene contenido
     */
    public static boolean validarNoVacio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /**
     * Valida si una cadena tiene la longitud mínima requerida.
     * 
     * @param valor Cadena a validar
     * @param longitudMinima Longitud mínima requerida
     * @return true si cumple la longitud mínima
     */
    public static boolean validarLongitudMinima(String valor, int longitudMinima) {
        return validarNoVacio(valor) && valor.trim().length() >= longitudMinima;
    }

    /**
     * Valida si un código tiene el formato esperado (3-4 letras, guión, números).
     * 
     * @param codigo Código a validar
     * @return true si el formato es correcto
     */
    public static boolean validarFormatoCodigo(String codigo) {
        if (!validarNoVacio(codigo)) {
            return false;
        }
        
        Pattern codigoPattern = Pattern.compile("^[A-Z]{3,4}-\\d{14}-[A-Z0-9]{8}$");
        return codigoPattern.matcher(codigo).matches();
    }
}