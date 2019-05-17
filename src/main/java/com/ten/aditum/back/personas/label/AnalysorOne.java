package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.service.AccessTimeService;
import com.ten.aditum.back.vo.Personas;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ten.aditum.back.util.TimeGenerator.getTotalSec;

/**
 * 基于早晚访问时间的二维分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorOne extends BaseAnalysor {

    private final AccessTimeService accessTimeService;

    @Autowired
    public AnalysorOne(AccessTimeService accessTimeService) {
        this.accessTimeService = accessTimeService;
    }

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("1")
                .setLabelName("轻松工作")
                .setLabelDesc("朝九晚五");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("2")
                .setLabelName("弹性工作")
                .setLabelDesc("朝十晚六");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("3")
                .setLabelName("忙碌工作")
                .setLabelDesc("朝九晚九");
        PersonasLabel label4 = new PersonasLabel()
                .setLabelId("23")
                .setLabelName("工作狂")
                .setLabelDesc("朝八晚十");
    }

    /**
     *
     */
    @Scheduled(cron = TEST_TIME)
//    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void analysis() {
        log.info("基于早晚访问时间的二维分析...开始");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("基于早晚访问时间的二维分析...结束");
    }

    private void analysisPerson(Person person) {
        // 获取AccessTime
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessTime记录, {}", person);
            return;
        }

        // 获取AccessTime
        AccessTime theAccessTime = select.get(0);
        String earliest = theAccessTime.getAverageEarliestAccessTime();
        String latest = theAccessTime.getAverageLatestAccessTime();

        // 计算最接近模型的索引
        int index = distanceTime(earliest, latest);
        int labelId = modelList.get(index).labelId;

        // 更新标签
        Personas personas = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelId(String.valueOf(labelId));

        log.info("用户 {} 计算完成，{}", person.getPersonnelName(), personas);

        personasController.updatePersonas(personas);
    }

    /**
     * 预设模型的二维时间
     */
    private static List<TimeDistance> modelList = new ArrayList<>();

    static {
        modelList.add(new TimeDistance(getTotalSec("9:00:00"), getTotalSec("17:00:00"), 1));
        modelList.add(new TimeDistance(getTotalSec("10:00:00"), getTotalSec("18:00:00"), 2));
        modelList.add(new TimeDistance(getTotalSec("9:00:00"), getTotalSec("21:00:00"), 3));
        modelList.add(new TimeDistance(getTotalSec("8:00:00"), getTotalSec("22:00:00"), 23));
    }

    /**
     * 计算二维时间距离，返回距离最近的模型的索引
     */
    private int distanceTime(String earliest, String latest) {
        long es = getTotalSec(earliest);
        long ls = getTotalSec(latest);

        TimeDistance the = new TimeDistance(es, ls, 0);

        double min = 0;
        int index = 0;

        for (int i = 0; i < modelList.size(); i++) {
            double distance = distance(the, modelList.get(i));
            // 第一轮
            if (i == 0) {
                min = distance;
                index = 0;
            }
            // 后续轮
            else {
                // 距离小于最小值
                if (distance < min) {
                    min = distance;
                    index = i;
                }
            }

        }

        return index;
    }

    /**
     * 计算二维距离
     */
    private double distance(TimeDistance the, TimeDistance other) {
        double ed = Math.pow(the.es - other.es, 2);
        double ld = Math.pow(the.ls - other.ls, 2);
        return Math.sqrt(ed + ld);
    }

    /**
     * 二维秒数辅助类
     */
    @AllArgsConstructor
    private static class TimeDistance {
        long es;
        long ls;

        int labelId;
    }

}
