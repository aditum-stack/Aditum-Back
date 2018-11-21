package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.Record;

public interface RecordDao {

    int insert(@Param("pojo") Record pojo);

    int insertList(@Param("pojos") List<Record> pojo);

    List<Record> select(@Param("pojo") Record pojo);

    int update(@Param("pojo") Record pojo);

}
