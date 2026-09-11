package com.example.smartqueue.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DoctorResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private BigDecimal consultationFee;
    private String status;
    private LocalDateTime createdAt;
}