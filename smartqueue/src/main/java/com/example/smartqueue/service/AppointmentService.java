package com.example.smartqueue.service;

import com.example.smartqueue.entity.Appointment;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.entity.User;
import com.example.smartqueue.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import com.example.smartqueue.repository.UserRepository;
import com.example.smartqueue.repository.DoctorRepository;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            DoctorAvailabilityRepository doctorAvailabilityRepository) {

        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }

    // Create Appointment
    public Appointment createAppointment(Appointment appointment) {

        User patient = userRepository.findById(
                appointment.getPatient().getId()
        ).orElseThrow(() ->
                new RuntimeException("Patient not found")
        );

        Doctor doctor = doctorRepository.findById(
                appointment.getDoctor().getId()
        ).orElseThrow(() ->
                new RuntimeException("Doctor not found")
        );

        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(
                        appointment.getAvailability().getId()
                ).orElseThrow(() ->
                        new RuntimeException("Availability not found")
                );

        if (!availability.getDoctor().getId().equals(doctor.getId())) {
            throw new RuntimeException(
                    "This availability does not belong to this doctor"
            );
        }
        appointment.setPatient(patient);

        System.out.println("Appointment Date: "
                + appointment.getAppointmentDate());

        System.out.println("Availability Date: "
                + availability.getAvailableDate());

        if (!availability.getAvailableDate()
                .equals(appointment.getAppointmentDate())) {

            throw new RuntimeException(
                    "Appointment date does not match doctor's availability date"
            );
        }

        if (!appointment.getAppointmentDate()
                .equals(availability.getAvailableDate())) {

            throw new RuntimeException(
                    "Appointment date does not match doctor's availability date"
            );
        }

// Time Validation
        if (appointment.getAppointmentTime().isBefore(availability.getStartTime())
                || appointment.getAppointmentTime().isAfter(availability.getEndTime())) {

            throw new RuntimeException(
                    "Appointment time is outside doctor's availability time"
            );
        }

        appointment.setDoctor(doctor);

        appointment.setDoctor(doctor);
        appointment.setAvailability(availability);


        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctor.getId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime()
                        );

        if (alreadyBooked) {
            throw new RuntimeException(
                    "This doctor is already booked for this date and time"
            );
        }

        return appointmentRepository.save(appointment);

    }



    // Get All Appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment updateAppointment(
            Long id,
            Appointment updatedAppointment) {

        Appointment existingAppointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        existingAppointment.setAppointmentDate(
                updatedAppointment.getAppointmentDate());

        existingAppointment.setAppointmentTime(
                updatedAppointment.getAppointmentTime());

        existingAppointment.setStatus(
                updatedAppointment.getStatus());

        existingAppointment.setReason(
                updatedAppointment.getReason());

        return appointmentRepository.save(existingAppointment);
    }

    public Appointment cancelAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException(
                    "Appointment is already cancelled"
            );
        }

        appointment.setStatus("CANCELLED");

        return appointmentRepository.save(appointment);
    }

    public Appointment confirmAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException(
                    "Cancelled appointment cannot be confirmed"
            );
        }

        appointment.setStatus("CONFIRMED");

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }

        appointmentRepository.deleteById(id);
    }
}