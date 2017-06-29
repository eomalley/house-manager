package com.omalley.housemanager.coordinators;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.omalley.housemanager.Application;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.MeasurementType;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MeasurementCoordinatorTest
{

    @Autowired
    IMeasurementCoordinator measurementCoord;


    @Test
    public void testAdjustMeasurementSubtract()
    {
        Inventory inventory = new Inventory();
        inventory.setMeasurement(MeasurementType.CUP);
        inventory.setQuantity(BigDecimal.valueOf(1)); // 236.59
        AdjustmentBean adjBean = new AdjustmentBean();
        adjBean.setAdjustmentType("subtract");
        adjBean.setMeasurementAdjust(MeasurementType.OUNCE);
        adjBean.setQuantityAdjust(BigDecimal.valueOf(3));
        this.measurementCoord.adjustInventory(inventory, adjBean); // 88.71
        // 147.88 ml

        Assert.assertTrue(BigDecimal.valueOf(5.00).setScale(2).equals(inventory.getQuantity()));
    }


    @Test
    public void testAdjustMeasurementAdd()
    {
        Inventory inventory = new Inventory();
        inventory.setMeasurement(MeasurementType.OUNCE);
        inventory.setQuantity(BigDecimal.valueOf(3)); // 88.71
        AdjustmentBean adjBean = new AdjustmentBean();
        adjBean.setAdjustmentType("add");
        adjBean.setMeasurementAdjust(MeasurementType.CUP);
        adjBean.setQuantityAdjust(BigDecimal.valueOf(3));
        this.measurementCoord.adjustInventory(inventory, adjBean); // 709.77
        // 798.48
        Assert.assertTrue(MeasurementType.CUP.equals(inventory.getMeasurement()));
        Assert.assertTrue(BigDecimal.valueOf(3.37).setScale(2).equals(inventory.getQuantity()));
    }


    @Test
    public void testValidateAmount()
    {
        Inventory inventory = new Inventory();
        inventory.setMeasurement(MeasurementType.OUNCE);
        inventory.setQuantity(BigDecimal.valueOf(10));
        this.measurementCoord.validateAmount(inventory);

        Assert.assertTrue(MeasurementType.CUP.equals(inventory.getMeasurement()));
        Assert.assertTrue(BigDecimal.valueOf(1.25).setScale(2).equals(inventory.getQuantity()));
    }

}
