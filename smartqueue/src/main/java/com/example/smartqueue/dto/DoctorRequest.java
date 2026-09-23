package com.example.smartqueue.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DoctorRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit phone number"
    )
    private String phone;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;

    @DecimalMin(value = "0.0", inclusive = true,
            message = "Consultation fee cannot be negative")
    private BigDecimal consultationFee;
}