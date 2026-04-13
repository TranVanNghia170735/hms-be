package com.hms.pharmacy.dto;

import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.entity.Sale;
import com.hms.pharmacy.entity.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemDTO {
    private Long id;
    private Long saleId;
    private Long medicineId;
    private String batchNo;
    private Integer quantity;
    private Double unitPrice;

    public SaleItem toEntity() {
        return new SaleItem(this.id, new Sale(this.saleId),
                new Medicine(this.medicineId), batchNo, quantity, unitPrice);
    }

}
