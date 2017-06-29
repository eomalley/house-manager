package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "RECIPES")
public class Recipe implements Serializable
{

    private static final long serialVersionUID = -5238014531933719885L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "LAST_MADE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMade;
    @Column(name = "TIMES_MADE")
    private int timesMade;
    @Column(name = "STYLE")
    private String style;
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ingredient> ingredientSet;
    @Column(name = "SERVING_SIZE")
    private int servingSize;
    @Column(name = "COOK_TIME")
    private String cookTime;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "RATING")
    private int rating;
    @Column(name = "IMAGE")
    private String image;
    @Transient
    private int shortIngredientCount;
    @Transient
    private int availableIngredientCount;


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


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public Date getLastMade()
    {
        return this.lastMade;
    }


    public void setLastMade(Date lastMade)
    {
        this.lastMade = lastMade;
    }


    public int getTimesMade()
    {
        return this.timesMade;
    }


    public void setTimesMade(int timesMade)
    {
        this.timesMade = timesMade;
    }


    public int getRating()
    {
        return this.rating;
    }


    public void setRating(int rating)
    {
        this.rating = rating;
    }


    public Set<Ingredient> getIngredientSet()
    {
        return this.ingredientSet;
    }


    public void setIngredientSet(Set<Ingredient> ingredientList)
    {
        this.ingredientSet = ingredientList;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public String getStyle()
    {
        return this.style;
    }


    public void setStyle(String style)
    {
        this.style = style;
    }


    public int getServingSize()
	{
		return servingSize;
	}


	public void setServingSize(int servingSize)
	{
		this.servingSize = servingSize;
	}


	public String getCookTime()
	{
		return cookTime;
	}


	public void setCookTime(String cookTime)
	{
		this.cookTime = cookTime;
	}


	public int getShortIngredientCount()
    {
        return this.shortIngredientCount;
    }


    public void setShortIngredientCount(int shortIngredientCount)
    {
        this.shortIngredientCount = shortIngredientCount;
    }


    public int getAvailableIngredientCount()
    {
        return this.availableIngredientCount;
    }


    public void setAvailableIngredientCount(int availableIngredientCount)
    {
        this.availableIngredientCount = availableIngredientCount;
    }


    public String getImage()
    {
        return this.image;
    }


    public void setImage(String image)
    {
        this.image = image;
    }

}
