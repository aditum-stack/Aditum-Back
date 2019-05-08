package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.AccessTime;

public interface AccessTimeDao {

    int insert(@Param("pojo") AccessTime pojo);

    int insertList(@Param("pojos") List<AccessTime> pojo);

    List<AccessTime> select(@Param("pojo") AccessTime pojo);

    int update(@Param("pojo") AccessTime pojo);

}
