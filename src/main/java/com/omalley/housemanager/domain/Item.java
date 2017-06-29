package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ITEMS")
public class Item implements Serializable
{

    private static final long serialVersionUID = -565905660911009697L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FAVORITE_BRAND")
    private String favoriteBrand;
    @OneToMany(mappedBy = "item", orphanRemoval = true, fetch = FetchType.EAGER)
    // , cascade = CascadeType.ALL
    @JsonIgnore
    private Set<Inventory> inventoryList;
    @Column(name = "ESSENTIAL")
    private boolean essential;
    @Column(name = "THRESHOLD")
    private BigDecimal threshold;
    @Enumerated(EnumType.STRING)
    @Column(name = "MEASUREMENT")
    private MeasurementType measurement;
    @Transient
    private int inventorySize;
    @Transient
    private BigDecimal convertableQuantity;
    @Transient
    private MeasurementType convertableQuantityMeasurement;
    @Transient
    private Map<MeasurementType, BigDecimal> nonConvertableQuantity;


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getFavoriteBrand()
    {
        return this.favoriteBrand;
    }


    public void setFavoriteBrand(String favoriteBrand)
    {
        this.favoriteBrand = favoriteBrand;
    }


    public Set<Inventory> getInventoryList()
    {
        return this.inventoryList;
    }


    public void setInventoryList(Set<Inventory> inventory)
    {
        this.inventoryList = inventory;
    }


    public int getInventorySize()
    {
        if(this.inventoryList != null)
        {
            return this.inventoryList.size();
        }
        else
        {
            return 0;
        }
    }


    public boolean isEssential()
    {
        return this.essential;
    }


    public void setEssential(boolean essential)
    {
        this.essential = essential;
    }


    public BigDecimal getThreshold()
    {
        return this.threshold;
    }


    public void setThreshold(BigDecimal threshold)
    {
        this.threshold = threshold;
    }


    public BigDecimal getConvertableQuantity()
    {
        return this.convertableQuantity;
    }


    public void setConvertableQuantity(BigDecimal convertableQuantity)
    {
        this.convertableQuantity = convertableQuantity;
    }


    public MeasurementType getConvertableQuantityMeasurement()
    {
        return this.convertableQuantityMeasurement;
    }


    public void setConvertableQuantityMeasurement(MeasurementType convertableQuantityMeasurement)
    {
        this.convertableQuantityMeasurement = convertableQuantityMeasurement;
    }


    public Map<MeasurementType, BigDecimal> getNonConvertableQuantity()
    {
        return this.nonConvertableQuantity;
    }


    public void setNonConvertableQuantity(Map<MeasurementType, BigDecimal> nonConvertableQuantity)
    {
        this.nonConvertableQuantity = nonConvertableQuantity;
    }


    public MeasurementType getMeasurement()
    {
        return this.measurement;
    }


    public void setMeasurement(MeasurementType measurement)
    {
        this.measurement = measurement;
    }


    @Override
    public String toString()
    {
        return this.name;
    }
}
