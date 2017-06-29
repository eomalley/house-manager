package com.omalley.housemanager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.omalley.housemanager.domain.InventoryHistory;

public interface InventoryHistoryDao extends CrudRepository<InventoryHistory, Long>
{
	List<InventoryHistory> findByInvenId(String invenId);
	List<InventoryHistory> findByCreationDttm(String creationDttm);
	List<InventoryHistory> findByUpdateType(String updateType);
}

