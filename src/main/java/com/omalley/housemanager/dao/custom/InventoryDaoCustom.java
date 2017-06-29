package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.Inventory;

public interface InventoryDaoCustom
{
    public List<Inventory> findAllZeroQuantityItems();


    public List<Inventory> returnAllItems();


    public void updateInventory(Inventory inventory);


    public List<String> findLikeBrandName(String hint);


    public List<String> findLikeTypeName(String hint);


    List<String> findLikeMeasurementName(String hint);

}
