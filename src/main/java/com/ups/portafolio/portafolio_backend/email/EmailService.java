package com.ups.portafolio.portafolio_backend.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class EmailService {
    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarNotificacionCita(String emailDestino, String nombreCliente, String estado) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("to", List.of(Map.of("email", emailDestino)));
        body.put("subject", "Actualización de tu cita: " + estado);
        
        String htmlContent = "<h2>Hola " + nombreCliente + ",</h2>" +
                             "<p>Te informamos que tu cita ha sido: <strong>" + estado + "</strong>.</p>" +
                             "<p>Gracias por usar nuestro sistema de asesorías.</p>";
        
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Correo enviado con éxito a " + emailDestino);
            }
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }

        System.out.println("API KEY: " + apiKey);
        System.out.println("SENDER EMAIL: " + senderEmail);
        System.out.println("SENDER NAME: " + senderName);

    }
}
