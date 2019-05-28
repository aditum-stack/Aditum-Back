package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.CommunityService;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
        log.debug("Community [GET] : {}", community);
        List<Community> communityList = service.select(community);
        if (communityList == null) {
            log.warn("Community [GET] FAILURE : {}", community);
            return new ResultModel(AditumCode.ERROR);
        }
        log.debug("Community [GET] SUCCESS : {} -> {}", community, communityList);
        return new ResultModel(AditumCode.OK, communityList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(@RequestBody Community community) {
        log.debug("Community [POST] : {}", community);
        Community entity = new Community()
                .setCommunityId(UidGenerator.generateUid())
                .setCommunityName(community.getCommunityName())
                .setCommunityCity(community.getCommunityCity())
                .setCommunityAddress(community.getCommunityAddress())
                .setDeviceCount(NO_DELETED)
                .setDeviceOnlineCount(NO_DELETED)
                .setCreateTime(TimeGenerator.currentTime())
                .setUpdateTime(TimeGenerator.currentTime())
                .setIsDeleted(NO_DELETED);

        int result = service.insert(entity);
        if (result < 1) {
            log.warn("Community [POST] FAILURE : {}", community);
            return new ResultModel(AditumCode.ERROR);
        }
        log.debug("Community [POST] SUCCESS : {}", entity);
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
