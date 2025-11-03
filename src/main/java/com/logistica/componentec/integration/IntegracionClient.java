package com.logistica.componentec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Cliente de integración para comunicación entre componentes A y B.
 * Proporciona métodos para invocar endpoints de los otros componentes.
 */
public class IntegracionClient {

    private static final String COMPONENTE_A_BASE_URL = "http://localhost:8081/api";
    private static final String COMPONENTE_B_BASE_URL = "http://localhost:8082/api";
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public IntegracionClient() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene estadísticas de pedidos del Componente A.
     * 
     * @return Mapa con estadísticas de pedidos
     * @throws IOException si hay error en la comunicación
     */
    public Map<String, Object> obtenerEstadisticasPedidos() throws IOException {
        String url = COMPONENTE_A_BASE_URL + "/pedidos/estadisticas";
        HttpGet request = new HttpGet(url);
        
        try (var response = httpClient.execute(request)) {
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(jsonResponse, Map.class);
        }
    }

    /**
     * Obtiene estadísticas de facturas del Componente B.
     * 
     * @return Mapa con estadísticas de facturas
     * @throws IOException si hay error en la comunicación
     */
    public Map<String, Object> obtenerEstadisticasFacturas() throws IOException {
        String url = COMPONENTE_B_BASE_URL + "/facturas/estadisticas";
        HttpGet request = new HttpGet(url);
        
        try (var response = httpClient.execute(request)) {
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(jsonResponse, Map.class);
        }
    }

    /**
     * Genera un reporte consolidado combinando datos de ambos componentes.
     * Este método completa el flujo circular de integración.
     * 
     * @return Reporte consolidado
     */
    public Map<String, Object> generarReporteConsolidado() {
        Map<String, Object> reporte = new HashMap<>();
        
        try {
            // Obtener datos de ambos componentes
            Map<String, Object> estadisticasPedidos = obtenerEstadisticasPedidos();
            Map<String, Object> estadisticasFacturas = obtenerEstadisticasFacturas();
            
            // Calcular totales consolidados
            BigDecimal totalPedidos = extraerTotal(estadisticasPedidos, "total");
            BigDecimal totalFacturas = extraerTotal(estadisticasFacturas, "total");
            BigDecimal totalConsolidado = totalPedidos.add(totalFacturas);
            
            // Construir reporte
            reporte.put("totalPedidos", totalPedidos);
            reporte.put("totalFacturas", totalFacturas);
            reporte.put("totalConsolidado", totalConsolidado);
            reporte.put("estadisticasPedidos", estadisticasPedidos);
            reporte.put("estadisticasFacturas", estadisticasFacturas);
            reporte.put("fechaGeneracion", java.time.LocalDateTime.now().toString());
            
        } catch (IOException e) {
            reporte.put("error", "Error al generar reporte: " + e.getMessage());
            reporte.put("totalConsolidado", BigDecimal.ZERO);
        }
        
        return reporte;
    }

    /**
     * Notifica al Componente A sobre una nueva factura creada.
     * 
     * @param facturaId ID de la factura
     * @param monto Monto de la factura
     * @return true si la notificación fue exitosa
     */
    public boolean notificarNuevaFactura(String facturaId, BigDecimal monto) {
        try {
            String url = COMPONENTE_A_BASE_URL + "/notificaciones/nueva-factura";
            HttpPost request = new HttpPost(url);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("facturaId", facturaId);
            payload.put("monto", monto);
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));
            
            try (var response = httpClient.execute(request)) {
                return response.getCode() >= 200 && response.getCode() < 300;
            }
            
        } catch (IOException e) {
            System.err.println("Error al notificar nueva factura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrae un valor total de un mapa de estadísticas.
     * 
     * @param estadisticas Mapa de estadísticas
     * @param clave Clave del total
     * @return Valor total como BigDecimal
     */
    private BigDecimal extraerTotal(Map<String, Object> estadisticas, String clave) {
        Object valor = estadisticas.get(clave);
        if (valor instanceof Number) {
            return BigDecimal.valueOf(((Number) valor).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Cierra el cliente HTTP.
     */
    public void cerrar() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}