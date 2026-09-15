package com.example.smartqueue.service;

import com.example.smartqueue.dto.AvailabilityResponseDTO;
import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailabilityService(
            DoctorAvailabilityRepository doctorAvailabilityRepository) {

        this.doctorAvailabilityRepository =
                doctorAvailabilityRepository;
    }

    public AvailabilityResponseDTO createAvailability(
            DoctorAvailability availability) {

        DoctorAvailability savedAvailability =
                doctorAvailabilityRepository.save(availability);

        return mapToResponseDTO(savedAvailability);
    }

    public List<AvailabilityResponseDTO> getAllAvailability() {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository.findAll();

        return availabilityList.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public List<AvailabilityResponseDTO> getAvailabilityByDoctorId(
            Long doctorId) {

        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository.findByDoctorId(doctorId);

        return availabilityList.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public AvailabilityResponseDTO updateAvailability(
            Long id,
            DoctorAvailability updatedAvailability) {

        DoctorAvailability existingAvailability =
                doctorAvailabilityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Availability not found"));

        existingAvailability.setAvailableDate(
                updatedAvailability.getAvailableDate());

        existingAvailability.setDayOfWeek(
                updatedAvailability.getDayOfWeek());

        existingAvailability.setStartTime(
                updatedAvailability.getStartTime());

        existingAvailability.setEndTime(
                updatedAvailability.getEndTime());

        existingAvailability.setIsAvailable(
                updatedAvailability.getIsAvailable());

        DoctorAvailability savedAvailability =
                doctorAvailabilityRepository.save(
                        existingAvailability);

        return mapToResponseDTO(savedAvailability);
    }

    public void deleteAvailability(Long id) {
    }

    private AvailabilityResponseDTO mapToResponseDTO(
            DoctorAvailability availability) {

        AvailabilityResponseDTO response =
                new AvailabilityResponseDTO();

        response.setId(availability.getId());

        response.setAvailableDate(
                availability.getAvailableDate());

        response.setDayOfWeek(
                availability.getDayOfWeek());

        response.setStartTime(
                availability.getStartTime());

        response.setEndTime(
                availability.getEndTime());

        response.setIsAvailable(
                availability.getIsAvailable());

        return response;
    }
}