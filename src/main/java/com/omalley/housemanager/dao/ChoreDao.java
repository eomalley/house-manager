package com.omalley.housemanager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ChoreDaoCustom;
import com.omalley.housemanager.domain.Chore;

@Transactional
@Repository(value = "choreDao")
public interface ChoreDao extends CrudRepository<Chore, Long>, ChoreDaoCustom
{
    List<Chore> findByName(String name);


    List<Chore> findByType(String type);


    List<Chore> findByFrequency(String frequency);


    List<Chore> findByFrequencyPeriod(String frequencyPeriod);

}
