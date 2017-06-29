package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TODO")
public class ToDo implements Serializable
{

    private static final long serialVersionUID = 8600166545539388561L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "DATE_ENTERED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntered;
    @Column(name = "ENTERED_BY")
    private String enteredBy;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "COMPLETED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;
    @Column(name = "COMPLETED_BY")
    private String completedBy;
    @Column(name = "IMPORTANCE")
    private int importance;


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


    public String getLocation()
    {
        return this.location;
    }


    public void setLocation(String location)
    {
        this.location = location;
    }


    public Date getDateEntered()
    {
        return this.dateEntered;
    }


    public void setDateEntered(Date dateEntered)
    {
        this.dateEntered = dateEntered;
    }


    public String getEnteredBy()
    {
        return this.enteredBy;
    }


    public void setEnteredBy(String enteredBy)
    {
        this.enteredBy = enteredBy;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public Date getDueDate()
    {
        return this.dueDate;
    }


    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }


    public String getCompletedBy()
    {
        return this.completedBy;
    }


    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
    }

	public Date getCompletedOn()
	{
		return completedOn;
	}


	public void setCompletedOn(Date completedOn)
	{
		this.completedOn = completedOn;
	}


	public int getImportance()
    {
        return this.importance;
    }


    public void setImportance(int importance)
    {
        this.importance = importance;
    }

}
