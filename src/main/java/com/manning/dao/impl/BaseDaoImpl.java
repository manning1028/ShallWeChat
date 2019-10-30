package com.manning.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/30 15:11
 */
@Repository
@Transactional
public class BaseDaoImpl {
    @Autowired
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

}
