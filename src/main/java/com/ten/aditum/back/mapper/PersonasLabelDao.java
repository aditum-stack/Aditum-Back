package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.PersonasLabel;

public interface PersonasLabelDao {

    int insert(@Param("pojo") PersonasLabel pojo);

    int insertList(@Param("pojos") List<PersonasLabel> pojo);

    List<PersonasLabel> select(@Param("pojo") PersonasLabel pojo);

    int update(@Param("pojo") PersonasLabel pojo);

}
