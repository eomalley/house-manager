package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.UserInfoDaoCustom;

@Transactional
public class UserInfoDaoCustomImpl implements UserInfoDaoCustom
{
	@PersistenceContext
    EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findLikeUserName(String hint)
	{
		 return this.em.createQuery("SELECT DISTINCT(u.userName) FROM UserInfo u where UPPER(u.userName) LIKE :userName").setParameter("userName", hint.toUpperCase() + "%").getResultList();
	}
	

}
