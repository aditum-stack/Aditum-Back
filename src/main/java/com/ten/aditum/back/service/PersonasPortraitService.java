package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.PersonasPortrait;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.vo.Personas;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.mapper.PersonasPortraitDao;

@Service
public class PersonasPortraitService {

    @Resource
    private PersonasPortraitDao personasPortraitDao;

    public int insert(PersonasPortrait pojo) {
        return personasPortraitDao.insert(pojo);
    }

    public int insertList(List<PersonasPortrait> pojos) {
        return personasPortraitDao.insertList(pojos);
    }

    public List<PersonasPortrait> select(PersonasPortrait pojo) {
        return personasPortraitDao.select(pojo);
    }

    public int update(PersonasPortrait pojo) {
        return personasPortraitDao.update(pojo);
    }

    /**
     * 根据标签创建或者更新用户画像
     */
    private void createOrUpdatePortrait(Personas personas,
                                        List<String> labelList,
                                        boolean existed,
                                        int portraitId) {
        if (existed) {
            PersonasPortrait update = new PersonasPortrait()
                    .setId(portraitId)
                    .setPersonasExt(String.join(",", labelList))
                    .setUpdateTime(TimeGenerator.currentTime());
            update(update);
        }
        // 未存在，创建
        else {
            PersonasPortrait create = new PersonasPortrait()
                    .setPersonnelId(personas.getPersonnelId())
                    .setPersonasExt(String.join(",", labelList))
                    .setCreateTime(TimeGenerator.currentTime())
                    .setIsDeleted(0);
            insert(create);
        }
    }
}
