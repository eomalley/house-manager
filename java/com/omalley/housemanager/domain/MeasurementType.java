package com.omalley.housemanager.domain;

import java.math.BigDecimal;

public enum MeasurementType
{
    PINCH("PINCH", BigDecimal.valueOf(0.05)), // 0.05 ml
    TEASPOON("TEASPOON", BigDecimal.valueOf(4.93)), // 4.93 ml
    TABLESPOON("TABLESPOON", BigDecimal.valueOf(14.79)), // 14.79 ml
    OUNCE("OUNCE", BigDecimal.valueOf(29.57)), // 29.57 ml
    CUP("CUP", BigDecimal.valueOf(236.59)), // 236.59 ml
    // PINT("PINT", BigDecimal.valueOf(473.18)), // 473.18 ml //removed bc confusing
    // QUART("QUART", BigDecimal.valueOf(946.35)), // 946.35 ml
    GALLON("GALLON", BigDecimal.valueOf(3785.41)), // 3785.41 ml
    POUND("POUND", BigDecimal.valueOf(1)),
    PIECE("PIECE", BigDecimal.valueOf(1)),
    BOX("BOX", BigDecimal.valueOf(1)),
    WHOLE("WHOLE", BigDecimal.valueOf(1)),
    CAN("CAN", BigDecimal.valueOf(1));

    private BigDecimal mlValue;
    private String name;


    private MeasurementType(String name, BigDecimal mlValue)
    {
        this.setMlValue(mlValue);
        this.setName(name);
    }


    public BigDecimal getMlValue()
    {
        return this.mlValue;
    }


    public void setMlValue(BigDecimal mlValue)
    {
        this.mlValue = mlValue;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}