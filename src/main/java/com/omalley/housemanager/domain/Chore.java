package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CHORES")
public class Chore implements Serializable
{
    private static final long serialVersionUID = 6432300111400848275L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "FREQUENCY")
    private int frequency;
    @Column(name = "FREQUENCY_PERIOD")
    @Enumerated(EnumType.STRING)
    private Period frequencyPeriod;
    @Column(name = "LAST_COMPLETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCompleted;
    @Column(name = "LAST_COMPLETED_BY")
    private String lastCompletedBy;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "ASSIGNEE")
    private String assignee;
    @Column(name = "DIFFICULTY")
    private int difficulty;


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


    public int getFrequency()
    {
        return this.frequency;
    }


    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }


    public Period getFrequencyPeriod()
    {
        return this.frequencyPeriod;
    }


    public void setFrequencyPeriod(Period frequencyPeriod)
    {
        this.frequencyPeriod = frequencyPeriod;
    }


    public Date getLastCompleted()
    {
        return this.lastCompleted;
    }


    public void setLastCompleted(Date lastCompleted)
    {
        this.lastCompleted = lastCompleted;
    }


    public String getLastCompletedBy()
    {
        return this.lastCompletedBy;
    }


    public void setLastCompletedBy(String lastCompletedBy)
    {
        this.lastCompletedBy = lastCompletedBy;
    }


    public Date getDueDate()
	{
		return this.dueDate;
	}


	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}


	public String getAssignee()
	{
		return assignee;
	}


	public void setAssignee(String assignee)
	{
		this.assignee = assignee;
	}


	public int getDifficulty()
    {
        return this.difficulty;
    }


    public void setDifficulty(int difficulty)
    {
        this.difficulty = difficulty;
    }

}
