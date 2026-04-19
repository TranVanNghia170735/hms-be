package com.hms.user.clients;

import com.hms.user.config.FeignClientInterceptor;
import com.hms.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ProfileMS", configuration = FeignClientInterceptor.class)
public interface ProfileClient {
    @PostMapping("/profile/patient/add")
    Long addPatientProfile(@RequestBody UserDTO userDTO);

    @PostMapping("/profile/doctor/add")
    Long addDoctorProfile(@RequestBody UserDTO userDTO);

    @GetMapping("/profile/doctor/getProfileId/{id}")
    Long getDoctor(@PathVariable("id") Long id);

    @GetMapping("/profile/patient/getProfileId/{id}")
    Long getPatient(@PathVariable("id") Long id);


}
