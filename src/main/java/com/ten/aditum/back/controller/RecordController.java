package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/record")
public class RecordController extends BaseController<Record> {
    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    private final RecordService service;

    @Autowired
    public RecordController(RecordService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Record record) {
        List<Record> recordList = service.select(record);
        if (recordList == null) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK, recordList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(Record record) {
        Record entity = new Record()
                .imei(record.getImei())
                .personnelId(record.getPersonnelId())
                .visiteTime(timeGenerator.currentTime())
                .visiteStatus(record.getVisiteStatus())
                .isDeleted(NO_DELETED);

        Integer result = service.insert(entity);
        if (result < 1) {
            return new ResultModel(AditumCode.ERROR);
        }
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
