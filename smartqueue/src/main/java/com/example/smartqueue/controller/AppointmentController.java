package com.example.smartqueue.controller;

import com.example.smartqueue.entity.Appointment;
import com.example.smartqueue.service.AppointmentService;
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
            @RequestBody Appointment appointment) {

        Appointment savedAppointment =
                appointmentService.createAppointment(appointment);

        return new ResponseEntity<>(
                savedAppointment,
                HttpStatus.CREATED
        );
    }

    // Get All Appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {

        List<Appointment> appointments =
                appointmentService.getAllAppointments();

        return new ResponseEntity<>(
                appointments,
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment) {

        Appointment updatedAppointment =
                appointmentService.updateAppointment(
                        id,
                        appointment
                );

        return new ResponseEntity<>(
                updatedAppointment,
                HttpStatus.OK
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok(
                "Appointment deleted successfully"
        );
    }
}