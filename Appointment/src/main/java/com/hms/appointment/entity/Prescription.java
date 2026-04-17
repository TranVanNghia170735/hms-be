package com.hms.appointment.entity;


import com.hms.appointment.dto.PrescriptionDTO;
import com.hms.appointment.dto.PrescriptionDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long patientId;
    private Long doctorId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    private LocalDate prescriptionDate;
    private String notes;

    public Prescription(Long id){
        this.id = id;
    }

    public PrescriptionDTO toDTO() {
        return new PrescriptionDTO(this.id, this.patientId, this.doctorId, appointment.getId(),
                this.prescriptionDate, this.notes, null);
    }

    public PrescriptionDetails toDetails(){
        return new PrescriptionDetails(id, patientId, doctorId, null,null, appointment.getId(),
                prescriptionDate, notes, null);
    }
}
