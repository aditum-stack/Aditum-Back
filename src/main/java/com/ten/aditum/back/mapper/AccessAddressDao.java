package com.ten.aditum.back.mapper;

import com.ten.aditum.back.entity.AccessAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccessAddressDao {

    int insert(@Param("pojo") AccessAddress pojo);

    int insertList(@Param("pojos") List<AccessAddress> pojo);

    List<AccessAddress> select(@Param("pojo") AccessAddress pojo);

    int update(@Param("pojo") AccessAddress pojo);

}
