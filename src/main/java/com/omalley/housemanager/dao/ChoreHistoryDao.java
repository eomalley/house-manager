package com.omalley.housemanager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ChoreHistoryDaoCustom;
import com.omalley.housemanager.domain.ChoreHistory;

@Transactional
@Repository(value = "choreHistoryDao")
public interface ChoreHistoryDao extends CrudRepository<ChoreHistory, Long>, ChoreHistoryDaoCustom
{
    List<ChoreHistory> findByChoreId(String choreId);


    List<ChoreHistory> findByLastDone(String lastDone);


    List<ChoreHistory> findByLastDoneBy(String lastDoneBy);

}
