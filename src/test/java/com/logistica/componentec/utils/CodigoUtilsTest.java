package com.logistica.componentec.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodigoUtilsTest {

    @Test
    void testGenerarCodigoUnico() {
        String codigo = CodigoUtils.generarCodigoUnico("CLIENTE");
        
        assertNotNull(codigo);
        assertTrue(codigo.startsWith("CLI-"));
        assertTrue(codigo.length() > 20);
    }

    @Test
    void testGenerarCodigoUnicoTiposVarios() {
        String codigoCliente = CodigoUtils.generarCodigoUnico("CLIENTE");
        String codigoPedido = CodigoUtils.generarCodigoUnico("PEDIDO");
        String codigoProveedor = CodigoUtils.generarCodigoUnico("PROVEEDOR");
        String codigoFactura = CodigoUtils.generarCodigoUnico("FACTURA");
        
        assertTrue(codigoCliente.startsWith("CLI-"));
        assertTrue(codigoPedido.startsWith("PED-"));
        assertTrue(codigoProveedor.startsWith("PROV-"));
        assertTrue(codigoFactura.startsWith("FACT-"));
    }

    @Test
    void testGenerarCodigoSimple() {
        String codigo = CodigoUtils.generarCodigoSimple();
        
        assertNotNull(codigo);
        assertEquals(32, codigo.length());
        assertFalse(codigo.contains("-"));
    }

    @Test
    void testGenerarCodigoConPrefijo() {
        String codigo = CodigoUtils.generarCodigoConPrefijo("TEST");
        
        assertNotNull(codigo);
        assertTrue(codigo.startsWith("TEST-"));
    }

    @Test
    void testGenerarCodigoUnicoTipoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            CodigoUtils.generarCodigoUnico(null);
        });
    }

    @Test
    void testGenerarCodigoUnicoTipoVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            CodigoUtils.generarCodigoUnico("");
        });
    }
}