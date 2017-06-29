package com.omalley.housemanager.coordinators.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.omalley.housemanager.coordinators.IInventoryCoordinator;
import com.omalley.housemanager.coordinators.IItemCoordinator;
import com.omalley.housemanager.coordinators.IMeasurementCoordinator;
import com.omalley.housemanager.dao.InventoryDao;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.MeasurementType;

@Component
public class InventoryCoordinator implements IInventoryCoordinator
{

    @Autowired
    InventoryDao invDao;

    @Autowired
    IItemCoordinator itemCoord;

    @Autowired
    IMeasurementCoordinator measurementCoord;

    public static final class SortInventoryByItemName implements Comparator<Inventory>
    {
        @Override
        public int compare(Inventory s1, Inventory s2)
        {
            return s1.getItem().getName().compareTo(s2.getItem().getName());
        }
    }


    @Override
    public Inventory getInventoryByBrand(String name)
    {
        List<Inventory> inventory = this.invDao.findByBrand(name);
        if(inventory.size() == 1)
        {
            return inventory.get(0);
        }

        return null;
    }


    @Override
    public void saveInventory(Inventory inventory, AdjustmentBean adjustmentBean)
    {
        if(inventory != null)
        {
            if(adjustmentBean != null && adjustmentBean.getAdjustmentType() != null
               && !(adjustmentBean.getAdjustmentType().isEmpty()))
            {
                this.measurementCoord.adjustInventory(inventory, adjustmentBean);
            }
            else
            {
                this.measurementCoord.validateAmount(inventory);
            }
            this.setUpper(inventory);
            if(inventory.getId() != 0)
            {
                this.invDao.updateInventory(inventory);
            }
            else
            {
                this.invDao.save(inventory);
            }
        }
    }


    private void setUpper(Inventory inventory)
    {

        if(inventory.getBrand() != null)
        {
            inventory.setBrand(inventory.getBrand().toUpperCase());
        }
        if(inventory.getType() != null)
        {
            inventory.setType(inventory.getType().toUpperCase());
        }
        if(inventory.getLastUpdBy() != null)
        {
            inventory.setLastUpdBy(inventory.getLastUpdBy().toUpperCase());
        }
        // if(inventory.getMeasurement() != null)
        // {
        // inventory.setMeasurement(inventory.getMeasurement().toUpperCase());
        // }

    }


    @Override
    public List<Inventory> parseCsvFile(MultipartFile file) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        List<Inventory> inventories = new ArrayList<Inventory>();
        // (required) itemName ItemBrand InvType InvQuantity(bigDecimal) MeasurementType (optional)
        // notes, rating
        while((line = br.readLine()) != null)
        {
            String[] invenArray = line.split(",");
            if(invenArray.length > 6) {
            Inventory inventory = new Inventory();
            inventory.setLiteralItemName(invenArray[0].trim());
            inventory.setBrand(invenArray[1].trim());
            inventory.setType(invenArray[2].trim());
            inventory.setQuantity(new BigDecimal(Double.parseDouble(invenArray[3].trim())));
            inventory.setMeasurement(MeasurementType.valueOf(invenArray[4].trim()));
            if(invenArray[5] != null)
            {
                inventory.setNotes(invenArray[5].trim());
            }
            if(invenArray[6] != null)
            {
                inventory.setRating(Integer.valueOf(invenArray[6].trim()));
            }
            inventories.add(inventory);
            }
            

        }

        return inventories;
    }
}
