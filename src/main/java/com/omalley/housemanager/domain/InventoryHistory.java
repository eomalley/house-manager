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
@Table(name ="INVENTORY_HIST")
public class InventoryHistory implements Serializable
{
	
	private static final long serialVersionUID = -4973351991073212681L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	@Column(name="INVEN_ID")
	private int invenId;
	@Column(name="CREATION_DTTM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDttm;
	@Column(name="UPDATE_TYPE")
	private String updateType;
	@Column(name="PREVIOUS")
	private String previousValue;
	@Column(name="NEW")
	private String newValue;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
		
	public int getInvenId()
	{
		return invenId;
	}
	
	public void setInvenId(int invenId)
	{
		this.invenId = invenId;
	}
	
	public Date getCreationDttm()
	{
		return creationDttm;
	}
	
	public void setCreationDttm(Date creationDttm)
	{
		this.creationDttm = creationDttm;
	}
	
	public String getUpdateType()
	{
		return updateType;
	}
	
	public void setUpdateType(String updateType)
	{
		this.updateType = updateType;
	}
	
	public String getPreviousValue()
	{
		return previousValue;
	}
	
	public void setPreviousValue(String previousValue)
	{
		this.previousValue = previousValue;
	}
	
	public String getNewValue()
	{
		return newValue;
	}
	
	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}
	
	

}
