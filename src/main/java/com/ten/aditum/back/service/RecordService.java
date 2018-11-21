package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.Record;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.mapper.RecordDao;

@Service
public class RecordService {

    @Resource
    private RecordDao recordDao;

    public int insert(Record pojo){
        return recordDao.insert(pojo);
    }

    public int insertList(List< Record> pojos){
        return recordDao.insertList(pojos);
    }

    public List<Record> select(Record pojo){
        return recordDao.select(pojo);
    }

    public int update(Record pojo){
        return recordDao.update(pojo);
    }

}
