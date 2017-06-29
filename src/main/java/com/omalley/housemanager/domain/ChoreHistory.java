package com.omalley.housemanager.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CHORE_HIST")
public class ChoreHistory implements Serializable
{
    private static final long serialVersionUID = 7539185296036422260L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "CHORE_ID")
    private Chore chore;
    @Column(name = "LAST_DONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDone;
    @Column(name = "LAST_DONE_BY")
    private String lastDoneBy;
    @Column(name = "DATE_ENTERED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntered;


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public Chore getChore()
    {
        return this.chore;
    }


    public void setChore(Chore chore)
    {
        this.chore = chore;
    }


    public Date getLastDone()
    {
        return this.lastDone;
    }


    public void setLastDone(Date lastDone)
    {
        this.lastDone = lastDone;
    }


    public String getLastDoneBy()
    {
        return this.lastDoneBy;
    }


    public void setLastDoneBy(String lastDoneBy)
    {
        this.lastDoneBy = lastDoneBy;
    }


    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }


    public Date getDateEntered()
    {
        return this.dateEntered;
    }


    public void setDateEntered(Date dateEntered)
    {
        this.dateEntered = dateEntered;
    }

}
