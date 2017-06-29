package com.omalley.housemanager.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.InventoryDaoCustom;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;

@Transactional
@Repository(value = "inventoryDao")
public interface InventoryDao extends CrudRepository<Inventory, Long>, InventoryDaoCustom
{
    List<Inventory> findByItem(Item item);


    List<Inventory> findByType(String type);


    List<Inventory> findByBrand(String brand);


    List<Inventory> findByQuantity(BigDecimal quantity);


    List<Inventory> findByLastBought(Date lastBought);


    List<Inventory> findByUseBy(Date UseBy);


    List<Inventory> findByLastUpdBy(String lastUpdBy);

}
