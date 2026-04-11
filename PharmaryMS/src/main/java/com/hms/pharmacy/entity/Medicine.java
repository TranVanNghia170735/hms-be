package com.hms.pharmacy.entity;


import com.hms.pharmacy.dto.MedicineDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dosage; //e.g 500mg, 5ml

    @Enumerated(EnumType.STRING)
//    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="category")
    private MedicineCategory category; //e.g ANTIBIOTIC, ANALGESIC

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="type")
    private MedicineType type; //e.g TABLET, SYRUP
    private String manufacturer;
    private Integer unitPrice;
    private LocalDateTime createAt; // time for record creation

    public Medicine(Long id){
        this.id = id;
    }

    public MedicineDTO toDTO(){
        return new MedicineDTO(id, name, dosage, category, type, manufacturer, unitPrice, createAt);
    }
}
