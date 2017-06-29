package com.omalley.housemanager.coordinators;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;

public interface IInventoryCoordinator
{
    Inventory getInventoryByBrand(String name);


    void saveInventory(Inventory inventory, AdjustmentBean adjustmentParams);


    public List<Inventory> parseCsvFile(MultipartFile file) throws IOException;

}
