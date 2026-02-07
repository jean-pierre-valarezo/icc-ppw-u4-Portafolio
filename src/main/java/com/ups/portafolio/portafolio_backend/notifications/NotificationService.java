package com.ups.portafolio.portafolio_backend.notifications;


import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    public void sendEmail(String to, String subject, String message) {
        System.out.println("EMAIL ENVIADO");
        System.out.println("Para: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + message);
    }

    public void sendWhatsApp(String phone, String message) {
        System.out.println("WHATSAPP ENVIADO");
        System.out.println("Teléfono: " + phone);
        System.out.println("Mensaje: " + message);
    }
}
