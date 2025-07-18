package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppService;
import com.project.back_end.services.DoctorService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppService service;

    @Autowired
    public DoctorController(DoctorService doctorService, AppService service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token
    ) {
        Map<String, Object> map = new HashMap<>();
        ResponseEntity<Map<String, String>> tempMap = service.validateToken(
                token,
                user
        );
        if (!tempMap.getBody().isEmpty()) {
            map.putAll(tempMap.getBody());
            return new ResponseEntity<>(map, tempMap.getStatusCode());
        }
        map.put("message", doctorService.getDoctorAvailability(doctorId, date));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> map = new HashMap<>();
        map.put("doctors", doctorService.getDoctors());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping("/add/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @RequestBody @Valid Doctor doctor,
            @PathVariable String token
    ) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String, String>> tempMap = service.validateToken(
                token,
                "admin"
        );
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }

        int result = doctorService.saveDoctor(doctor);
        if (result == 1) {
            response.put("message", "Doctor added successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (result == -1) {
            response.put("message", "Doctor already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        response.put("message", "An internal server error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(
            @RequestBody @Valid Login login
    ) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody @Valid Doctor doctor,
            @PathVariable String token
    ) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String, String>> tempMap = service.validateToken(
                token,
                "admin"
        );
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        int result = doctorService.updateDoctor(doctor);
        if (result == 1) {
            response.put("message", "Doctor updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (result == -1) {
            response.put("message", "Doctor not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("message", "An internal server error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable long id,
            @PathVariable String token
    ) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> tempMap = service.validateToken(
                token,
                "admin"
        );
        if (!tempMap.getBody().isEmpty()) {
            return tempMap;
        }
        int result = doctorService.deleteDoctor(id);
        if (result == 1) {
            response.put("message", "Doctor deleted successfully with ID: " + id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (result == -1) {
            response.put("message", "Doctor not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("message", "An internal server error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality
    ) {
        Map<String, Object> map = new HashMap<>();
        map = service.filterDoctor(name, speciality, time);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
