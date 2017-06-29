package com.omalley.housemanager.coordinators.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IMeasurementCoordinator;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.MeasurementType;

@Component
public class MeasurementCoordinator implements IMeasurementCoordinator
{

    private static final String ADD = "add";
    private static final String SUBTRACT = "subtract";
    public static final List<MeasurementType> convertableTypes;

    public static final class SortByValue implements Comparator<MeasurementType>
    {
        @Override
        public int compare(MeasurementType s1, MeasurementType s2)
        {
            return s2.getMlValue().compareTo(s1.getMlValue());
        }
    }

    static
    {
        List<MeasurementType> set = new ArrayList<>();
        set.add(MeasurementType.PINCH);
        set.add(MeasurementType.TEASPOON);
        set.add(MeasurementType.TABLESPOON);
        set.add(MeasurementType.OUNCE);
        set.add(MeasurementType.CUP);
        // set.add(MeasurementType.PINT);
        // set.add(MeasurementType.QUART);
        set.add(MeasurementType.GALLON);
        convertableTypes = set;
    }


    @Override
    public void adjustInventory(Inventory inventory, AdjustmentBean adjustment)
    {
        switch(adjustment.getAdjustmentType())
        {
            case ADD:
                this.addAmount(inventory, adjustment.getMeasurementAdjust(), adjustment.getQuantityAdjust());
                break;
            case SUBTRACT:
                this.subtractAmount(inventory, adjustment.getMeasurementAdjust(), adjustment.getQuantityAdjust());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }


    private void subtractAmount(Inventory inventory, MeasurementType type, BigDecimal quantityToSubtract)
    {
        if(inventory.getMeasurement() != null)
        {

            if(!convertableTypes.contains(type))
            {
                inventory.setMeasurement(type);
                BigDecimal total = inventory.getQuantity().subtract(quantityToSubtract);
                if(total.compareTo(BigDecimal.ZERO) < 0)
                {
                    total = BigDecimal.ZERO;
                }
                inventory.setQuantity(total);

            }
            else
            {
                BigDecimal total = this.getNewMlTotalSubtract(inventory, type, quantityToSubtract);
                if(total.compareTo(BigDecimal.ZERO) < 0)
                {
                    total = BigDecimal.ZERO;
                }
                this.setNewMeasurementTypeAndQuantity(inventory, type, total);

            }
        }

    }


    private void addAmount(Inventory inventory, MeasurementType type, BigDecimal quantityToAdd)
    {
        if(inventory.getMeasurement() == null)
        {
            inventory.setMeasurement(type);
            inventory.setQuantity(quantityToAdd);
        }
        else
        {
            if(!convertableTypes.contains(type))
            {
                inventory.setMeasurement(type);
                inventory.setQuantity(inventory.getQuantity().add(quantityToAdd));

            }
            else
            {
                BigDecimal total = this.getNewMlTotalAdd(inventory, type, quantityToAdd);
                this.setNewMeasurementTypeAndQuantity(inventory, type, total);
            }
        }
    }


    private void setNewMeasurementTypeAndQuantity(Inventory inventory, MeasurementType type, BigDecimal total)
    {
        MeasurementType newType;
        if(inventory.isLockMeasurement())
        {
            newType = type;
        }
        else
        {
            newType = this.getLargestWholeTypePerMl(total);
        }
        inventory.setQuantity(this.getNewQuantity(newType, total));
        inventory.setMeasurement(newType);
    }


    @Override
    public void validateAmount(Inventory inventory)
    {
        if(!convertableTypes.contains(inventory.getMeasurement()))
        {
            return;
        }
        BigDecimal testVal = this.getMlValue(inventory.getMeasurement(), inventory.getQuantity());
        MeasurementType testType = this.getLargestWholeTypePerMl(testVal);
        if(testType != inventory.getMeasurement())
        {
            this.setNewMeasurementTypeAndQuantity(inventory, inventory.getMeasurement(), testVal);
        }
    }


    @Override
    public boolean doesInventoryContain(Inventory inventory, BigDecimal amount, MeasurementType measurementType)
    {
        if(MeasurementCoordinator.convertableTypes.contains(measurementType) == MeasurementCoordinator.convertableTypes.contains(inventory.getMeasurement()))
        {
            BigDecimal requestedMlValue = this.getMlValue(measurementType, amount);
            BigDecimal inventoryMlValue = this.getMlValue(inventory.getMeasurement(), inventory.getQuantity());
            return (inventoryMlValue.compareTo(requestedMlValue) >= 0);
        }
        else
        {
            return false;
        }

    }


    private BigDecimal getMlValue(MeasurementType type, BigDecimal quantity)
    {

        return type.getMlValue().multiply(quantity);
    }


    private BigDecimal getNewMlTotalSubtract(Inventory inventory, MeasurementType type, BigDecimal quantityToSubtract)
    {
        return this.getMlValue(inventory.getMeasurement(), inventory.getQuantity()).subtract(this.getMlValue(type,
                                                                                                             quantityToSubtract));
    }


    private BigDecimal getNewMlTotalAdd(Inventory inventory, MeasurementType type, BigDecimal quantityToAdd)
    {
        return this.getMlValue(inventory.getMeasurement(), inventory.getQuantity()).add(this.getMlValue(type,
                                                                                                        quantityToAdd));
    }


    private BigDecimal getNewQuantity(MeasurementType newType, BigDecimal total)
    {
        if(newType == null)
        {
            return BigDecimal.ZERO;
        }
        return total.divide(newType.getMlValue(), 2, RoundingMode.HALF_DOWN);
    }


    private MeasurementType getLargestWholeTypePerMl(BigDecimal mlValue)
    {
        Collections.sort(convertableTypes, new MeasurementCoordinator.SortByValue());
        // start dividing by the largest, once you get at least 1, return
        for(MeasurementType type : convertableTypes)
        {
            if(mlValue.compareTo(type.getMlValue()) >= 0)
            {

                return type;

            }
        }
        // must be 0 or less than 0
        return null;
    }


	@Override
	public boolean isInventoryConvertable(Inventory inventory)
	{
		return MeasurementCoordinator.convertableTypes.contains(inventory.getMeasurement());
	}


	@Override
	public BigDecimal getMlValue(BigDecimal value, MeasurementType type)
	{
		return this.getMlValue(type, value);
	}
	
	@Override
	public AdjustmentBean getNewMeasurementValues(BigDecimal value)
	{
		
		MeasurementType type = this.getLargestWholeTypePerMl(value);
		BigDecimal newVal = this.getNewQuantity(type, value);
		AdjustmentBean adjBean = new AdjustmentBean();
		adjBean.setMeasurementAdjust(type);
		adjBean.setQuantityAdjust(newVal);
		return adjBean;
	}


}
