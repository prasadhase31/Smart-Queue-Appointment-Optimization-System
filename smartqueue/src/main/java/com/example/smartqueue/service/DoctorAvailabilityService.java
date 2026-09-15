package com.example.smartqueue.service;

import com.example.smartqueue.entity.DoctorAvailability;
import com.example.smartqueue.repository.DoctorAvailabilityRepository;
import org.springframework.stereotype.Service;
import com.example.smartqueue.dto.AvailabilityResponseDTO;

import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
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

    public List<DoctorAvailability> getAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctorId(doctorId);
    }

    public DoctorAvailability updateAvailability(
            Long id,
            DoctorAvailability updatedAvailability) {
        return null;
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