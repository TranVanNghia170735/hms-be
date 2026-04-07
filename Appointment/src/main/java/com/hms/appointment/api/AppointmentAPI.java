package com.hms.appointment.api;

import com.hms.appointment.dto.ApointmentDetails;
import com.hms.appointment.dto.AppointmentDTO;
import com.hms.appointment.exception.HmsException;
import com.hms.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/appointment")
@Validated
@RequiredArgsConstructor
public class AppointmentAPI {

    private final AppointmentService appointmentService;

    @PostMapping("/schedule")
    public ResponseEntity<Long> scheduleAppointment(@RequestBody AppointmentDTO appointmentDTO) throws HmsException {
        return new ResponseEntity<>(appointmentService.scheduleAppointment(appointmentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) throws HmsException{
        appointmentService.cancelAppointment(appointmentId);
        return new ResponseEntity<>("APPOINTMENT_CANCELLED", HttpStatus.OK);
    }

    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<AppointmentDTO> getAppointmentDetails (@PathVariable Long appointmentId) throws HmsException{
        return new ResponseEntity<>(appointmentService.getAppointmentDetails(appointmentId), HttpStatus.OK);

    }

    @GetMapping("/get/details/{appointmentId}")
    public ResponseEntity<ApointmentDetails> getAppointmentDetailsWithName(@PathVariable Long appointmentId) throws HmsException {
        return new ResponseEntity<>(appointmentService.getAppointmentDetailsWithName(appointmentId), HttpStatus.OK);

    }

    @GetMapping("/getAllByPatient/{patientId}")
    public ResponseEntity<List<ApointmentDetails>> getAllAppointmentsByPatientId(@PathVariable Long patientId) throws HmsException{
        return new ResponseEntity<>(appointmentService.getAllAppointmentsByPatientId(patientId), HttpStatus.OK);

    }

    @GetMapping("/getAllByDoctor/{patientId}")
    public ResponseEntity<List<ApointmentDetails>> getAllAppointmentsByDoctorId(@PathVariable Long patientId) throws HmsException{
        return new ResponseEntity<>(appointmentService.getAllAppointmentsByDoctorId(patientId), HttpStatus.OK);

    }



}
