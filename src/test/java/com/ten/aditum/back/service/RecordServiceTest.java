package com.ten.aditum.back.service;

import com.ten.aditum.back.AditumWebApplication;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AditumWebApplication.class)
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: insert(Record pojo)
     */
    @Test
    public void testInsert() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: insertList(List<Record> pojos)
     */
    @Test
    public void testInsertList() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: select(Record pojo)
     */
    @Test
    public void testSelect() throws Exception {
        Record pojo = new Record();
        pojo.setId(10000);
        List<Record> recordList = recordService.selectAfterTheId(pojo);
        System.out.println(recordList);
    }

    /**
     * Method: selectAfterTime(Record pojo)
     */
    @Test
    public void testSelectAfterTime() throws Exception {
        Record pojo = new Record();
        pojo.setVisiteTime(TimeGenerator.currentDateTime());
        System.out.println(pojo);
        List<Record> recordList = recordService.selectAfterTheDateTime(pojo);
        System.out.println(recordList);
    }

    /**
     * Method: update(Record pojo)
     */
    @Test
    public void testUpdate() throws Exception {
//TODO: Test goes here... 
    }


} 
