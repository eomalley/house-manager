package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "GROCERY_ITEM")
public class GroceryItem implements Serializable
{
    private static final long serialVersionUID = -3171335910431090965L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "SPECIFIC_BRAND")
    private String specificBrand;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "INVENTORY_ID")
    private Inventory inventory;
    @Column(name = "QUANTITY", precision = 8, scale = 2)
    private BigDecimal quantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "MEASUREMENT")
    private MeasurementType measurement;
    @Transient
    @Size(min = 1, max = 40)
    private String itemName;


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public String getSpecificBrand()
    {
        return this.specificBrand;
    }


    public void setSpecificBrand(String specificBrand)
    {
        this.specificBrand = specificBrand;
    }


    public Item getItem()
    {
        return this.item;
    }


    public void setItem(Item item)
    {
        this.item = item;
        this.itemName = item.getName();
    }


    public Inventory getInventory()
    {
        return this.inventory;
    }


    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }


    public BigDecimal getQuantity()
    {
        return this.quantity;
    }


    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }


    public MeasurementType getMeasurement()
    {
        return this.measurement;
    }


    public void setMeasurement(MeasurementType measurement)
    {
        this.measurement = measurement;
    }


    public String getItemName()
    {
        if(item != null && itemName.isEmpty()) {
            return this.item.getName();
        }
        return this.itemName;
    }


    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

}
