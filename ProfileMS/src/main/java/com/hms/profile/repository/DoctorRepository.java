package com.hms.profile.repository;

import com.hms.profile.entity.Doctor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor>findByEmail(String email);
    Optional<Doctor> findByLicenseNo(String licenseNo);
}
