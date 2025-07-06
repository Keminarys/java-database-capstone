package com.project.back_end.controllers;

import com.project.back_end.model.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SystemService systemService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, SystemService systemService) {
        this.appointmentService = appointmentService;
           }

    @GetMapping("/doctor/{token}/{date}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @RequestParam(required = false) String patientName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        var validation = systemService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        String email = systemService.extractEmail(token);
        Long doctorId = systemService.getDoctorIdByEmail(email); // You may need to implement this helper
        return ResponseEntity.ok(appointmentService.getAppointments(doctorId, date, patientName));
    }

    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        var validation = systemService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int check = systemService.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime().toLocalDate(),
                appointment.getAppointmentTime().toLocalTime()
        );

        if (check == -1) {
            return ResponseEntity.badRequest().body("Invalid doctor ID.");
        } else if (check == 0) {
            return ResponseEntity.badRequest().body("Time slot is already booked.");
        }

        int result = appointmentService.bookAppointment(appointment);
        return result == 1
                ? ResponseEntity.ok("Appointment booked successfully.")
                : ResponseEntity.status(500).body("Failed to book appointment.");
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        var validation = systemService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        String email = systemService.extractEmail(token);
        Long patientId = systemService.getPatientIdByEmail(email); // You may need to implement this helper

        String result = appointmentService.updateAppointment(appointment.getId(), appointment, patientId);
        return result.equals("Appointment updated successfully.")
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId, @PathVariable String token) {
        var validation = systemService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        String email = systemService.extractEmail(token);
        Long patientId = systemService.getPatientIdByEmail(email); // You may need to implement this helper

        String result = appointmentService.cancelAppointment(appointmentId, patientId);
        return result.equals("Appointment canceled successfully.")
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }
}
