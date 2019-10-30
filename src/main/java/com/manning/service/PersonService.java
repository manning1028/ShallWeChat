package com.manning.service;

import com.manning.entity.Person;

import java.util.List;

/**
 * @author: zengjing
 * @version: v1.0
 * @description:
 * @date: 2019/10/30 12:57
 */


public interface PersonService {

    Person getPerson(String id);

    List<Person> getAllPerson();

    void addPerson(Person person);

    boolean delPerson(String id);

    boolean updatePerson(Person person);
}
