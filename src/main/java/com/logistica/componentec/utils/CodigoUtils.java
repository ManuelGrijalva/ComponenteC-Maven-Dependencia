package com.logistica.componentec.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utilidades para generación de códigos únicos en el sistema de logística.
 * Proporciona métodos para generar identificadores únicos para diferentes entidades.
 */
public class CodigoUtils {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Genera un código único basado en el tipo de entidad.
     * 
     * @param tipoEntidad Tipo de entidad (CLIENTE, PEDIDO, PROVEEDOR, FACTURA)
     * @return Código único generado
     * @throws IllegalArgumentException si el tipo de entidad es null o vacío
     */
    public static String generarCodigoUnico(String tipoEntidad) {
        if (tipoEntidad == null || tipoEntidad.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de entidad no puede ser null o vacío");
        }

        String prefijo = obtenerPrefijo(tipoEntidad.toUpperCase());
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        return String.format("%s-%s-%s", prefijo, timestamp, uuid);
    }

    /**
     * Genera un código único simple usando UUID.
     * 
     * @return Código UUID sin guiones
     */
    public static String generarCodigoSimple() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * Genera un código con prefijo personalizado.
     * 
     * @param prefijo Prefijo personalizado
     * @return Código con prefijo
     */
    public static String generarCodigoConPrefijo(String prefijo) {
        if (prefijo == null || prefijo.trim().isEmpty()) {
            throw new IllegalArgumentException("El prefijo no puede ser null o vacío");
        }
        
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        return String.format("%s-%s", prefijo.toUpperCase(), timestamp);
    }

    /**
     * Obtiene el prefijo correspondiente según el tipo de entidad.
     * 
     * @param tipoEntidad Tipo de entidad
     * @return Prefijo correspondiente
     */
    private static String obtenerPrefijo(String tipoEntidad) {
        return switch (tipoEntidad) {
            case "CLIENTE" -> "CLI";
            case "PEDIDO" -> "PED";
            case "PROVEEDOR" -> "PROV";
            case "FACTURA" -> "FACT";
            case "USUARIO" -> "USR";
            case "PRODUCTO" -> "PROD";
            default -> "GEN"; // Genérico
        };
    }
}