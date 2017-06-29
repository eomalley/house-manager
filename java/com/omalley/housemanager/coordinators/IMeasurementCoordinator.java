package com.omalley.housemanager.coordinators;

import java.math.BigDecimal;

import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.MeasurementType;

public interface IMeasurementCoordinator
{

    public void validateAmount(Inventory inventory);


    public void adjustInventory(Inventory inventory, AdjustmentBean adjustment);


    boolean doesInventoryContain(Inventory inventory, BigDecimal amount, MeasurementType measurementType);
    
    boolean isInventoryConvertable(Inventory inventory);
    
    public BigDecimal getMlValue(BigDecimal value, MeasurementType type);

	public AdjustmentBean getNewMeasurementValues(BigDecimal value);

}
