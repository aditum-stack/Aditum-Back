package com.ten.aditum.back.controller;


import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public abstract class BaseController<Entity> {

    public static final int NO_DELETED = 0;
    public static final int IS_DELETED = 1;

    /* Restful Interface */

    @RequestMapping(method = RequestMethod.GET)
    public abstract ResultModel get(Entity entity);

    @RequestMapping(method = RequestMethod.POST)
    public abstract ResultModel post(Entity entity);

    @RequestMapping(method = RequestMethod.PUT)
    public abstract ResultModel update(Entity entity);

    @RequestMapping(method = RequestMethod.DELETE)
    public abstract ResultModel delete(Entity entity);

}
