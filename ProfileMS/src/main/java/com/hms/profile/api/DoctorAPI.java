package com.hms.profile.api;


import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.dto.PatientDTO;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.DoctorService;
import com.hms.profile.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profile/doctor")
@Validated
@RequiredArgsConstructor
public class DoctorAPI {

    private final DoctorService doctorService;

    @PostMapping("/add")
    public ResponseEntity<Long> addDoctor (@RequestBody DoctorDTO doctorDTO) throws HmsException {
        return new ResponseEntity<>(doctorService.addDoctor(doctorDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById (@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(doctorService.getDoctorById(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorDTO doctorDTO) throws HmsException {
        return new ResponseEntity<>(doctorService.updateDoctor(doctorDTO), HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> doctorExists(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(doctorService.doctorExists(id), HttpStatus.OK);
    }

    @GetMapping("/dropdowns")
    public ResponseEntity<List<DoctorDropdown>> getDoctorDropdowns() throws HmsException {
        return new ResponseEntity<>(doctorService.getDoctorDropdown(), HttpStatus.OK);
    }

    @GetMapping("/getDoctorsById")
    public ResponseEntity<List<DoctorDropdown>> getDoctorsById(@RequestParam List<Long> ids) throws  HmsException {
        return new ResponseEntity<>(doctorService.getDoctorsById(ids), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() throws HmsException {
        return new ResponseEntity<>(doctorService.getAllDoctor(), HttpStatus.OK);
    }


}
