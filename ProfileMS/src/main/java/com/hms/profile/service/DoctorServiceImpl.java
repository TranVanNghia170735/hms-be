package com.hms.profile.service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.entity.Doctor;
import com.hms.profile.exception.HmsException;
import com.hms.profile.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;

    @Override
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException {
        if(doctorDTO.getEmail()!=null && doctorRepository.findByEmail(doctorDTO.getEmail()).isPresent())
            throw new HmsException("DOCTOR_ALREADY_EXISTS");
        if(doctorDTO.getEmail()!=null &&  doctorRepository.findByLicenseNo(doctorDTO.getLicenseNo()).isPresent())
            throw new HmsException("DOCTOR_ALREADY_EXISTS");
        return doctorRepository.save(doctorDTO.toEntity()).getId();

    }

    @Override
    public DoctorDTO getDoctorById(Long id) throws HmsException {
        return doctorRepository.findById(id).orElseThrow(()-> new HmsException("DOCTOR_NOT_FOUND")).toDTO();
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws HmsException {
        doctorRepository.findById(doctorDTO.getId())
                .orElseThrow(() -> new HmsException("DOCTOR_NOT_FOUND"));
        return doctorRepository.save(doctorDTO.toEntity()).toDTO();
    }

    @Override
    public Boolean doctorExists(Long id) throws HmsException {
        return doctorRepository.existsById(id);
    }

    @Override
    public List<DoctorDropdown> getDoctorDropdown() throws HmsException {
        return doctorRepository.findAllDoctorDropdowns();
    }

    @Override
    public List<DoctorDropdown> getDoctorsById(List<Long> ids) throws HmsException {
        return doctorRepository.findAllDoctorDropdownsByIds(ids);
    }

    @Override
    public List<DoctorDTO> getAllDoctor() throws HmsException {
        return((List<Doctor>) doctorRepository.findAll()).stream().map(doctor 
                -> doctor.toDTO()).toList();
    }


}
