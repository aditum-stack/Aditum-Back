package com.ten.aditum.back.mapper;

import com.ten.aditum.back.entity.AccessTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccessTimeDao {

    int insert(@Param("pojo") AccessTime pojo);

    int insertList(@Param("pojos") List<AccessTime> pojo);

    List<AccessTime> select(@Param("pojo") AccessTime pojo);

    int update(@Param("pojo") AccessTime pojo);

}
