package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "INGREDIENTS")
public class Ingredient implements Serializable
{

    private static final long serialVersionUID = -4859796428663493646L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "RECIPE_ID", nullable = false)
    @JsonIgnore
    private Recipe recipe;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @Column(name = "AMOUNT", precision = 8, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "MEASUREMENT")
    private MeasurementType measurement;
    @Column(name = "FORM")
    private String form;
    @Transient
    private String itemName;
    @Transient
    private Set<Inventory> availableInventory = new HashSet<Inventory>();


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public Recipe getRecipe()
    {
        return this.recipe;
    }


    public void setRecipe(Recipe recipe)
    {
        this.recipe = recipe;
    }


    public Item getItem()
    {
        return this.item;
    }


    public void setItem(Item item)
    {
        this.item = item;
    }


    public BigDecimal getAmount()
    {
        return this.amount;
    }


    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }


    public MeasurementType getMeasurement()
    {
        return this.measurement;
    }


    public void setMeasurement(MeasurementType measurement)
    {
        this.measurement = measurement;
    }


    public String getForm()
    {
        return this.form;
    }


    public void setForm(String form)
    {
        this.form = form;
    }


    public String getItemName()
    {
        return this.itemName;
    }


    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }


    public Set<Inventory> getAvailableInventory()
    {
        return this.availableInventory;
    }


    public void setAvailableInventory(Set<Inventory> inventorySet)
    {
        this.availableInventory = inventorySet;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.form == null) ? 0 : this.form.hashCode());
        result = prime * result + (int) (this.id ^ (this.id >>> 32));
        result = prime * result + ((this.item == null) ? 0 : this.item.hashCode());
        result = prime * result + ((this.measurement == null) ? 0 : this.measurement.hashCode());
        result = prime * result + ((this.recipe == null) ? 0 : this.recipe.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(this.getClass() != obj.getClass())
        {
            return false;
        }
        Ingredient other = (Ingredient) obj;
        if(this.amount == null)
        {
            if(other.amount != null)
            {
                return false;
            }
        }
        else if(!this.amount.equals(other.amount))
        {
            return false;
        }
        if(this.form == null)
        {
            if(other.form != null)
            {
                return false;
            }
        }
        else if(!this.form.equals(other.form))
        {
            return false;
        }
        if(this.id != other.id)
        {
            return false;
        }
        if(this.item == null)
        {
            if(other.item != null)
            {
                return false;
            }
        }
        else if(!this.item.equals(other.item))
        {
            return false;
        }
        if(this.measurement != other.measurement)
        {
            return false;
        }
        if(this.recipe == null)
        {
            if(other.recipe != null)
            {
                return false;
            }
        }
        else if(!this.recipe.equals(other.recipe))
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return this.itemName + ' ' + this.amount.toString() + ' ' + this.measurement + ' ' + this.form;
    }
}
