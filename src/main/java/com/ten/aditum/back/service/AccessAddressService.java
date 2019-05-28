package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.AccessAddress;
import com.ten.aditum.back.mapper.AccessAddressDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccessAddressService {

    @Resource
    private AccessAddressDao accessAddressDao;

    public int insert(AccessAddress pojo) {
        return accessAddressDao.insert(pojo);
    }

    public int insertList(List<AccessAddress> pojos) {
        return accessAddressDao.insertList(pojos);
    }

    public List<AccessAddress> select(AccessAddress pojo) {
        return accessAddressDao.select(pojo);
    }

    public int update(AccessAddress pojo) {
        return accessAddressDao.update(pojo);
    }

}
