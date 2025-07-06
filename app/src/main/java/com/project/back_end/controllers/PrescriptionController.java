package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service sharedService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescriptionValidated(@RequestBody Prescription prescription,
            @PathVariable String token) {

        if (!sharedService.validateToken("doctor", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        boolean appointmentUpdated = appointmentService.markAsPrescribed(prescription.getAppointmentId());
        if (!appointmentUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to update appointment status"));
        }

        boolean saved = prescriptionService.savePrescription(prescription);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prescription saved successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save prescription"));
        }
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        if (!sharedService.validateToken("doctor", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        Prescription prescription = prescriptionService.getByAppointmentId(appointmentId);
        if (prescription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Prescription not found"));
        }

        return ResponseEntity.ok(prescription);
    }
}
