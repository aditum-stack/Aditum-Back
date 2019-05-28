package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.mapper.CommunityDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommunityService {

    @Resource
    private CommunityDao communityDao;

    public int insert(Community pojo) {
        return communityDao.insert(pojo);
    }

    public int insertList(List<Community> pojos) {
        return communityDao.insertList(pojos);
    }

    public List<Community> select(Community pojo) {
        return communityDao.select(pojo);
    }

    public int update(Community pojo) {
        return communityDao.update(pojo);
    }

}
