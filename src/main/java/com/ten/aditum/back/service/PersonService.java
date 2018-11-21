package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.Person;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.mapper.PersonDao;

@Service
public class PersonService {

    @Resource
    private PersonDao personDao;

    public int insert(Person pojo){
        return personDao.insert(pojo);
    }

    public int insertList(List< Person> pojos){
        return personDao.insertList(pojos);
    }

    public List<Person> select(Person pojo){
        return personDao.select(pojo);
    }

    public int update(Person pojo){
        return personDao.update(pojo);
    }

}
