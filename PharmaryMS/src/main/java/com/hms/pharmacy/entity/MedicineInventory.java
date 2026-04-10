package com.hms.pharmacy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicineInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medicine_id", nullable = false)
    private Medicine medicine; // Linked to Medicine entity
    private String batchNo; //Batch number of the medicine
    private Integer quantity; //Quantity in stock
    private LocalDate expiryDate; // Expiry date of the batch
    private LocalDate addedDate; //Date when the batch was added to inventory.

}
