package com.omalley.housemanager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.GroceryItemDaoCustom;
import com.omalley.housemanager.domain.GroceryItem;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;

@Transactional
@Repository(value = "groceryItemDao")
public interface GroceryItemDao extends CrudRepository<GroceryItem, Long>, GroceryItemDaoCustom
{
    List<GroceryItem> findByItem(Item item);


    List<GroceryItem> findByInventory(Inventory inventory);

}
