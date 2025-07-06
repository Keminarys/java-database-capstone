package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SystemService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public SystemService(TokenService tokenService,
                         AdminRepository adminRepository,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository,
                         DoctorService doctorService,
                         PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<?> validateToken(String token, String email) {
        try {
            if (!tokenService.isTokenValid(token, email)) {
                return ResponseEntity.status(401).body("Invalid or expired token.");
            }
            return ResponseEntity.ok("Token is valid.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token validation failed.");
        }
    }

    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid username or password.");
            }
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Admin validation failed.");
        }
    }

    public List<Doctor> filterDoctor(String name, String specialty, String timePeriod) {
        if (name != null && specialty != null && timePeriod != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, timePeriod);
        } else if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        } else if (name != null && timePeriod != null) {
            return doctorService.filterDoctorByNameAndTime(name, timePeriod);
        } else if (specialty != null && timePeriod != null) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, timePeriod);
        } else if (name != null) {
            return doctorService.findDoctorByName(name);
        } else if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        } else if (timePeriod != null) {
            return doctorService.filterDoctorsByTime(timePeriod);
        } else {
            return doctorService.getDoctors();
        }
    }

    public int validateAppointment(Long doctorId, LocalDate date, LocalTime time) {
        var doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return -1;

        List<LocalTime> availableTimes = doctorService.getDoctorAvailability(doctorId, date);
        return availableTimes.contains(time) ? 1 : 0;
    }

    public boolean validatePatient(String email, String phone) {
        return patientRepository.findByEmailOrPhone(email, phone) == null;
    }

    public ResponseEntity<?> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null || !patient.getPassword().equals(password)) {
                return ResponseEntity.status(401).body("Invalid email or password.");
            }
            String token = tokenService.generateToken(patient.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Patient login failed.");
        }
    }

    public ResponseEntity<?> filterPatient(String token, String condition, String doctorName) {
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) return ResponseEntity.status(404).body("Patient not found.");

            Long patientId = patient.getId();

            if (condition != null && doctorName != null) {
                return ResponseEntity.ok(patientService.filterByDoctorAndCondition(patientId, doctorName, condition));
            } else if (condition != null) {
                return ResponseEntity.ok(patientService.filterByCondition(patientId, condition));
            } else if (doctorName != null) {
                return ResponseEntity.ok(patientService.filterByDoctor(patientId, doctorName));
            } else {
                return ResponseEntity.ok(patientService.getPatientAppointment(patientId));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error filtering patient appointments.");
        }
    }
}
