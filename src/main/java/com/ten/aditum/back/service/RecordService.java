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

    public int insert(Record pojo) {
        return recordDao.insert(pojo);
    }

    public int insertList(List<Record> pojos) {
        return recordDao.insertList(pojos);
    }

    public List<Record> select(Record pojo) {
        return recordDao.select(pojo);
    }

    /**
     * 获取此ID之后的数据
     */
    public List<Record> selectAfterTheId(Record pojo) {
        if (pojo.getId() == null) {
            return null;
        }
        return recordDao.selectAfterTheId(pojo);
    }

    /**
     * 当前时间对应的时刻之后的数据
     */
    public List<Record> selectAfterTheDateTime(Record pojo) {
        if (pojo.getVisiteTime() == null) {
            return null;
        }
        return recordDao.selectAfterTheVisitTime(pojo);
    }

    public int update(Record pojo) {
        return recordDao.update(pojo);
    }

}
