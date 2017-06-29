package com.omalley.housemanager.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ToDoDaoCustom;
import com.omalley.housemanager.domain.ToDo;

@Transactional
@Repository(value = "toDoDao")
public interface ToDoDao extends CrudRepository<ToDo, Long>, ToDoDaoCustom
{

	List<ToDo> findByDateEntered(Date dateEntered);
	
	List<ToDo> findByDueDate(Date dueDate);
	
	List<ToDo> findByImportance(int importance);
	
	List<ToDo> findByEnteredBy(String enteredBy);

}
