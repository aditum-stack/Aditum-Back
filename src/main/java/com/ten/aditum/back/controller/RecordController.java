package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/record")
public class RecordController extends BaseController<Record> {

    private final RecordService service;

    @Autowired
    public RecordController(RecordService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Record record) {
        log.info("Record [GET] : {}", record);
        List<Record> recordList = service.select(record);
        if (recordList == null) {
            log.warn("Record [GET] FAILURE : {}", record);
            return new ResultModel(AditumCode.ERROR);
        }
        log.info("Record [GET] SUCCESS : {} -> {}", record, recordList);
        return new ResultModel(AditumCode.OK, recordList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(@RequestBody Record record) {
        log.info("Record [POST] : {}", record);
        Record entity = new Record()
                .setImei(record.getImei())
                .setPersonnelId(record.getPersonnelId())
                .setVisiteTime(TimeGenerator.currentTime())
                .setVisiteStatus(record.getVisiteStatus())
                .setIsDeleted(NO_DELETED);

        int result = service.insert(entity);
        if (result < 1) {
            log.warn("Record [POST] FAILURE : {}", record);
            return new ResultModel(AditumCode.ERROR);
        }

        log.info("New record : " + entity);

        log.info("Record [POST] SUCCESS : {}", entity);
        return new ResultModel(AditumCode.OK);
    }

    @Override
    public ResultModel update(Record record) {
        return null;
    }

    @Override
    public ResultModel delete(Record record) {
        return null;
    }
}
