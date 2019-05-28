package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.PersonService;
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
        log.debug("Person [GET] : {}", person);
        List<Person> personList = service.select(person);
        if (personList == null) {
            log.warn("Person [GET] FAILURE : {}", person);
            return new ResultModel(AditumCode.ERROR);
        }
        log.debug("Person [GET] SUCCESS : {} -> {}", person, personList);
        return new ResultModel(AditumCode.OK, personList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(@RequestBody Person person) {
        log.debug("Person [POST] : {}", person);
        Person entity = new Person()
                .setPersonnelId(UidGenerator.generateUid())
                .setPersonnelName(person.getPersonnelName())
                .setCommunityId(person.getCommunityId())
                .setPersonnelAddress(person.getPersonnelAddress())
                .setPersonnelPhone(person.getPersonnelPhone())
                .setCreateTime(TimeGenerator.currentTime())
                .setUpdateTime(TimeGenerator.currentTime())
                .setIsDeleted(NO_DELETED);

        int result = service.insert(entity);
        if (result < 1) {
            log.warn("Person [POST] FAILURE : {}", person);
            return new ResultModel(AditumCode.ERROR);
        }
        log.debug("Person [POST] SUCCESS : {}", entity);
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
