package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service sharedService;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        if (!sharedService.validateToken(user, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        boolean isAvailable = doctorService.checkAvailability(doctorId, date);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/{token}")
    public ResponseEntity<?> saveDoctor(
            @Validated @RequestBody Doctor doctor,
            @PathVariable String token) {

        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        if (doctorService.exists(doctor)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Doctor already exists"));
        }

        doctorService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Doctor registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@Validated @RequestBody Login login) {
        Map<String, Object> result = doctorService.authenticate(login);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateDoctor(
            @Validated @RequestBody Doctor doctor,
            @PathVariable String token) {

        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        if (!doctorService.existsById(doctor.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found"));
        }

        doctorService.updateDoctor(doctor);
        return ResponseEntity.ok(Map.of("message", "Doctor updated successfully"));
    }

    @DeleteMapping("/delete/{doctorId}/{token}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized access"));
        }

        if (!doctorService.existsById(doctorId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found"));
        }

        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        List<Doctor> filteredDoctors = sharedService.filterDoctors(name, time, speciality);
        return ResponseEntity.ok(Map.of("filteredDoctors", filteredDoctors));
    }
}
