package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 居住地动态标签
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AddressAnalysor extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天0点10分执行
     */
    @Scheduled(cron = "0 10 0 1/1 * ?")
    public void analysis() {
        log.info("居住地动态标签...开始");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("居住地动态标签...结束");
    }

    private void analysisPerson(Person person) {
        String communityId = person.getCommunityId();

        Community community = new Community()
                .setCommunityId(communityId)
                .setIsDeleted(NO_DELETED);

        List<Community> select = communityService.select(community);
        if (select.size() < 1) {
            throw new RuntimeException("用户所属社区缺失！ Cid is" + communityId);
        }

        Community entity = select.get(0);
        String communityName = entity.getCommunityName();

        String labelName = "居住:" + communityName;

        Personas label = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName(labelName);

        personasService.updatePersonasByLabelName(label);

        log.info("用户 {} 居住地标签计算完成，{}", person.getPersonnelName(), labelName);
    }

}
