package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.dto.StockStatus;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineInventoryServiceImpl implements MedicineInventoryService{

    private final MedicineInventoryRepository medicineInventoryRepository;
    private final MedicineService medicineService;

    @Override
    public List<MedicineInventoryDTO> getAllMedicines() throws HmsException {
        List<MedicineInventory> inventories = (List<MedicineInventory>) medicineInventoryRepository.findAll();
        return inventories.stream().map(MedicineInventory::toDTO).toList();
    }

    @Override
    public MedicineInventoryDTO getMedicineById(Long id) throws HmsException {
        return medicineInventoryRepository.findById(id).orElseThrow(()-> new HmsException("INVENTORY_NOT_FOUND")).toDTO();
    }

    @Override
    public MedicineInventoryDTO addMedicine(MedicineInventoryDTO medicine) throws HmsException {
        medicine.setAddedDate(LocalDate.now());
        medicineService.addStock(medicine.getMedicineId(), medicine.getQuantity());
        medicine.setInitialQuantity(medicine.getQuantity());
        medicine.setStatus(StockStatus.ACTIVE);
        return medicineInventoryRepository.save(medicine.toEntity()).toDTO();
    }

    @Override
    public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicine) throws HmsException {
        MedicineInventory existingInventory = medicineInventoryRepository.findById(medicine.getId())
                .orElseThrow(()-> new HmsException("INVENTORY_NOT_FOUND"));
        existingInventory.setBatchNo(medicine.getBatchNo());
        if(existingInventory.getInitialQuantity() < medicine.getQuantity()){
            medicineService.addStock(medicine.getMedicineId(), medicine.getQuantity() - existingInventory.getInitialQuantity());
        } else if (existingInventory.getInitialQuantity() > medicine.getQuantity()) {
            medicineService.removeStock(medicine.getMedicineId(), existingInventory.getInitialQuantity() -medicine.getQuantity());

        }
        existingInventory.setQuantity(medicine.getQuantity());
        existingInventory.setInitialQuantity(medicine.getQuantity());
        existingInventory.setExpiryDate(medicine.getExpiryDate());

        return medicineInventoryRepository.save(existingInventory).toDTO();
    }


    @Override
    public void deleteMedicine(Long id) throws HmsException {
        medicineInventoryRepository.deleteById(id);
    }

    private void markExpired(List<MedicineInventory> inventories) throws HmsException {
        for(MedicineInventory inventory : inventories){
            inventory.setStatus(StockStatus.EXPIRED);
        }
        medicineInventoryRepository.saveAll(inventories);
    }

    @Override
    @Scheduled(cron ="0 30 23 * * ?") // seconds minutes hours dayOfMonth month dayOfWeek
    public void deleteExpiredMedicines() throws HmsException {
        System.out.println("Deleting expired medicines");
        List<MedicineInventory> expiredMedicines = medicineInventoryRepository.findByExpiryDateBefore(LocalDate.now());
        for(MedicineInventory medicineInventory : expiredMedicines){
            medicineService.removeStock(medicineInventory.getMedicine().getId(), medicineInventory.getQuantity());

        }
        this.markExpired(expiredMedicines);
    }

    @Override
    @Transactional
    public String sellStock(Long medicineId, Integer quantity) throws HmsException {
        List<MedicineInventory> inventories = medicineInventoryRepository.findByMedicineIdAndExpiryDateAfterAndQuantityGreaterThanAndStatusOrderByExpiryDateAsc(medicineId,
                LocalDate.now(), 0, StockStatus.EXPIRED);
        if(inventories.isEmpty()){
            throw  new HmsException("OUT_OF_STOCK");
        }
        StringBuilder batchDetails = new StringBuilder();
        int remainingQuantity = quantity;
        for(MedicineInventory inventory: inventories){
            if(remainingQuantity <=0){
                break;
            }
            int availableQuantity = inventory.getQuantity();
            if(availableQuantity <= remainingQuantity){
                // Use up the entire batch
                batchDetails.append(String.format("Batch %s: %d units\n", inventory.getBatchNo(),
                        availableQuantity));
                remainingQuantity -=availableQuantity;
                inventory.setQuantity(0);
                inventory.setStatus(StockStatus.EXPIRED);
            } else {
                batchDetails.append(String.format("Batch %s: %d units\n", inventory.getBatchNo(),
                        remainingQuantity));
                inventory.setQuantity(availableQuantity - remainingQuantity);
                medicineService.removeStock(medicineId, remainingQuantity);
                remainingQuantity = 0;
            }
        }
        if(remainingQuantity > 0){
            throw new HmsException("INSUFFICIENT_STOCK");
        }
        medicineService.removeStock(medicineId, quantity);
        medicineInventoryRepository.saveAll(inventories);

        medicineInventoryRepository.saveAll(inventories);
        return batchDetails.toString();
    }


}
