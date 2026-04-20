package com.hms.appointment.service;

import com.hms.appointment.clients.ProfileClient;
import com.hms.appointment.dto.*;
import com.hms.appointment.entity.Appointment;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final ApiService apiService;
    private final ProfileClient profileClient;

    @Override
    public Long scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException {
//        Boolean doctorExists = apiService.doctorExists(appointmentDTO.getDoctorId()).block();
//        Boolean patientExist = apiService.patientExists(appointmentDTO.getPatientId()).block();
        Boolean doctorExists = profileClient.doctorExists(appointmentDTO.getDoctorId());
        Boolean patientExist = profileClient.patientExists(appointmentDTO.getPatientId());
        if(doctorExists == null  || !doctorExists){
            throw new HmsException("DOCTOR_NOT_FOUND");
        }

        if(patientExist == null  || !patientExist){
            throw new HmsException("PATIENT_NOT_FOUND");
        }

        appointmentDTO.setStatus(Status.SCHEDULED);
        return appointmentRepository.save(appointmentDTO.toEntity()).getId();
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws HmsException {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new HmsException("APPOINTMENT_NOT_FOUND"));
        if(appointment.getStatus().equals(Status.CANCELLED)){
            throw new HmsException("APPOINTMENT_ALREADY_CANCELLED");
        }
        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void completeAppointment(Long appointmentId) {

    }

    @Override
    public void rescheduleAppointment(Long appointmentId, String newDateTime) {

    }

    @Override
    public AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException {
        return appointmentRepository.findById(appointmentId).orElseThrow(()-> new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
    }

    @Override
    public ApointmentDetails getAppointmentDetailsWithName(Long appointId) throws HmsException {
        AppointmentDTO appointmentDTO = appointmentRepository.findById(appointId)
                .orElseThrow(() -> new HmsException("APPOINTMENT_NOT_FOUND")).toDTO();
//        DoctorDTO doctorDTO = apiService.getDoctorById(appointmentDTO.getDoctorId()).block();
//        PatientDTO patientDTO = apiService.getPatientById(appointmentDTO.getPatientId()).block();

        DoctorDTO doctorDTO = profileClient.getDoctorById(appointmentDTO.getDoctorId());
        PatientDTO patientDTO = profileClient.getPatientById(appointmentDTO.getPatientId());

        return new ApointmentDetails(appointmentDTO.getId(), appointmentDTO.getPatientId(),
                patientDTO.getName(),patientDTO.getEmail(),patientDTO.getPhone(),
                appointmentDTO.getDoctorId(), doctorDTO.getName(),
                appointmentDTO.getAppointmentTime(), appointmentDTO.getStatus(),
                appointmentDTO.getReason(), appointmentDTO.getNotes());
    }

    @Override
    public List<ApointmentDetails> getAllAppointmentsByPatientId(Long patientId) throws HmsException {
        return appointmentRepository.findAllByPatientId(patientId).stream()
                .map(appointment -> {
                    DoctorDTO doctorDTO = profileClient.getDoctorById(appointment.getDoctorId());
                    appointment.setDoctorName(doctorDTO.getName());
                    return appointment;
                }).toList();
    }

    @Override
    public List<ApointmentDetails> getAllAppointmentsByDoctorId(Long doctorId) throws HmsException {
        return appointmentRepository.findAllByDoctorId(doctorId).stream().map(appointment -> {
            PatientDTO patientDTO = profileClient.getPatientById(appointment.getPatientId());
            appointment.setPatientName(patientDTO.getName());
            appointment.setPatientEmail(patientDTO.getEmail());
            appointment.setPatientPhone(patientDTO.getPhone());
            return appointment;
        }).toList();
    }

    @Override
    public List<MonthlyVisitDTO> getAppointmentCountByPatient(Long patientId) throws HmsException {
        return appointmentRepository.countCurrentYearVisitsByPatient(patientId);
    }

    @Override
    public List<ReasonCountDTO> getReasonCountByPatient(Long patientDTO) {
        return appointmentRepository.countReasonsByPatientId(patientDTO);
    }

    @Override
    public List<ReasonCountDTO> getReasonCountByDoctor(Long doctorId) {
        return appointmentRepository.countReasonsByDoctorId(doctorId);
    }

    @Override
    public List<MonthlyVisitDTO> getAppointmentCountByDoctor(Long doctorId) throws HmsException {
        return appointmentRepository.countCurrentYearVisitsByDoctor(doctorId);
    }

    @Override
    public List<MonthlyVisitDTO> getAppointmentCounts() throws HmsException {
        return appointmentRepository.countCurrentYearVisits();
    }

    @Override
    public List<ReasonCountDTO> getReasonCount() {
        return appointmentRepository.countReasons();
    }

    @Override
    public List<ApointmentDetails> getTodaysAppointments() {
        LocalDate today = LocalDate.now();
        LocalDateTime startDay = today.atStartOfDay();
        LocalDateTime endDay = today.atTime(LocalTime.MAX);
        return appointmentRepository.findByAppointmentTimeBetween(startDay, endDay).stream().map(appointment -> {
            DoctorDTO doctorDTO = profileClient.getDoctorById(appointment.getDoctorId());
            PatientDTO patientDTO = profileClient.getPatientById(appointment.getPatientId());
            return new ApointmentDetails(appointment.getId(), appointment.getPatientId(), patientDTO.getName(),
                    patientDTO.getEmail(), patientDTO.getPhone(), appointment.getDoctorId(), doctorDTO.getName(),
                    appointment.getAppointmentTime(), appointment.getStatus(), appointment.getReason(),
                    appointment.getNotes());
        } ).toList();
    }


}
