package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ToDoDaoCustom;
import com.omalley.housemanager.domain.ToDo;

@Transactional
public class ToDoDaoCustomImpl implements ToDoDaoCustom
{
	@PersistenceContext
    EntityManager em;
	
	@Override
    public void updateToDo(ToDo todo)
    {
        this.em.merge(todo);
        this.em.flush();

    }
	
	@SuppressWarnings("unchecked")
    @Override
    public List<ToDo> returnAllItems()
    {
        return this.em.createQuery("FROM ToDo").getResultList();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ToDo> getIncompleteToDos() 
	{
		return this.em.createQuery("SELECT u FROM ToDo u where u.completedBy is null").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ToDo> getCompletedToDos() 
	{
		return this.em.createQuery("SELECT u FROM ToDo u where u.completedBy is not null").getResultList();
	}
	
	
}
