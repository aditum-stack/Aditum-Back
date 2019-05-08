package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.PersonasPortrait;

public interface PersonasPortraitDao {

    int insert(@Param("pojo") PersonasPortrait pojo);

    int insertList(@Param("pojos") List<PersonasPortrait> pojo);

    List<PersonasPortrait> select(@Param("pojo") PersonasPortrait pojo);

    int update(@Param("pojo") PersonasPortrait pojo);

}
