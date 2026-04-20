package com.hms.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCountsDTO {
    private List<MonthlyRoleCountDTO> doctorCounts;
    private List<MonthlyRoleCountDTO> patientCounts;

}
