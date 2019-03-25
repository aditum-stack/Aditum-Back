package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.PersonService;
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
@RequestMapping(value = "/person")
public class PersonController extends BaseController<Person> {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Person person) {
        List<Person> personList = service.select(person);
        if (personList == null) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK, personList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(Person person) {
        Person entity = new Person()
                .personnelId(super.uidGenerator.generateUid())
                .personnelName(person.getPersonnelName())
                .communityId(person.getCommunityId())
                .personnelAddress(person.getPersonnelAddress())
                .personnelPhone(person.getPersonnelPhone())
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
    public ResultModel update(Person person) {
        return null;
    }

    @Override
    public ResultModel delete(Person person) {
        return null;
    }
}
