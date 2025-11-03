package com.logistica.componentec.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class CalculadoraUtilsTest {

    @Test
    void testCalcularTotal() {
        List<BigDecimal> valores = Arrays.asList(
            new BigDecimal("10.50"),
            new BigDecimal("25.30"),
            new BigDecimal("5.20")
        );
        
        BigDecimal resultado = CalculadoraUtils.calcularTotal(valores);
        assertEquals(new BigDecimal("41.00"), resultado);
    }

    @Test
    void testCalcularTotalConNulls() {
        List<BigDecimal> valores = Arrays.asList(
            new BigDecimal("10.50"),
            null,
            new BigDecimal("25.30")
        );
        
        BigDecimal resultado = CalculadoraUtils.calcularTotal(valores);
        assertEquals(new BigDecimal("35.80"), resultado);
    }

    @Test
    void testCalcularTotalConIva() {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal iva = new BigDecimal("12");
        
        BigDecimal resultado = CalculadoraUtils.calcularTotalConIva(subtotal, iva);
        assertEquals(new BigDecimal("112.00"), resultado);
    }

    @Test
    void testAplicarDescuento() {
        BigDecimal monto = new BigDecimal("100.00");
        BigDecimal descuento = new BigDecimal("10");
        
        BigDecimal resultado = CalculadoraUtils.aplicarDescuento(monto, descuento);
        assertEquals(new BigDecimal("90.00"), resultado);
    }

    @Test
    void testCalcularTotalListaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            CalculadoraUtils.calcularTotal(null);
        });
    }
}