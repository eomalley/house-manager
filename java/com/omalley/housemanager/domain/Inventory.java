package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "INVENTORY")
public class Inventory implements Serializable
{
    private static final long serialVersionUID = -3275315034513281481L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @Column(name = "TYPE")
    @Size(min = 1, max = 40)
    private String type;
    @Column(name = "BRAND")
    @Size(min = 1, max = 40)
    private String brand;
    @Column(name = "QUANTITY", precision = 8, scale = 2)
    private BigDecimal quantity;
    @Column(name = "LAST_BOUGHT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastBought;
    @Column(name = "USE_BY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useBy;
    @Column(name = "LAST_UPD_BY")
    private String lastUpdBy;
    @Column(name = "NOTES")
    private String notes;
    @Transient
    @Size(min = 1, max = 40)
    private String literalItemName;
    @Enumerated(EnumType.STRING)
    @Column(name = "MEASUREMENT")
    private MeasurementType measurement;
    @Column(name = "RATING")
    private int rating;
    @Column(name = "LOCK_MEASUREMENT")
    private boolean lockMeasurement;


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public Item getItem()
    {
        return this.item;
    }


    public void setItem(Item item)
    {
        this.item = item;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getBrand()
    {
        return this.brand;
    }


    public void setBrand(String brand)
    {
        this.brand = brand;
    }


    public BigDecimal getQuantity()
    {
        return this.quantity;
    }


    public void setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
    }


    public Date getLastBought()
    {
        return this.lastBought;
    }


    public void setLastBought(Date lastBought)
    {
        this.lastBought = lastBought;
    }


    public Date getUseBy()
    {
        return this.useBy;
    }


    public void setUseBy(Date lastUsed)
    {
        this.useBy = lastUsed;
    }


    public String getLastUpdBy()
    {
        return this.lastUpdBy;
    }


    public void setLastUpdBy(String lastUpdBy)
    {
        this.lastUpdBy = lastUpdBy;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public String getLiteralItemName()
    {
        return this.literalItemName;
    }


    public void setLiteralItemName(String literalItemName)
    {
        this.literalItemName = literalItemName;
    }


    public MeasurementType getMeasurement()
    {
        return this.measurement;
    }


    public void setMeasurement(MeasurementType measurement)
    {
        this.measurement = measurement;
    }


    public int getRating()
    {
        return this.rating;
    }


    public void setRating(int rating)
    {
        this.rating = rating;
    }


    public boolean isLockMeasurement()
    {
        return this.lockMeasurement;
    }


    public void setLockMeasurement(boolean lockMeasurement)
    {
        this.lockMeasurement = lockMeasurement;
    }

}
