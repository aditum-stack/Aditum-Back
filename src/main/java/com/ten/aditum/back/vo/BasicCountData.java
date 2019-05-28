package com.ten.aditum.back.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 基础数据数量展示
 */
@Data
@Accessors(chain = true)
public class BasicCountData {
    /**
     * 查询的社区ID
     */
    private String communityId;

    /**
     * 总量展示
     */
    private int communityCount;
    private int deviceCount;
    private int personCount;
    private int recordCount;

    /**
     * 最近一周每天的总量和增量显示
     */
    private List<TotalAndIncrement> communityCountList;
    private List<TotalAndIncrement> deviceCountList;
    private List<TotalAndIncrement> personCountList;
    private List<TotalAndIncrement> recordCountList;

    /**
     * 最近一周的总量和增量信息
     */
    @Data
    public static class TotalAndIncrement {
        private int totalCount;
        private int increaseCount;
    }
}
