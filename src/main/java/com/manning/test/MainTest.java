package com.manning.test;

import com.manning.entity.TbUser;
import org.junit.Test;
import org.springframework.stereotype.Component;


/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/29 16:50
 */

@Component
public class MainTest {

//@Autowired
//SessionFactory sessionFactory;


    @Test
  public void test(){
TbUser tbUser=new TbUser();
tbUser.setSign("1111");
tbUser.setId(1);
tbUser.setNickname("222");
//      System.out.println(sessionFactory);


  }
}
