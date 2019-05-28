package com.ten.aditum.back.personas.label;


import com.github.pagehelper.util.StringUtil;
import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 电话号运营商动态标签
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class PhoneAnalysor extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天0点20分执行
     */
    @Scheduled(cron = "0 20 0 1/1 * ?")
    public void analysis() {
        log.info("电话号运营商动态标签...开始");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("电话号运营商动态标签...结束");
    }

    /**
     * 分析运营商并更新用户画像
     */
    private void analysisPerson(Person person) {
        String phone = person.getPersonnelPhone();
        if (StringUtil.isEmpty(phone)) {
            log.warn("用户 {} 没有电话号", person.getPersonnelName());
            return;
        }

        String operator = chinaMobilePhone(phone);
        String labelName = "运营商:" + operator;
        Personas label = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName(labelName);
        personasService.updatePersonasByLabelName(label);
        log.info("用户 {} 运营商标签计算完成，{}", person.getPersonnelName(), labelName);
    }

    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700,173,199
     **/
    private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|77|73|99|8[019])\\d{8}$)|(^1700\\d{7}$)";

    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709
     **/
    private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";

    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     **/
    private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

    /**
     * 查询电话属于哪个运营商
     *
     * @param tel 手机号码
     * @return 0：不属于任何一个运营商，1:移动，2：联通，3：电信
     */
    private String chinaMobilePhone(String tel) {
        boolean b1 = tel != null && !"".equals(tel.trim()) && match(CHINA_MOBILE_PATTERN, tel);
        if (b1) {
            return "移动";
        }
        b1 = tel != null && !"".equals(tel.trim()) && match(CHINA_UNICOM_PATTERN, tel);
        if (b1) {
            return "联通";
        }
        b1 = tel != null && !"".equals(tel.trim()) && match(CHINA_TELECOM_PATTERN, tel);
        if (b1) {
            return "电信";
        }
        return "其他";
    }

    /**
     * 匹配函数
     */
    private static boolean match(String regex, String tel) {
        return Pattern.matches(regex, tel);
    }
}
