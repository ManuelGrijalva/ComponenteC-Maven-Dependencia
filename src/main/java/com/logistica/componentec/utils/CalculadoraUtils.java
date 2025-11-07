package com.logistica.componentec.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utilidades para cálculos financieros empresariales en el sistema de logística.
 * Proporciona métodos reutilizables para cálculos de totales, descuentos e impuestos.
 */
public class CalculadoraUtils {

    // Constantes empresariales
    private static final BigDecimal IVA_EMPRESARIAL = new BigDecimal("15.00"); // 15%
    private static final BigDecimal DESCUENTO_NIVEL_1 = new BigDecimal("5.00");  // 5%
    private static final BigDecimal DESCUENTO_NIVEL_2 = new BigDecimal("10.00"); // 10%
    private static final BigDecimal DESCUENTO_NIVEL_3 = new BigDecimal("15.00"); // 15%
    private static final BigDecimal UMBRAL_NIVEL_1 = new BigDecimal("1000.00");
    private static final BigDecimal UMBRAL_NIVEL_2 = new BigDecimal("5000.00");

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
     * Calcula descuentos escalonados según el monto empresarial.
     * - < $1,000: 5% descuento
     * - $1,000 - $4,999: 10% descuento
     * - ≥ $5,000: 15% descuento
     *
     * @param monto Monto original
     * @return Porcentaje de descuento aplicable
     */
    public static BigDecimal calcularPorcentajeDescuentoEscalonado(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser null");
        }

        if (monto.compareTo(UMBRAL_NIVEL_2) >= 0) {
            return DESCUENTO_NIVEL_3; // 15%
        } else if (monto.compareTo(UMBRAL_NIVEL_1) >= 0) {
            return DESCUENTO_NIVEL_2; // 10%
        } else {
            return DESCUENTO_NIVEL_1; // 5%
        }
    }

    /**
     * Aplica descuento escalonado automático según el monto.
     *
     * @param monto Monto original
     * @return Monto con descuento aplicado
     */
    public static BigDecimal aplicarDescuentoEscalonado(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser null");
        }

        BigDecimal porcentajeDescuento = calcularPorcentajeDescuentoEscalonado(monto);
        return aplicarDescuento(monto, porcentajeDescuento);
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

    /**
     * Calcula el IVA empresarial (15%) sobre un monto.
     *
     * @param monto Monto sobre el cual calcular IVA
     * @return Valor del IVA
     */
    public static BigDecimal calcularIVA(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser null");
        }

        return monto.multiply(IVA_EMPRESARIAL.divide(BigDecimal.valueOf(100)));
    }

    /**
     * Calcula el total con IVA empresarial (15%) aplicado.
     *
     * @param subtotal Subtotal sin IVA
     * @return Total con IVA incluido
     */
    public static BigDecimal calcularTotalConIVA(BigDecimal subtotal) {
        if (subtotal == null) {
            throw new IllegalArgumentException("El subtotal no puede ser null");
        }

        BigDecimal iva = calcularIVA(subtotal);
        return subtotal.add(iva);
    }

    /**
     * Calcula el total con IVA aplicado usando porcentaje personalizado.
     *
     * @param subtotal Subtotal sin IVA
     * @param porcentajeIva Porcentaje de IVA (ej: 15 para 15%)
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
     * Cálculo completo empresarial: aplica descuento escalonado y luego IVA.
     *
     * @param montoOriginal Monto original antes de descuentos e impuestos
     * @return Resultado con subtotal, descuento, IVA y total final
     */
    public static ResultadoCalculoEmpresarial calcularTotalEmpresarial(BigDecimal montoOriginal) {
        if (montoOriginal == null) {
            throw new IllegalArgumentException("El monto original no puede ser null");
        }

        BigDecimal porcentajeDescuento = calcularPorcentajeDescuentoEscalonado(montoOriginal);
        BigDecimal montoDescuento = montoOriginal.multiply(porcentajeDescuento.divide(BigDecimal.valueOf(100)));
        BigDecimal subtotal = montoOriginal.subtract(montoDescuento);
        BigDecimal iva = calcularIVA(subtotal);
        BigDecimal total = subtotal.add(iva);

        return new ResultadoCalculoEmpresarial(
            montoOriginal,
            porcentajeDescuento,
            montoDescuento,
            subtotal,
            IVA_EMPRESARIAL,
            iva,
            total
        );
    }

    /**
     * Clase para encapsular el resultado de cálculos empresariales completos.
     */
    public static class ResultadoCalculoEmpresarial {
        private final BigDecimal montoOriginal;
        private final BigDecimal porcentajeDescuento;
        private final BigDecimal montoDescuento;
        private final BigDecimal subtotal;
        private final BigDecimal porcentajeIVA;
        private final BigDecimal montoIVA;
        private final BigDecimal total;

        public ResultadoCalculoEmpresarial(BigDecimal montoOriginal, BigDecimal porcentajeDescuento,
                                         BigDecimal montoDescuento, BigDecimal subtotal,
                                         BigDecimal porcentajeIVA, BigDecimal montoIVA, BigDecimal total) {
            this.montoOriginal = montoOriginal;
            this.porcentajeDescuento = porcentajeDescuento;
            this.montoDescuento = montoDescuento;
            this.subtotal = subtotal;
            this.porcentajeIVA = porcentajeIVA;
            this.montoIVA = montoIVA;
            this.total = total;
        }

        // Getters
        public BigDecimal getMontoOriginal() { return montoOriginal; }
        public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
        public BigDecimal getMontoDescuento() { return montoDescuento; }
        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getPorcentajeIVA() { return porcentajeIVA; }
        public BigDecimal getMontoIVA() { return montoIVA; }
        public BigDecimal getTotal() { return total; }
    }
}