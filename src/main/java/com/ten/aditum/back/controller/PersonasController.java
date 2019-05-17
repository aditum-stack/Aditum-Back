package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.*;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.*;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/personas")
public class PersonasController {

    private static final int NO_DELETED = 0;
    private static final int IS_DELETED = 1;

    private final PersonService personService;
    private final PersonasLabelService personasLabelService;
    private final PersonasPortraitService personasPortraitService;

    @Autowired
    public PersonasController(PersonService personService,
                              PersonasLabelService personasLabelService,
                              PersonasPortraitService personasPortraitService) {
        this.personService = personService;
        this.personasLabelService = personasLabelService;
        this.personasPortraitService = personasPortraitService;
    }

    /**
     * 根据personId获取用户画像标签集合
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel getPersonas(Personas personas) {
        log.debug("Personas [GET] : {}", personas);

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
            personasList = new String[]{"小白"};
        }

        Personas personasEntity = new Personas()
                .setPersonnelId(select.getPersonnelId())
                .setPersonasList(Arrays.asList(personasList));

        log.debug("Personas [GET] SUCCESS : {} -> {}", personas, personasEntity);
        return new ResultModel(AditumCode.OK, personasEntity);
    }

    /**
     * 根据personId获取用户画像标签集合(带随机权重)
     */
    @RequestMapping(value = "/weight", method = RequestMethod.GET)
    public ResultModel getPersonasWeight(Personas personas) {
        log.debug("PersonasWeight [GET] : {}", personas);

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
            personasList = new String[]{"小白"};
        }

        List<Personas.WeightsLabel> weightsLabelList = new ArrayList<>();
        for (String personasLabel : personasList) {
            Personas.WeightsLabel weightsLabel = new Personas.WeightsLabel();
            weightsLabel.setName(personasLabel);
            weightsLabel.setValue((int) (Math.random() * 100));
            weightsLabelList.add(weightsLabel);
        }

        Personas personasEntity = new Personas()
                .setPersonnelId(select.getPersonnelId())
                .setPersonasWeightList(weightsLabelList);

        log.debug("PersonasWeight [GET] SUCCESS : {} -> {}", personas, personasEntity);
        return new ResultModel(AditumCode.OK, personasEntity);
    }

    /**
     * 根据personId更新该用户的用户画像标签
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel updatePersonas(Personas personas) {
        log.debug("Personas [POST] : {}", personas);

        if (personas.getPersonnelId() == null || personas.getLabelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        // select portrait
        PersonasPortrait personasPortrait = new PersonasPortrait()
                .setPersonnelId(personas.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasPortrait> personasPortraitList = personasPortraitService.select(personasPortrait);

        // personas model array
        List<String> labelList = new ArrayList<>();
        boolean existed;
        int portraitId = 0;

        // 当前用户画像不存在
        if (personasPortraitList.size() < 1) {
            existed = false;
            labelList.add("新用户");
            labelList.add("青铜会员");
        }
        // 当前用户画像已存在
        else {
            existed = true;
            PersonasPortrait select = personasPortraitList.get(0);
            portraitId = select.getId();
            String personasExt = select.getPersonasExt();
            String[] personasList = personasExt.split(",");
            labelList.addAll(Arrays.asList(personasList));
        }

        // select model
        PersonasLabel personasLabel = new PersonasLabel()
                .setLabelId(personas.getLabelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasLabel> personasLabelList = personasLabelService.select(personasLabel);
        if (personasLabelList.size() < 1) {
            log.warn("Personas [POST] Label FAILURE : {}", personas);
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasLabel selectLabel = personasLabelList.get(0);
        String labelName = selectLabel.getLabelName();

        // 未包含此标签
        if (!labelList.contains(labelName)) {
            labelList.add(labelName);
        }

        // 已存在，更新
        if (existed) {
            PersonasPortrait update = new PersonasPortrait()
                    .setId(portraitId)
                    .setPersonasExt(String.join(",", labelList))
                    .setUpdateTime(TimeGenerator.currentTime());
            personasPortraitService.update(update);

            log.debug("Personas [POST] update SUCCESS : {} -> {}", personas, update);
            return new ResultModel(AditumCode.OK, update);
        }
        // 未存在，创建
        else {
            PersonasPortrait create = new PersonasPortrait()
                    .setPersonnelId(personas.getPersonnelId())
                    .setPersonasExt(String.join(",", labelList))
                    .setCreateTime(TimeGenerator.currentTime())
                    .setIsDeleted(NO_DELETED);
            personasPortraitService.insert(create);

            log.debug("Personas [POST] create SUCCESS : {} -> {}", personas, create);
            return new ResultModel(AditumCode.OK, create);
        }
    }

    /**
     * 根据personId更新该用户的用户画像标签，可直接通过Name添加，而无需在Label表中有对应项
     */
    @RequestMapping(value = "/name", method = RequestMethod.POST)
    public ResultModel updatePersonasByLabelName(Personas personas) {
        log.debug("Personas by name [POST] : {}", personas);

        if (personas.getPersonnelId() == null || personas.getLabelName() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        // select portrait
        PersonasPortrait personasPortrait = new PersonasPortrait()
                .setPersonnelId(personas.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasPortrait> personasPortraitList = personasPortraitService.select(personasPortrait);

        // personas model array
        List<String> labelList = new ArrayList<>();
        boolean existed;
        int portraitId = 0;

        // 当前用户画像不存在
        if (personasPortraitList.size() < 1) {
            existed = false;
            labelList.add("新用户");
            labelList.add("青铜会员");
        }
        // 当前用户画像已存在
        else {
            existed = true;
            PersonasPortrait select = personasPortraitList.get(0);
            portraitId = select.getId();
            String personasExt = select.getPersonasExt();
            String[] personasList = personasExt.split(",");
            labelList.addAll(Arrays.asList(personasList));
        }

        String labelName = personas.getLabelName();

        // 未包含此标签
        if (!labelList.contains(labelName)) {
            labelList.add(labelName);
        }

        // 已存在，更新
        if (existed) {
            PersonasPortrait update = new PersonasPortrait()
                    .setId(portraitId)
                    .setPersonasExt(String.join(",", labelList))
                    .setUpdateTime(TimeGenerator.currentTime());
            personasPortraitService.update(update);

            log.debug("Personas by name [POST] update SUCCESS : {} -> {}", personas, update);
            return new ResultModel(AditumCode.OK, update);
        }
        // 未存在，创建
        else {
            PersonasPortrait create = new PersonasPortrait()
                    .setPersonnelId(personas.getPersonnelId())
                    .setPersonasExt(String.join(",", labelList))
                    .setCreateTime(TimeGenerator.currentTime())
                    .setIsDeleted(NO_DELETED);
            personasPortraitService.insert(create);

            log.debug("Personas by name [POST] create SUCCESS : {} -> {}", personas, create);
            return new ResultModel(AditumCode.OK, create);
        }
    }

    /**
     * 根据personId删除该用户的用户画像标签
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResultModel removePersonas(Personas personas) {
        log.debug("Personas [DELETE] : {}", personas);

        if (personas.getPersonnelId() == null || personas.getLabelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        // select portrait
        PersonasPortrait personasPortrait = new PersonasPortrait()
                .setPersonnelId(personas.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasPortrait> personasPortraitList = personasPortraitService.select(personasPortrait);
        if (personasPortraitList.size() < 1) {
            log.warn("Personas [DELETE] 当前用户画像不存在 : {}", personas);
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasPortrait select = personasPortraitList.get(0);
        int portraitId = select.getId();
        String personasExt = select.getPersonasExt();
        String[] personasList = personasExt.split(",");
        List<String> labelList = new ArrayList<>(Arrays.asList(personasList));

        // select model
        PersonasLabel personasLabel = new PersonasLabel()
                .setLabelId(personas.getLabelId())
                .setIsDeleted(NO_DELETED);

        List<PersonasLabel> personasLabelList = personasLabelService.select(personasLabel);
        if (personasLabelList.size() < 1) {
            log.warn("Personas [DELETE] Label FAILURE : {}", personas);
            return new ResultModel(AditumCode.ERROR);
        }

        PersonasLabel selectLabel = personasLabelList.get(0);
        String labelName = selectLabel.getLabelName();

        // 若包含此标签，删除
        labelList.remove(labelName);

        PersonasPortrait update = new PersonasPortrait()
                .setId(portraitId)
                .setPersonasExt(String.join(",", labelList))
                .setUpdateTime(TimeGenerator.currentTime());
        personasPortraitService.update(update);

        log.debug("Personas [DELETE] remove SUCCESS : {} -> {}", personas, update);
        return new ResultModel(AditumCode.OK, update);
    }

}
