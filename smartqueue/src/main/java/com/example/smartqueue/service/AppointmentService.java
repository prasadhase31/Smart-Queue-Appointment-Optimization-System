package com.example.smartqueue.service;

import com.example.smartqueue.dto.AppointmentRequestDTO;
import com.example.smartqueue.entity.Appointment;
import com.example.smartqueue.entity.Doctor;
import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.entity.User;
import com.example.smartqueue.exception.BadRequestException;
import com.example.smartqueue.exception.ResourceNotFoundException;
import com.example.smartqueue.repository.AppointmentRepository;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import com.example.smartqueue.repository.DoctorRepository;
import com.example.smartqueue.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.smartqueue.dto.AppointmentResponseDTO;
import com.example.smartqueue.dto.UserResponseDTO;
import com.example.smartqueue.dto.DoctorResponseDTO;
import com.example.smartqueue.dto.AvailabilityResponseDTO;
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
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {

        // Find Patient
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found")
                );

        // Find Doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        if (!"ACTIVE".equalsIgnoreCase(doctor.getStatus())) {
            throw new BadRequestException(
                    "Inactive doctor cannot accept new appointments"
            );
        }

        // Past date validation
        if (request.getAppointmentDate().isBefore(java.time.LocalDate.now())) {
            throw new BadRequestException(
                    "Appointment date cannot be in the past"
            );
        }

        if (request.getAppointmentDate().equals(java.time.LocalDate.now())
                && request.getAppointmentTime().isBefore(java.time.LocalTime.now())) {

            throw new BadRequestException(
                    "Appointment time cannot be in the past"
            );
        }

        // Find Availability
        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(
                        request.getAvailabilityId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException("Availability not found")
                );

        // Doctor ↔ Availability validation
        if (!availability.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException(
                    "This availability does not belong to this doctor"
            );
        }

        // Date validation
        if (!request.getAppointmentDate()
                .equals(availability.getAvailableDate())) {

            throw new BadRequestException(
                    "Appointment date does not match doctor's availability date"
            );
        }

        // Time validation
        if (request.getAppointmentTime().isBefore(availability.getStartTime())
                || request.getAppointmentTime().isAfter(availability.getEndTime())) {

            throw new BadRequestException(
                    "Appointment time is outside doctor's availability time"
            );
        }

        // Duplicate booking validation
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (alreadyBooked) {
            throw new BadRequestException(
                    "This doctor is already booked for this date and time"
            );
        }

        // DTO → Entity
        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAvailability(availability);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());

        // Default status
        appointment.setStatus("BOOKED");

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponseDTO(savedAppointment);
    }

    // Get All Appointments
    public List<AppointmentResponseDTO> getAllAppointments() {

        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // Update Appointment

    // Update Appointment
    // Update Appointment
    public AppointmentResponseDTO updateAppointment(
            Long id,
            AppointmentRequestDTO request) {

        // 1. Find existing appointment
        Appointment existingAppointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: " + id
                                )
                        );

        // 2. Cancelled appointment cannot be updated
        if ("CANCELLED".equals(existingAppointment.getStatus())) {

            throw new BadRequestException(
                    "Cancelled appointment cannot be updated"
            );
        }

        // 3. Past date validation
        if (request.getAppointmentDate()
                .isBefore(java.time.LocalDate.now())) {

            throw new BadRequestException(
                    "Appointment date cannot be in the past"
            );
        }

        // 4. Past time validation
        if (request.getAppointmentDate()
                .equals(java.time.LocalDate.now())
                && request.getAppointmentTime()
                .isBefore(java.time.LocalTime.now())) {

            throw new BadRequestException(
                    "Appointment time cannot be in the past"
            );
        }

        // 5. Find patient
        User patient =
                userRepository.findById(request.getPatientId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + request.getPatientId()
                                )
                        );

        // 6. Find doctor
        Doctor doctor =
                doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: "
                                                + request.getDoctorId()
                                )
                        );

        // 7. Doctor must be ACTIVE
        if (!"ACTIVE".equalsIgnoreCase(doctor.getStatus())) {

            throw new BadRequestException(
                    "Inactive doctor cannot accept appointments"
            );
        }

        // 8. Find availability
        DoctorAvailability availability =
                doctorAvailabilityRepository.findById(
                                request.getAvailabilityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability not found with id: "
                                                + request.getAvailabilityId()
                                )
                        );

        // 9. Doctor ↔ Availability validation
        if (!availability.getDoctor().getId()
                .equals(doctor.getId())) {

            throw new BadRequestException(
                    "This availability does not belong to this doctor"
            );
        }

        // 10. Date validation
        if (!request.getAppointmentDate()
                .equals(availability.getAvailableDate())) {

            throw new BadRequestException(
                    "Appointment date does not match doctor's availability date"
            );
        }

        // 11. Time validation
        if (request.getAppointmentTime()
                .isBefore(availability.getStartTime())
                || request.getAppointmentTime()
                .isAfter(availability.getEndTime())) {

            throw new BadRequestException(
                    "Appointment time is outside doctor's availability time"
            );
        }

        // 12. Duplicate booking validation
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndIdNot(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                id
                        );

        if (alreadyBooked) {

            throw new BadRequestException(
                    "This doctor is already booked for this date and time"
            );
        }

        // 13. Update appointment
        existingAppointment.setPatient(patient);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setAvailability(availability);
        existingAppointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        existingAppointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        existingAppointment.setReason(
                request.getReason()
        );

        // 14. Save
        Appointment updatedAppointment =
                appointmentRepository.save(existingAppointment);

        // 15. Return DTO
        return mapToResponseDTO(updatedAppointment);
    }

    // Cancel Appointment
    public AppointmentResponseDTO cancelAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: " + id
                                )
                        );

        // Check already cancelled
        if ("CANCELLED".equals(appointment.getStatus())) {

            throw new BadRequestException(
                    "Appointment is already cancelled"
            );
        }

        appointment.setStatus("CANCELLED");

        Appointment cancelledAppointment =
                appointmentRepository.save(appointment);

        return mapToResponseDTO(cancelledAppointment);
    }


    // Confirm Appointment
    public AppointmentResponseDTO confirmAppointment(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: " + id
                                )
                        );

        // Cancelled appointment cannot be confirmed
        if ("CANCELLED".equals(appointment.getStatus())) {

            throw new BadRequestException(
                    "Cancelled appointment cannot be confirmed"
            );
        }

        // Already confirmed
        if ("CONFIRMED".equals(appointment.getStatus())) {

            throw new BadRequestException(
                    "Appointment is already confirmed"
            );
        }

        appointment.setStatus("CONFIRMED");

        Appointment confirmedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponseDTO(confirmedAppointment);
    }


    // Delete Appointment
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Appointment not found with id: " + id
            );
        }

        appointmentRepository.deleteById(id);
    }

    private AppointmentResponseDTO mapToResponseDTO(
            Appointment appointment) {

        AppointmentResponseDTO response =
                new AppointmentResponseDTO();

        response.setId(appointment.getId());
        response.setAppointmentDate(
                appointment.getAppointmentDate()
        );
        response.setAppointmentTime(
                appointment.getAppointmentTime()
        );
        response.setReason(
                appointment.getReason()
        );
        response.setStatus(
                appointment.getStatus()
        );

        // Patient DTO
        if (appointment.getPatient() != null) {

            UserResponseDTO patient =
                    new UserResponseDTO();

            patient.setId(appointment.getPatient().getId());
            patient.setName(appointment.getPatient().getName());
            patient.setEmail(appointment.getPatient().getEmail());
            patient.setPhone(appointment.getPatient().getPhone());
            patient.setRole(appointment.getPatient().getRole());

            response.setPatient(patient);
        }

        // Doctor DTO
        if (appointment.getDoctor() != null) {

            DoctorResponseDTO doctor =
                    new DoctorResponseDTO();

            doctor.setId(appointment.getDoctor().getId());
            doctor.setName(appointment.getDoctor().getName());
            doctor.setEmail(appointment.getDoctor().getEmail());
            doctor.setPhone(appointment.getDoctor().getPhone());
            doctor.setSpecialization(
                    appointment.getDoctor().getSpecialization()
            );
            doctor.setConsultationFee(
                    appointment.getDoctor().getConsultationFee()
            );
            doctor.setStatus(
                    appointment.getDoctor().getStatus()
            );
            doctor.setCreatedAt(
                    appointment.getDoctor().getCreatedAt()
            );

            response.setDoctor(doctor);
        }

        // Availability DTO
        if (appointment.getAvailability() != null) {

            AvailabilityResponseDTO availability =
                    new AvailabilityResponseDTO();

            availability.setId(
                    appointment.getAvailability().getId()
            );
            availability.setAvailableDate(
                    appointment.getAvailability().getAvailableDate()
            );
            availability.setDayOfWeek(
                    appointment.getAvailability().getDayOfWeek()
            );
            availability.setStartTime(
                    appointment.getAvailability().getStartTime()
            );
            availability.setEndTime(
                    appointment.getAvailability().getEndTime()
            );
            availability.setIsAvailable(
                    appointment.getAvailability().getIsAvailable()
            );

            response.setAvailability(availability);
        }

        return response;
    }
}