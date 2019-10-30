package com.manning.service;

import com.jq.support.common.persistence.Parameter;
import com.manning.dao.TbUserDao;
import com.manning.entity.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/30 15:23
 */
@Service
@Transactional
public class TbUserService {

    @Autowired
   private TbUserDao tbUserDao;

    public void save(TbUser tbUser){
         tbUserDao.save(tbUser);
    }

    public TbUser findUserByTelphone(String telphone){
     TbUser tbUser= tbUserDao.getByHql("select user from TbUser user where telphone=:p1",new Parameter(telphone));
     return  tbUser;
     }
}
