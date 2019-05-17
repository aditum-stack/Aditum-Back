package com.ten.aditum.back.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户画像交互数据
 */
@Data
@Accessors(chain = true)
public class Personas {
    /**
     * 用户ID
     */
    private String personnelId;
    /**
     * 标签ID
     */
    private String labelId;
    /**
     * 标签名称
     */
    private String labelName;
    /**
     * 标签类别
     */
    private Integer labelType;
    /**
     * 用户画像标签集合
     */
    private List<String> personasList;
    /**
     * 用户画像标签集合 (权值)
     */
    private List<WeightsLabel> personasWeightList;

    @Data
    public static class WeightsLabel {
        String name;
        Integer value;
    }
}
