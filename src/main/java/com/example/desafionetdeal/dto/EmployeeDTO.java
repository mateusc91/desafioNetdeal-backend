package com.example.desafionetdeal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO {
    private Long id;

    @NotNull(message = "Name may not be null")
    private String name;

    @NotNull(message = "Password may not be null")
    private String password;

    private Integer passwordScore;
    private String passwordComplexity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
