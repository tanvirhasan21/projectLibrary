package com.example.basicauthentication.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate srStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate srEndDate;
}
