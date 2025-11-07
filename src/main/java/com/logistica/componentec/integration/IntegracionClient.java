package com.logistica.componentec.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cliente de integración empresarial para comunicación entre microservicios.
 * Proporciona métodos para invocar endpoints de los otros componentes con manejo de errores.
 */
public class IntegracionClient {

    private static final String COMPONENTE_A_BASE_URL = "http://localhost:8080/api";
    private static final String COMPONENTE_B_BASE_URL = "http://localhost:8081/api";

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public IntegracionClient() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene estadísticas de proyectos del Componente A (Operaciones).
     *
     * @return Mapa con estadísticas de proyectos
     * @throws IOException si hay error en la comunicación
     */
    public Map<String, Object> obtenerEstadisticasProyectos() throws IOException {
        String url = COMPONENTE_A_BASE_URL + "/proyectos/estadisticas";
        HttpGet request = new HttpGet(url);

        try (var response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                throw new IOException("Error al obtener estadísticas de proyectos: " + response.getCode());
            }
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(jsonResponse, Map.class);
        }
    }

    /**
     * Obtiene información de un proyecto específico del Componente A.
     */
    public Map<String, Object> obtenerProyecto(Long proyectoId) throws IOException {
        String url = COMPONENTE_A_BASE_URL + "/proyectos/" + proyectoId;
        HttpGet request = new HttpGet(url);

        try (var response = httpClient.execute(request)) {
            if (response.getCode() == 404) {
                return null; // Proyecto no encontrado
            }
            if (response.getCode() != 200) {
                throw new IOException("Error al obtener proyecto: " + response.getCode());
            }
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(jsonResponse, Map.class);
        }
    }

    /**
     * Obtiene estadísticas de documentos del Componente B.
     */
    public Map<String, Object> obtenerEstadisticasDocumentos() throws IOException {
        String url = COMPONENTE_B_BASE_URL + "/documentos/estadisticas";
        HttpGet request = new HttpGet(url);

        try (var response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                throw new IOException("Error al obtener estadísticas de documentos: " + response.getCode());
            }
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
            return objectMapper.readValue(jsonResponse, Map.class);
        }
    }

    /**
     * Valida existencia de proyecto en Componente A desde Componente B.
     */
    public boolean validarExistenciaProyecto(Long proyectoId) throws IOException {
        try {
            Map<String, Object> proyecto = obtenerProyecto(proyectoId);
            return proyecto != null;
        } catch (IOException e) {
            throw new IOException("Error al validar existencia de proyecto: " + e.getMessage(), e);
        }
    }

    /**
     * Notifica al Componente A sobre creación de documento en Componente B.
     */
    public void notificarDocumentoCreado(Long proyectoId, Long documentoId, String tipoDocumento) throws IOException {
        String url = COMPONENTE_A_BASE_URL + "/proyectos/" + proyectoId + "/notificar-documento";
        HttpPost request = new HttpPost(url);

        Map<String, Object> payload = new HashMap<>();
        payload.put("documentoId", documentoId);
        payload.put("tipoDocumento", tipoDocumento);
        payload.put("accion", "CREADO");

        String jsonPayload = objectMapper.writeValueAsString(payload);
        request.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));

        try (var response = httpClient.execute(request)) {
            if (response.getCode() != 200 && response.getCode() != 204) {
                throw new IOException("Error al notificar documento creado: " + response.getCode());
            }
        }
    }

    /**
     * Obtiene dashboard integrado de ambos componentes.
     */
    public Map<String, Object> obtenerDashboardIntegrado() throws IOException {
        Map<String, Object> dashboard = new HashMap<>();

        try {
            Map<String, Object> estadisticasProyectos = obtenerEstadisticasProyectos();
            Map<String, Object> estadisticasDocumentos = obtenerEstadisticasDocumentos();

            dashboard.put("proyectos", estadisticasProyectos);
            dashboard.put("documentos", estadisticasDocumentos);
            dashboard.put("timestamp", System.currentTimeMillis());

        } catch (IOException e) {
            // Log error pero devuelve dashboard parcial
            dashboard.put("error", "Error al obtener algunas estadísticas: " + e.getMessage());
        }

        return dashboard;
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
