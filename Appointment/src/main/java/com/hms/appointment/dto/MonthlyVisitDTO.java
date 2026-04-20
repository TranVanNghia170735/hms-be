package com.hms.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonthlyVisitDTO {
    private String month;
    private Long count;

    public MonthlyVisitDTO(String month, Long count) {
        this.month = month;
        this.count = count;
    }
}
