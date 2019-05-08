package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.AccessAddress;

public interface AccessAddressDao {

    int insert(@Param("pojo") AccessAddress pojo);

    int insertList(@Param("pojos") List<AccessAddress> pojo);

    List<AccessAddress> select(@Param("pojo") AccessAddress pojo);

    int update(@Param("pojo") AccessAddress pojo);

}
