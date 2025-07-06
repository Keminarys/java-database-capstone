package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.models.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service sharedService;

    @GetMapping("/info/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {
        if (!sharedService.validateToken("patient", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        Patient patient = patientService.getPatientByToken(token);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Patient not found"));
        }

        return ResponseEntity.ok(patient);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createPatient(@Validated @RequestBody Patient patient) {
        if (sharedService.exists(patient)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Patient already exists"));
        }

        boolean created = patientService.createPatient(patient);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Patient registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to register patient"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        Map<String, Object> result = sharedService.validatePatientLogin(login);
        if (result.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/appointments/{patientId}/{token}/{user}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String token,
            @PathVariable String user) {

        if (!sharedService.validateToken(user, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        List<?> appointments = patientService.getAppointments(patientId);
        return ResponseEntity.ok(Map.of("appointments", appointments));
    }

    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        if (!sharedService.validateToken("patient", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        List<?> filtered = sharedService.filterAppointments(condition, name, token);
        return ResponseEntity.ok(Map.of("filteredAppointments", filtered));
    }
}
