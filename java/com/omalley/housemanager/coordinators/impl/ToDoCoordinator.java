package com.omalley.housemanager.coordinators.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IToDoCoordinator;
import com.omalley.housemanager.dao.ScratchDao;
import com.omalley.housemanager.dao.ToDoDao;
import com.omalley.housemanager.domain.Scratch;
import com.omalley.housemanager.domain.ToDo;

@Component
public class ToDoCoordinator implements IToDoCoordinator
{
    @Autowired
    ToDoDao toDoDao;

    @Autowired
    ScratchDao scratchDao;


    @Override
    public void createOrUpdateToDo(ToDo todo)
    {
        if(todo.getId() != 0)
        {
            this.toDoDao.updateToDo(todo);
        }
        else
        {
            this.setUpper(todo);
            this.toDoDao.save(todo);
        }

    }


    private void setUpper(ToDo todo)
    {
        if(todo.getCompletedBy() != null)
        {
            todo.setCompletedBy(todo.getCompletedBy().toUpperCase());
        }

        if(todo.getEnteredBy() != null)
        {
            todo.setEnteredBy(todo.getEnteredBy().toUpperCase());
        }

        if(todo.getName() != null)
        {
            todo.setName(todo.getName().toUpperCase());
        }
    }


    @Override
    public void createOrUpdateScratch(Scratch scratch)
    {
        if(scratch.getId() != 0)
        {
            this.scratchDao.updateScratch(scratch);
        }
        else
        {
            this.scratchDao.save(scratch);
        }

    }

}
