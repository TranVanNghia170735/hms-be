package com.hms.profile.api;


import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.dto.PatientDTO;
import com.hms.profile.exception.HmsException;
import com.hms.profile.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profile/patient")
@Validated
@RequiredArgsConstructor

public class PatientAPI {

    private final PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<Long> addPatient (@RequestBody PatientDTO patientDTO) throws HmsException {
        return new ResponseEntity<>(patientService.addPatient(patientDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PatientDTO> getPatientById (@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO dto)
            throws HmsException {
        return new ResponseEntity<>(patientService.updatePatient(dto), HttpStatus.OK);
    }


    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> patientExists(@PathVariable Long id) throws HmsException{
        return new ResponseEntity<>(patientService.patientExists(id), HttpStatus.OK);
    }

    @GetMapping("/getPatientsById")
    public ResponseEntity<List<DoctorDropdown>> getPatientsById(@RequestParam List<Long> ids) throws  HmsException {
        return new ResponseEntity<>(patientService.getPatientsById(ids), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PatientDTO>> getAllPatients() throws HmsException {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }



}
