package com.omalley.housemanager.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ScratchDaoCustom;
import com.omalley.housemanager.domain.Scratch;

@Transactional
@Repository(value = "scratchDao")
public interface ScratchDao extends CrudRepository<Scratch, Long>, ScratchDaoCustom
{

}
