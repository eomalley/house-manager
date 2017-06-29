package com.omalley.housemanager.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.UserInfoDaoCustom;
import com.omalley.housemanager.domain.UserInfo;

@Transactional
@Repository(value = "userDao")
public interface UserInfoDao extends CrudRepository<UserInfo, Long>, UserInfoDaoCustom
{
	public UserInfo findByUserName(String userName);
}
