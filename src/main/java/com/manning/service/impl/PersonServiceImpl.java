package com.manning.service.impl;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/30 12:59
 */

import com.manning.dao.PersonDao;
import com.manning.entity.Person;
import com.manning.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDao personDao;

    public Person getPerson(String id) {
        return personDao.getPerson(id);
    }

    public List<Person> getAllPerson() {
        List<Person> list = personDao.getAllPerson();
        return list;
    }


    public void addPerson(Person person) {
        personDao.addPerson(person);
    }

    public boolean delPerson(String id) {
        return personDao.delPerson(id);
    }

    public boolean updatePerson(Person person) {
        return personDao.updatePerson(person);
    }
}
