package com.ups.portafolio.portafolio_backend.appointment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.ups.portafolio.portafolio_backend.appointment.entity.AppointmentEntity;
import com.ups.portafolio.portafolio_backend.appointment.repository.AppointmentRepository;
import com.ups.portafolio.portafolio_backend.email.EmailService;
import com.ups.portafolio.portafolio_backend.schedules.entity.ScheduleEntity;
import com.ups.portafolio.portafolio_backend.schedules.repository.ScheduleRepository;
import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;
import com.ups.portafolio.portafolio_backend.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private EmailService emailService;

    @Transactional
    public AppointmentEntity bookAppointment(UUID scheduleId, UUID clientId, String topic) {
        
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (!"AVAILABLE".equals(schedule.getStatus())) {
            throw new RuntimeException("Este horario ya no está disponible");
        }

        UserEntity client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        AppointmentEntity appointment = AppointmentEntity.builder()
                .schedule(schedule)
                .client(client)
                .programmer(schedule.getUser()) 
                .status("PENDING") 
                .topic(topic)      
                .build();

        schedule.setStatus("BOOKED");
        scheduleRepository.save(schedule);

        return appointmentRepository.save(appointment);
    }

    public List<AppointmentEntity> getMyAppointments(UUID clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public List<AppointmentEntity> getIncomingAppointments(UUID programmerId) {
        return appointmentRepository.findByProgrammerId(programmerId);
    }

    public AppointmentEntity updateStatus(UUID appointmentId, String newStatus) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setStatus(newStatus);

        if ("REJECTED".equalsIgnoreCase(newStatus)) {
            ScheduleEntity schedule = appointment.getSchedule();
            schedule.setStatus("AVAILABLE"); 
            scheduleRepository.save(schedule);
        }

        return appointmentRepository.save(appointment);
    }

    public long countByProgrammerAndStatus(UUID programmerId, String status) {
        return appointmentRepository.countByProgrammerIdAndStatus(programmerId, status);
    }

    public byte[] generatePdfReport(UUID programmerId) {
        List<AppointmentEntity> appointments = appointmentRepository.findByProgrammerId(programmerId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph title = new Paragraph("Reporte de Asesorías Recibidas", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.addCell("Cliente");
        table.addCell("Motivo");
        table.addCell("Estado");

        for (AppointmentEntity app : appointments) {
            table.addCell(app.getClient().getName());
            table.addCell(app.getTopic());
            table.addCell(app.getStatus());
        }

        document.add(table);
        document.close();
        
        return out.toByteArray();
    }

    public byte[] generateExcelReport(UUID programmerId) {
        List<AppointmentEntity> appointments = appointmentRepository.findByProgrammerId(programmerId);
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Asesorías");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Cliente");
            headerRow.createCell(1).setCellValue("Motivo");
            headerRow.createCell(2).setCellValue("Estado");

            int rowIdx = 1;
            for (AppointmentEntity app : appointments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(app.getClient().getName());
                row.createCell(1).setCellValue(app.getTopic());
                row.createCell(2).setCellValue(app.getStatus());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar Excel", e);
        }
    }

    public Map<String, Long> getAppointmentSummary(UUID programmerId) {
        List<AppointmentEntity> appointments = appointmentRepository.findByProgrammerId(programmerId);
    
        Map<String, Long> summary = new HashMap<>();
        summary.put("pendientes", appointments.stream().filter(a -> "PENDIENTE".equals(a.getStatus())).count());
        summary.put("aprobadas", appointments.stream().filter(a -> "APROBADA".equals(a.getStatus())).count());
        summary.put("rechazadas", appointments.stream().filter(a -> "RECHAZADA".equals(a.getStatus())).count());
    
        return summary;
    }

    public void cambiarEstadoCita(UUID id, String nuevoEstado) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setStatus(nuevoEstado);
        appointmentRepository.save(appointment);

        emailService.enviarNotificacionCita(
            appointment.getClient().getEmail(),
            appointment.getClient().getName(), 
            nuevoEstado
        );
    }
}