package com.omalley.housemanager.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCRATCH")
public class Scratch implements Serializable
{

    private static final long serialVersionUID = -7545647085664363874L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "NOTE")
    private String note;


    public long getId()
    {
        return this.id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public String getNote()
    {
        return this.note;
    }


    public void setNote(String note)
    {
        this.note = note;
    }

}
