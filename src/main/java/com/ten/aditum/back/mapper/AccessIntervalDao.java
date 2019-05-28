package com.ten.aditum.back.mapper;

import com.ten.aditum.back.entity.AccessInterval;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccessIntervalDao {

    int insert(@Param("pojo") AccessInterval pojo);

    int insertList(@Param("pojos") List<AccessInterval> pojo);

    List<AccessInterval> select(@Param("pojo") AccessInterval pojo);

    int update(@Param("pojo") AccessInterval pojo);

}
