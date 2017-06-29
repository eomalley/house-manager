package com.omalley.housemanager.coordinators;

import com.omalley.housemanager.domain.Scratch;
import com.omalley.housemanager.domain.ToDo;

public interface IToDoCoordinator
{
    public void createOrUpdateToDo(ToDo todo);


    public void createOrUpdateScratch(Scratch scratch);

}
