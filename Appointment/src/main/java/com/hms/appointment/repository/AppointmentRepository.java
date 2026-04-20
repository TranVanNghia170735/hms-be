package com.hms.appointment.repository;

import com.hms.appointment.dto.ApointmentDetails;
import com.hms.appointment.dto.MonthlyVisitDTO;
import com.hms.appointment.dto.ReasonCountDTO;
import com.hms.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query("SELECT new com.hms.appointment.dto.ApointmentDetails(a.id, a.patientId, null, null, null, a.doctorId, null, a.appointmentTime, a.status, a.reason, a.notes) FROM Appointment a WHERE a.patientId = ?1")
    List<ApointmentDetails> findAllByPatientId(Long patientId);

    @Query("SELECT new com.hms.appointment.dto.ApointmentDetails(a.id, a.patientId, null, null, null, a.doctorId, null, a.appointmentTime, a.status, a.reason, a.notes) FROM Appointment a WHERE a.doctorId = ?1")
    List<ApointmentDetails> findAllByDoctorId(Long doctorId);

//    @Query("Select new com.hms.appointment.dto.MonthlyVisitDTO(CAST(FUNCTION('MONTHNAME', a.appointmentTime) as String), COUNT(a)) " +
//            "FROM Appointment a WHERE a.patientId=?1 AND YEAR(a.appointmentTime)=YEAR(CURRENT_DATE) " +
//            "GROUP BY FUNCTION('MONTH', a.appointmentTime),CAST(FUNCTION('MONTHNAME', a.appointmentTime) as String) ORDER BY FUNCTION('MONTH', a.appointmentTime)"
//            )
//    List<MonthlyVisitDTO> countCurrentYearVisitsByPatient(Long patientId);

//    @Query("""
//SELECT new com.hms.appointment.dto.MonthlyVisitDTO(
//    FUNCTION('TO_CHAR', a.appointmentTime, 'Month'),
//    COUNT(a)
//)
//FROM Appointment a
//WHERE a.patientId = ?1
//AND EXTRACT(YEAR FROM a.appointmentTime) = EXTRACT(YEAR FROM CURRENT_DATE)
//GROUP BY
//    EXTRACT(MONTH FROM a.appointmentTime),
//    FUNCTION('TO_CHAR', a.appointmentTime, 'Month')
//ORDER BY EXTRACT(MONTH FROM a.appointmentTime)
//""")
//    List<MonthlyVisitDTO> countCurrentYearVisitsByPatient(Long patientId);

    @Query("""
    SELECT new com.hms.appointment.dto.MonthlyVisitDTO(
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string),
        COUNT(a)
    )
    FROM Appointment a
    WHERE a.patientId = ?1
    AND EXTRACT(YEAR FROM a.appointmentTime) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY 
        EXTRACT(MONTH FROM a.appointmentTime),
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string)
    ORDER BY EXTRACT(MONTH FROM a.appointmentTime)
""")
    List<MonthlyVisitDTO> countCurrentYearVisitsByPatient(Long patientId);

    @Query("""
    SELECT new com.hms.appointment.dto.MonthlyVisitDTO(
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string),
        COUNT(a)
    )
    FROM Appointment a
    WHERE a.doctorId = ?1
    AND EXTRACT(YEAR FROM a.appointmentTime) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY 
        EXTRACT(MONTH FROM a.appointmentTime),
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string)
    ORDER BY EXTRACT(MONTH FROM a.appointmentTime)
""")
    List<MonthlyVisitDTO> countCurrentYearVisitsByDoctor(Long doctorId);

    @Query("""
    SELECT new com.hms.appointment.dto.MonthlyVisitDTO(
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string),
        COUNT(a)
    )
    FROM Appointment a
    WHERE EXTRACT(YEAR FROM a.appointmentTime) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY 
        EXTRACT(MONTH FROM a.appointmentTime),
        CAST(FUNCTION('TO_CHAR', a.appointmentTime, 'FMMonth') AS string)
    ORDER BY EXTRACT(MONTH FROM a.appointmentTime)
""")
    List<MonthlyVisitDTO> countCurrentYearVisits();


    @Query("SELECT new com.hms.appointment.dto.ReasonCountDTO(a.reason, COUNT(a)) FROM Appointment a " +
            "WHERE a.patientId = ?1 GROUP BY a.reason")
    List<ReasonCountDTO> countReasonsByPatientId(Long patientId);

    @Query("SELECT new com.hms.appointment.dto.ReasonCountDTO(a.reason, COUNT(a)) FROM Appointment a " +
            "WHERE a.doctorId = ?1 GROUP BY a.reason")
    List<ReasonCountDTO> countReasonsByDoctorId(Long doctorId);


    @Query("SELECT new com.hms.appointment.dto.ReasonCountDTO(a.reason, COUNT(a)) FROM Appointment a " +
            "GROUP BY a.reason")
    List<ReasonCountDTO> countReasons();

    List<Appointment> findByAppointmentTimeBetween (LocalDateTime startOfDay, LocalDateTime endOfDay);

}
