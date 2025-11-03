package com.logistica.componentec.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utilidades para cálculos financieros en el sistema de logística.
 * Proporciona métodos reutilizables para el cálculo de totales.
 */
public class CalculadoraUtils {

    /**
     * Calcula el total sumando todos los elementos de una lista de BigDecimal.
     * 
     * @param valores Lista de valores a sumar
     * @return El total calculado
     * @throws IllegalArgumentException si la lista es null
     */
    public static BigDecimal calcularTotal(List<BigDecimal> valores) {
        if (valores == null) {
            throw new IllegalArgumentException("La lista de valores no puede ser null");
        }
        
        return valores.stream()
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el total con IVA aplicado.
     * 
     * @param subtotal Subtotal sin IVA
     * @param porcentajeIva Porcentaje de IVA (ej: 12 para 12%)
     * @return Total con IVA incluido
     */
    public static BigDecimal calcularTotalConIva(BigDecimal subtotal, BigDecimal porcentajeIva) {
        if (subtotal == null || porcentajeIva == null) {
            throw new IllegalArgumentException("Subtotal y porcentaje IVA no pueden ser null");
        }
        
        BigDecimal iva = subtotal.multiply(porcentajeIva.divide(BigDecimal.valueOf(100)));
        return subtotal.add(iva);
    }

    /**
     * Calcula descuento aplicado a un monto.
     * 
     * @param monto Monto original
     * @param porcentajeDescuento Porcentaje de descuento
     * @return Monto con descuento aplicado
     */
    public static BigDecimal aplicarDescuento(BigDecimal monto, BigDecimal porcentajeDescuento) {
        if (monto == null || porcentajeDescuento == null) {
            throw new IllegalArgumentException("Monto y porcentaje descuento no pueden ser null");
        }
        
        BigDecimal descuento = monto.multiply(porcentajeDescuento.divide(BigDecimal.valueOf(100)));
        return monto.subtract(descuento);
    }
}