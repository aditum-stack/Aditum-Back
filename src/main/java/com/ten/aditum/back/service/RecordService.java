package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.mapper.RecordDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
     * 获取数量
     */
    public int selectCount(Record pojo) {
        return recordDao.selectCount(pojo);
    }

    /**
     * 获取某段时间内的数量
     */
    public int selectCountBetweenDateTime(Record pojo, String startTime, String endTime) {
        return recordDao.selectCountBetweenDateTime(pojo, startTime, endTime);
    }

    /**
     * 获取某段时间内的数量
     */
    public int selectCountAfterDateTime(Record pojo, String startTime) {
        return recordDao.selectCountAfterDateTime(pojo, startTime);
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
