package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/community")
public class CommunityController extends BaseController<Community> {
    private final CommunityService service;

    @Autowired
    public CommunityController(CommunityService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Community community) {
        List<Community> communityList = service.select(community);
        if (communityList == null) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK, communityList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(Community community) {
        Community entity = new Community()
                .communityId(super.uidGenerator.generateUid())
                .communityName(community.getCommunityName())
                .communityCity(community.getCommunityCity())
                .communityAddress(community.getCommunityAddress())
                .deviceCount(NO_DELETED)
                .deviceOnlineCount(NO_DELETED)
                .createTime(super.timeGenerator.currentTime())
                .updateTime(super.timeGenerator.currentTime())
                .isDeleted(NO_DELETED);

        int result = service.insert(entity);
        if (result < 1) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK);
    }

    @Override
    public ResultModel update(Community community) {
        return null;
    }

    @Override
    public ResultModel delete(Community community) {
        return null;
    }

}
