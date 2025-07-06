package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.security.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            // Log the error if needed
            return 0;
        }
    }

    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedAppointment, Long patientId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            return "Appointment not found.";
        }

        Appointment existingAppointment = optionalAppointment.get();

        if (!existingAppointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized to update this appointment.";
        }

        // Check if doctor is available at the new time
        List<Appointment> overlappingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        updatedAppointment.getDoctor().getId(),
                        updatedAppointment.getAppointmentTime().minusMinutes(59),
                        updatedAppointment.getAppointmentTime().plusMinutes(59)
                );

        if (!overlappingAppointments.isEmpty()) {
            return "Doctor is not available at the selected time.";
        }

        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setStatus(updatedAppointment.getStatus());
        appointmentRepository.save(existingAppointment);

        return "Appointment updated successfully.";
    }

    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            return "Appointment not found.";
        }

        Appointment appointment = optionalAppointment.get();

        if (!appointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized to cancel this appointment.";
        }

        appointmentRepository.deleteById(appointmentId);
        return "Appointment canceled successfully.";
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, LocalDate date, String patientName) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, start, end);
        } else {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        }
    }

    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}
