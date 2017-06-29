package com.omalley.housemanager.domain;


public enum Period
{
    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    YEAR("YEAR");

    private String name;


    private Period(String name)
    {
        this.setName(name);
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
