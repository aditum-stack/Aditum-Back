package com.ten.aditum.back;

import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.service.PersonService;
import com.ten.aditum.back.service.PersonasPortraitService;
import com.ten.aditum.back.service.PersonasService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public abstract class BaseAnalysor {

    /*
     * 1 基于早晚访问时间的二维分析
     * 2 基于早或晚时间的一维分析
     * 3 基于访问频次的一维分析
     * 4 基于用户信息的数据分析
     * 5 基于用户访问地理信息的分析
     * 6 基于访问时长的分析
     * 7 基于排名的数据分析
     */

    /**
     * 测试时间，每分钟执行一次
     */
    protected static final String TEST_TIME = "0 0/1 * * * ?";

    protected static final int NO_DELETED = 0;

    @Resource
    protected PersonasService personasService;
    @Resource
    protected PersonService personService;
    @Resource
    protected PersonasPortraitService personasPortraitService;

    /**
     * 在子类中展示本类要分析的标签
     */
    public void showModelLabel() {
    }

    protected List<Person> selectAllPerson() {
        Person personEntity = new Person()
                .setIsDeleted(NO_DELETED);
        List<Person> personList = personService.select(personEntity);
        log.info("所有Person集合 : {}", personList);
        return personList;
    }

}
