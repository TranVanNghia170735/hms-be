package com.hms.profile.service;

import com.hms.profile.dto.DoctorDTO;
import com.hms.profile.dto.DoctorDropdown;
import com.hms.profile.dto.PatientDTO;
import com.hms.profile.exception.HmsException;

import java.util.List;

public interface DoctorService {
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException;
    public DoctorDTO getDoctorById(Long id) throws HmsException;
    public DoctorDTO updateDoctor(DoctorDTO doctor) throws HmsException;
    public Boolean doctorExists(Long id) throws HmsException;
    public List<DoctorDropdown> getDoctorDropdown() throws HmsException;

}
