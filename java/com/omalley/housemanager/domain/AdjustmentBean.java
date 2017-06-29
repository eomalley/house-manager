package com.omalley.housemanager.domain;

import java.math.BigDecimal;

public class AdjustmentBean
{
    private String adjustmentType;
    private BigDecimal quantityAdjust;
    private MeasurementType measurementAdjust;


    public String getAdjustmentType()
    {
        return this.adjustmentType;
    }


    public void setAdjustmentType(String adjustmentType)
    {
        this.adjustmentType = adjustmentType;
    }


    public BigDecimal getQuantityAdjust()
    {
        return this.quantityAdjust;
    }


    public void setQuantityAdjust(BigDecimal quantityAdjust)
    {
        this.quantityAdjust = quantityAdjust;
    }


    public MeasurementType getMeasurementAdjust()
    {
        return this.measurementAdjust;
    }


    public void setMeasurementAdjust(MeasurementType measurementAdjust)
    {
        this.measurementAdjust = measurementAdjust;
    }
}
