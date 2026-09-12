
package com.example.smartqueue.controller;

import com.example.smartqueue.dto.AppointmentRequestDTO;
import com.example.smartqueue.entity.Appointment;
import com.example.smartqueue.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Create Appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO request) {

        Appointment savedAppointment =
                appointmentService.createAppointment(request);

        return new ResponseEntity<>(
                savedAppointment,
                HttpStatus.CREATED
        );
    }

    // Get All Appointments
    @GetMapping
    public List<AppointmentResponseDTO> getAllAppointments() {

        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // Update Appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO request) {

        Appointment updatedAppointment =
                appointmentService.updateAppointment(id, request);

        return new ResponseEntity<>(
                updatedAppointment,
                HttpStatus.OK
        );
    }

    // Cancel Appointment
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id) {

        Appointment cancelledAppointment =
                appointmentService.cancelAppointment(id);

        return new ResponseEntity<>(
                cancelledAppointment,
                HttpStatus.OK
        );
    }

    // Confirm Appointment
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Appointment> confirmAppointment(
            @PathVariable Long id) {

        Appointment confirmedAppointment =
                appointmentService.confirmAppointment(id);

        return new ResponseEntity<>(
                confirmedAppointment,
                HttpStatus.OK
        );
    }

    // Delete Appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok(
                "Appointment deleted successfully"
        );
    }
}
