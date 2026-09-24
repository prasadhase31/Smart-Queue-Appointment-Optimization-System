package com.example.smartqueue.service;

import com.example.smartqueue.dto.AvailabilityResponseDTO;
import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import com.example.smartqueue.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final DoctorRepository doctorRepository;

    public DoctorAvailabilityService(
            DoctorAvailabilityRepository doctorAvailabilityRepository,
            DoctorRepository doctorRepository) {

        this.doctorAvailabilityRepository =
                doctorAvailabilityRepository;

        this.doctorRepository =
                doctorRepository;
    }


    // =========================
    // CREATE AVAILABILITY
    // =========================

    public AvailabilityResponseDTO createAvailability(
            DoctorAvailability availability) {

        Doctor doctor = doctorRepository.findById(
                availability.getDoctor().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Doctor not found with id: "
                                + availability.getDoctor().getId()
                )
        );

        if (!"ACTIVE".equalsIgnoreCase(doctor.getStatus())) {
            throw new BadRequestException(
                    "Inactive doctor cannot have new availability"
            );
        }

        if (availability.getAvailableDate()
                .getDayOfWeek()
                != availability.getDayOfWeek()) {

            throw new RuntimeException(
                    "Day of week does not match available date"
            );
        }


        // 1. Start time must be before end time
        if (!availability.getStartTime()
                .isBefore(availability.getEndTime())) {

            throw new RuntimeException(
                    "Start time must be before end time"
            );
        }


        // 2. Check exact duplicate availability
        boolean exists =
                doctorAvailabilityRepository
                        .existsByDoctorIdAndAvailableDateAndStartTimeAndEndTime(
                                availability.getDoctor().getId(),
                                availability.getAvailableDate(),
                                availability.getStartTime(),
                                availability.getEndTime()
                        );

        if (exists) {

            throw new RuntimeException(
                    "Availability already exists for this doctor on this date and time"
            );
        }


        // 3. Check overlapping availability
        boolean overlapping =
                doctorAvailabilityRepository
                        .existsOverlappingAvailability(
                                availability.getDoctor().getId(),
                                availability.getAvailableDate(),
                                availability.getStartTime(),
                                availability.getEndTime()
                        );

        if (overlapping) {

            throw new RuntimeException(
                    "Availability overlaps with an existing availability"
            );
        }


        // 4. Save availability
        DoctorAvailability savedAvailability =
                doctorAvailabilityRepository.save(availability);


        // 5. Return DTO
        return mapToResponseDTO(savedAvailability);
    }


    // =========================
    // GET ALL AVAILABILITY
    // =========================

    public List<AvailabilityResponseDTO> getAllAvailability() {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository.findAll();

        return availabilityList.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }


    // =========================
    // GET AVAILABILITY BY DOCTOR
    // =========================

    public List<AvailabilityResponseDTO> getAvailabilityByDoctorId(
            Long doctorId) {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository.findByDoctorId(doctorId);

        return availabilityList.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }


    // =========================
    // UPDATE AVAILABILITY
    // =========================

    public AvailabilityResponseDTO updateAvailability(
            Long id,
            DoctorAvailability updatedAvailability) {


        // 1. Find existing availability
        DoctorAvailability existingAvailability =
                doctorAvailabilityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Availability not found"
                                ));



        if (updatedAvailability.getAvailableDate()
                .getDayOfWeek()
                != updatedAvailability.getDayOfWeek()) {

            throw new RuntimeException(
                    "Day of week does not match available date"
            );
        }

        // 2. Start time must be before end time
        if (!updatedAvailability.getStartTime()
                .isBefore(updatedAvailability.getEndTime())) {

            throw new RuntimeException(
                    "Start time must be before end time"
            );
        }


        // 3. Check overlapping availability
        // Existing record itself is excluded using ID
        boolean overlapping =
                doctorAvailabilityRepository
                        .existsOverlappingAvailabilityForUpdate(
                                id,
                                existingAvailability.getDoctor().getId(),
                                updatedAvailability.getAvailableDate(),
                                updatedAvailability.getStartTime(),
                                updatedAvailability.getEndTime()
                        );

        if (overlapping) {

            throw new RuntimeException(
                    "Availability overlaps with an existing availability"
            );
        }


        // 4. Update fields
        existingAvailability.setAvailableDate(
                updatedAvailability.getAvailableDate()
        );

        existingAvailability.setDayOfWeek(
                updatedAvailability.getDayOfWeek()
        );

        existingAvailability.setStartTime(
                updatedAvailability.getStartTime()
        );

        existingAvailability.setEndTime(
                updatedAvailability.getEndTime()
        );

        existingAvailability.setIsAvailable(
                updatedAvailability.getIsAvailable()
        );


        // 5. Save updated availability
        DoctorAvailability savedAvailability =
                doctorAvailabilityRepository.save(
                        existingAvailability
                );


        // 6. Return DTO
        return mapToResponseDTO(savedAvailability);
    }


    // =========================
    // DELETE AVAILABILITY
    // =========================

    public void deleteAvailability(Long id) {
        // Currently skipped as discussed
    }


    // =========================
    // ENTITY → DTO
    // =========================

    private AvailabilityResponseDTO mapToResponseDTO(
            DoctorAvailability availability) {

        AvailabilityResponseDTO response =
                new AvailabilityResponseDTO();

        response.setId(
                availability.getId()
        );

        response.setAvailableDate(
                availability.getAvailableDate()
        );

        response.setDayOfWeek(
                availability.getDayOfWeek()
        );

        response.setStartTime(
                availability.getStartTime()
        );

        response.setEndTime(
                availability.getEndTime()
        );

        response.setIsAvailable(
                availability.getIsAvailable()
        );

        return response;
    }
}