package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.PersonasPortrait;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.PersonasLabelService;
import com.ten.aditum.back.service.PersonasPortraitService;
import com.ten.aditum.back.service.PersonasService;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 用户画像信息
 */
@Slf4j
@RestController
@RequestMapping(value = "/personas")
public class PersonasController extends BaseController {

    private Random random = new Random();

    private final PersonasService personasService;
    private final PersonasLabelService personasLabelService;
    private final PersonasPortraitService personasPortraitService;

    @Autowired
    public PersonasController(PersonasService personasService,
                              PersonasLabelService personasLabelService,
                              PersonasPortraitService personasPortraitService) {
        this.personasService = personasService;
        this.personasLabelService = personasLabelService;
        this.personasPortraitService = personasPortraitService;
    }

    /**
     * 根据personId获取用户画像标签集合
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel getPersonas(Personas personas) {
        log.info("Personas [GET] : {}", personas);

        if (personas.getPersonnelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasPortrait personasPortrait = new PersonasPortrait()
                .setPersonnelId(personas.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasPortrait> personasPortraitList = personasPortraitService.select(personasPortrait);
        if (personasPortraitList.size() < 1) {
            log.warn("Personas [GET] FAILURE : {}", personas);
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasPortrait select = personasPortraitList.get(0);

        String personasExt = select.getPersonasExt();
        String[] personasList = personasExt.split(",");
        if (personasList.length == 0) {
            personasList = new String[]{"当前用户暂时没有用户画像"};
        }

        Personas personasEntity = new Personas()
                .setPersonnelId(select.getPersonnelId())
                .setPersonasList(Arrays.asList(personasList));

        log.info("Personas [GET] SUCCESS : {} -> {}", personas, personasEntity);
        return new ResultModel(AditumCode.OK, personasEntity);
    }

    /**
     * 根据personId获取用户画像标签集合(带随机权重)
     */
    @RequestMapping(value = "/weight", method = RequestMethod.GET)
    public ResultModel getPersonasWeight(Personas personas) {
        log.info("PersonasWeight [GET] : {}", personas);

        if (personas.getPersonnelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasPortrait personasPortrait = new PersonasPortrait()
                .setPersonnelId(personas.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasPortrait> personasPortraitList = personasPortraitService.select(personasPortrait);
        if (personasPortraitList.size() < 1) {
            log.warn("PersonasWeight [GET] FAILURE : {}", personas);
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasPortrait select = personasPortraitList.get(0);

        String personasExt = select.getPersonasExt();
        String[] personasList = personasExt.split(",");
        if (personasList.length == 0) {
            personasList = new String[]{"当前用户暂时没有用户画像"};
        }

        List<Personas.WeightsLabel> weightsLabelList = new ArrayList<>();
        for (String personasLabel : personasList) {
            Personas.WeightsLabel weightsLabel = new Personas.WeightsLabel();
            weightsLabel.setName(personasLabel);
            weightsLabel.setValue(random.nextInt(100));
            weightsLabelList.add(weightsLabel);
        }

        Personas personasEntity = new Personas()
                .setPersonnelId(select.getPersonnelId())
                .setPersonasWeightList(weightsLabelList);

        log.info("PersonasWeight [GET] SUCCESS : {} -> {}", personas, personasEntity);
        return new ResultModel(AditumCode.OK, personasEntity);
    }

    /**
     * 根据labelId更新用户画像标签，labelId必须存在于label表中并且对应
     */
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public ResultModel updatePersonasByLabelId(Personas personas) {
        log.info("updatePersonasByLabelId [POST] : {}", personas);

        if (personas.getPersonnelId() == null || personas.getLabelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        personasService.updatePersonas(personas);

        log.info("updatePersonasByLabelId [POST] SUCCESS : {}", personas);
        return new ResultModel(AditumCode.OK, personas);
    }

    /**
     * 根据labelId更新用户画像标签，name可以不存在于label表中
     */
    @RequestMapping(value = "/updateByName", method = RequestMethod.POST)
    public ResultModel updatePersonasByLabelName(Personas personas) {
        log.info("updatePersonasByLabelName [POST] : {}", personas);

        if (personas.getPersonnelId() == null || personas.getLabelName() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        personasService.updatePersonasByLabelName(personas);

        log.info("updatePersonasByLabelName [POST] SUCCESS : {}", personas);
        return new ResultModel(AditumCode.OK, personas);
    }

}
