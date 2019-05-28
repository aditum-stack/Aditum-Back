package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.mapper.PersonasLabelDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PersonasLabelService {

    @Resource
    private PersonasLabelDao personasLabelDao;

    public int insert(PersonasLabel pojo) {
        return personasLabelDao.insert(pojo);
    }

    public int insertList(List<PersonasLabel> pojos) {
        return personasLabelDao.insertList(pojos);
    }

    public List<PersonasLabel> select(PersonasLabel pojo) {
        return personasLabelDao.select(pojo);
    }

    public int update(PersonasLabel pojo) {
        return personasLabelDao.update(pojo);
    }

}
