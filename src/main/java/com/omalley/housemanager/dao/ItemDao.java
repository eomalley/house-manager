package com.omalley.housemanager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ItemDaoCustom;
import com.omalley.housemanager.domain.Item;

@Transactional
@Repository(value = "itemDao")
public interface ItemDao extends CrudRepository<Item, Long>, ItemDaoCustom
{
    List<Item> findByName(String name);


}
