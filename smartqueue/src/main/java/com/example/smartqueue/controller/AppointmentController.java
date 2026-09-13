
package com.example.smartqueue.controller;

import com.example.smartqueue.dto.AppointmentRequestDTO;
import com.example.smartqueue.dto.AppointmentResponseDTO;
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
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO savedAppointment =
                appointmentService.createAppointment(request);

        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    // Get All Appointments
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {

        List<AppointmentResponseDTO> appointments =
                appointmentService.getAllAppointments();

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    // Update Appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO updatedAppointment =
                appointmentService.updateAppointment(id, request);

        return new ResponseEntity<>(
                updatedAppointment,
                HttpStatus.OK
        );
    }

    // Cancel Appointment
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(
            @PathVariable Long id) {

        AppointmentResponseDTO cancelledAppointment =
                appointmentService.cancelAppointment(id);

        return new ResponseEntity<>(
                cancelledAppointment,
                HttpStatus.OK
        );
    }

    // Confirm Appointment
    @PutMapping("/{id}/confirm")
    public ResponseEntity<AppointmentResponseDTO> confirmAppointment(
            @PathVariable Long id) {

        AppointmentResponseDTO confirmedAppointment =
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
