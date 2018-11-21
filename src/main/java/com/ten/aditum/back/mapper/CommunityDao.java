package com.ten.aditum.back.mapper;

import com.ten.aditum.back.entity.Community;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommunityDao {

    int insert(@Param("pojo") Community pojo);

    int insertList(@Param("pojos") List<Community> pojo);

    List<Community> select(@Param("pojo") Community pojo);

    int update(@Param("pojo") Community pojo);

}
