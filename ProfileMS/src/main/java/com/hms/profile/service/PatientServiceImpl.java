package com.hms.profile.service;

import com.hms.profile.dto.PatientDTO;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

    private final PatientRepository patientRepository;

    @Override
    public Long addPatient(PatientDTO patientDTO) throws HmsException {
        if(patientDTO.getEmail()!=null && patientRepository.findByEmail(patientDTO.getEmail()).isPresent())
            throw new HmsException("PATIENT_ALREADY_EXISTS");
        if(patientDTO.getAadharNo()!=null && patientRepository.findByAadharNo(patientDTO.getAadharNo()).isPresent())
            throw new HmsException("PATIENT_ALREADY_EXISTS");
        return patientRepository.save(patientDTO.toEntity()).getId();
    }

    @Override
    public PatientDTO getPatientById(Long id) throws HmsException {
        return patientRepository.findById(id).orElseThrow(()-> new HmsException("PATIENT_NOT_FOUND")).toDTO();
    }
}
