package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.ToDo;

public interface ToDoDaoCustom
{

	void updateToDo(ToDo todo);

	List<ToDo> getIncompleteToDos();

	List<ToDo> getCompletedToDos();

	List<ToDo> returnAllItems();

}
